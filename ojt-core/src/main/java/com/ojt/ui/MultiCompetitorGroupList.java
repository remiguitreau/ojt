/**
 * 
 */
package com.ojt.ui;

import org.apache.log4j.Logger;

import com.ojt.Categorie;
import com.ojt.Club;
import com.ojt.Competitor;
import com.ojt.CompetitorCategory;
import com.ojt.CompetitorGroup;
import com.ojt.CompetitorList;
import com.ojt.algo.Algorithme;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.Scrollable;

/**
 * Table representing a list of lists, which organisation can be modified.
 * @author Rmi "DwarfConan" Guitreau
 * @since 28 mars 08 : Creation.
 */
public class MultiCompetitorGroupList extends JComponent implements MultiListSlidable, Scrollable {

	private final static int LABEL_SPACE = 150;

	private final List<CompetitorGroup> competitorGroups;

	private JList groupsList;

	private int cellHeight = -1;

	private MultiListSlider slider;

	private int maxListSize;

	private MouseListener mouseListener;

	private final Logger logger = Logger.getLogger(getClass());

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	public MultiCompetitorGroupList(final List<CompetitorGroup> competitorGroups, final int maxListSize) {
		super();

		this.competitorGroups = competitorGroups;
		this.maxListSize = maxListSize;
		initComponents();
		initListeners();
	}

	/**
	 * @return the maxListSize
	 */
	public int getMaxListSize() {
		return maxListSize;
	}

	/**
	 * @param maxListSize the maxListSize to set
	 */
	public void setMaxListSize(final int maxListSize) {
		this.maxListSize = maxListSize;
	}

	public List<CompetitorGroup> getCompetitorGroups() {
		return competitorGroups;
	}

	@Override
	public int getListElementHeight() {
		if (cellHeight == -1) {
			cellHeight = groupsList.getCellRenderer().getListCellRendererComponent(groupsList, "Test", 0,
					false, false).getPreferredSize().height;
		}
		return cellHeight;
	}

	@Override
	public int getListHeight(final int listIdx) {
		return getCompetitorsList(listIdx).size() * getListElementHeight();
	}

	@Override
	public int getNbLists() {
		return competitorGroups == null ? 0 : competitorGroups.size();
	}

	@Override
	public void memberMoved(final int srcList, final int destList) {
		if (getCompetitorsList(destList).size() < 16) {
			if (srcList == destList) {
				return;
			}
			if (srcList < destList) {
				final Competitor tmpObj = getCompetitorsList(srcList).remove(
						getCompetitorsList(srcList).size() - 1);
				getCompetitorsList(destList).add(0, tmpObj);
			} else {
				final Competitor tmpObj = getCompetitorsList(srcList).remove(0);
				getCompetitorsList(destList).add(tmpObj);
			}
			updateList();
			repaint();
		}
	}

	@Override
	public boolean listCanGrow(final int listIdx) {
		return getCompetitorsList(listIdx).size() < maxListSize;
	}

	@Override
	public boolean listCanDiminue(final int listIdx) {
		return getCompetitorsList(listIdx).size() > 1;
	}

	@Override
	public boolean isListIgnored(final int listIdx) {
		return getCompetitorGroups().get(listIdx).isSavedForAnotherWeighing();
	}

	@Override
	public int getNbElements(final int listIdx) {
		return getCompetitorsList(listIdx).size();
	}

	@Override
	public Dimension getPreferredScrollableViewportSize() {
		final Dimension dim = groupsList.getPreferredScrollableViewportSize();
		return new Dimension((int) (LABEL_SPACE + dim.getWidth() + slider.getWidth()), (int) dim.getHeight());
	}

	@Override
	public int getScrollableBlockIncrement(final Rectangle visibleRect, final int orientation,
			final int direction) {
		return groupsList.getScrollableBlockIncrement(visibleRect, orientation, direction);
	}

	@Override
	public boolean getScrollableTracksViewportHeight() {
		return groupsList.getScrollableTracksViewportHeight();
	}

	@Override
	public boolean getScrollableTracksViewportWidth() {
		return groupsList.getScrollableTracksViewportWidth();
	}

	@Override
	public int getScrollableUnitIncrement(final Rectangle visibleRect, final int orientation,
			final int direction) {
		return groupsList.getScrollableUnitIncrement(visibleRect, orientation, direction);
	}

	// -------------------------------------------------------------------------
	// Protégées
	// -------------------------------------------------------------------------
	/**
	 * Fusionne deux groupes.
	 */
	protected void mergeGroups(final int groupIdx1, final int groupIdx2) {
		// Récupération et suppression du groupe 2.
		final CompetitorGroup group2 = competitorGroups.remove(groupIdx2);
		final CompetitorGroup group1 = competitorGroups.get(groupIdx1);
		// Fusion des deux groupes.
		group1.getCompetitors().addAll(group2.getCompetitors());
		slider.setMultiListSlidable(this);
		repaint();
	}

	/**
	 * Divise un groupe.
	 */
	protected void divideGroup(final int groupIdx) {
		// Récupération et suppression du groupe 2.
		final CompetitorGroup group = competitorGroups.remove(groupIdx);
		final CompetitorList list1 = new CompetitorList();
		final CompetitorList list2 = new CompetitorList();
		// Répartition dans les deux groupes.
		final int middle = group.getCompetitors().size() / 2;
		for (int i = 0; i < middle; i++) {
			list1.add(group.getCompetitors().get(i));
		}
		for (int i = middle; i < group.getCompetitors().size(); i++) {
			list2.add(group.getCompetitors().get(i));
		}
		competitorGroups.add(groupIdx, new CompetitorGroup(list2, new Algorithme()));
		competitorGroups.add(groupIdx, new CompetitorGroup(list1, new Algorithme()));
		slider.setMultiListSlidable(this);
		repaint();
	}

	// -------------------------------------------------------------------------
	// Privï¿½es
	// -------------------------------------------------------------------------
	private void initListeners() {
		mouseListener = new MouseAdapter() {
			@Override
			public void mouseClicked(final MouseEvent evt) {
				// Sur clic droit affichage du menu
				if (evt.getButton() == MouseEvent.BUTTON3) {
					showMenuForList(getListIndexAtY(evt.getY()), evt.getPoint());
				}
			}
		};
		// addMouseListener(mouseListener);
		groupsList.addMouseListener(mouseListener);
	}

	private int getListIndexAtY(final int y) {
		int currHeight = 0;
		for (int i = 0; i < competitorGroups.size(); i++) {
			currHeight += getListHeight(i);
			if (currHeight > y) {
				return i;
			}
		}
		return competitorGroups.size() - 1;
	}

	private void showMenuForList(final int listIdx, final Point point) {
		final JPopupMenu popup = new JPopupMenu();
		final JMenuItem itemPrevious = new JMenuItem("Fusionner avec le groupe précédent");
		itemPrevious.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				mergeGroups(listIdx - 1, listIdx);
			}
		});
		popup.add(itemPrevious);
		itemPrevious.setEnabled(!isListIgnored(listIdx)
				&& ((listIdx > 0) && ((getNbElements(listIdx - 1) + getNbElements(listIdx)) <= maxListSize) && !isListIgnored(listIdx - 1)));

		final JMenuItem itemNext = new JMenuItem("Fusionner avec le groupe suivant");
		itemNext.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				mergeGroups(listIdx, listIdx + 1);
			}
		});
		popup.add(itemNext);
		itemNext.setEnabled(!isListIgnored(listIdx)
				&& ((listIdx < getNbLists() - 1)
						&& ((getNbElements(listIdx) + getNbElements(listIdx + 1)) <= maxListSize) && !isListIgnored(listIdx + 1)));

		final JMenuItem itemDivide = new JMenuItem("Diviser le groupe");
		itemDivide.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				divideGroup(listIdx);
			}
		});
		popup.add(itemDivide);
		itemDivide.setEnabled(!isListIgnored(listIdx) && (getNbElements(listIdx) > 1));

		if (competitorGroups.get(listIdx).isSavedForAnotherWeighing()) {
			final JMenuItem itemIncludeInCurrentSort = new JMenuItem("Inclure dans le tri en cours");
			itemIncludeInCurrentSort.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(final ActionEvent e) {
					competitorGroups.get(listIdx).setSavedForAnotherWeighing(false);
					slider.listIgnoredChanged(listIdx,
							competitorGroups.get(listIdx).isSavedForAnotherWeighing());
					repaint();
				}
			});
			popup.add(itemIncludeInCurrentSort);
		} else {
			final JMenuItem itemSavedForAnotherSort = new JMenuItem("Réserver pour un prochain tri");
			itemSavedForAnotherSort.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(final ActionEvent e) {
					competitorGroups.get(listIdx).setSavedForAnotherWeighing(true);
					slider.listIgnoredChanged(listIdx,
							competitorGroups.get(listIdx).isSavedForAnotherWeighing());
					repaint();
				}
			});
			popup.add(itemSavedForAnotherSort);
		}

		popup.show(this, point.x, point.y);
	}

	private void updateLocations() {
		groupsList.setBounds(LABEL_SPACE, 0, groupsList.getPreferredSize().width,
				groupsList.getPreferredSize().height);
		slider.setBounds(LABEL_SPACE + groupsList.getWidth(), 0, slider.getWidth(), slider.getHeight());
	}

	private void initComponents() {
		groupsList = new JList();
		add(groupsList);

		slider = new MultiListSlider();
		add(slider);

		groupsList.setCellRenderer(new MultiListCellRenderer());

		updateList();
		slider.setMultiListSlidable(this);
		setPreferredSize(new Dimension(LABEL_SPACE + groupsList.getPreferredSize().width + slider.getWidth(),
				groupsList.getPreferredSize().height));
		setSize(getPreferredSize());
		updateLocations();
	}

	private CompetitorList getCompetitorsList(final int listIdx) {
		return competitorGroups.get(listIdx).getCompetitors();
	}

	private int getListIndexOfCompetitor(final Competitor comp) {
		for (int i = 0; i < competitorGroups.size(); i++) {
			if (getCompetitorsList(i).contains(comp)) {
				return i;
			}
		}
		return -1;
	}

	private void updateList() {
		final List<Competitor> toDisplay = new ArrayList<Competitor>();
		for (final CompetitorGroup data : competitorGroups) {
			logger.info("Add to display = " + data.getCompetitors().size());
			for (final Competitor comp : data.getCompetitors()) {
				logger.info("  - " + comp.toString());
			}
			toDisplay.addAll(data.getCompetitors());
		}
		groupsList.setListData(toDisplay.toArray(new Object[toDisplay.size()]));
		logger.info("To display total = " + toDisplay.size());
	}

	public class MultiListCellRenderer extends DefaultListCellRenderer {
		@Override
		public Component getListCellRendererComponent(final JList list, final Object value, final int index,
				final boolean isSelected, final boolean cellHasFocus) {
			final JLabel comp = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected,
					cellHasFocus);
			if (value instanceof Competitor) {
				final boolean groupSaved = MultiCompetitorGroupList.this.getCompetitorGroups().get(
						MultiCompetitorGroupList.this.getListIndexOfCompetitor((Competitor) value)).isSavedForAnotherWeighing();
				if (groupSaved) {
					comp.setBackground(Color.LIGHT_GRAY);
					comp.setForeground(Color.GRAY);
					comp.setFont(comp.getFont().deriveFont(Font.ITALIC));
				} else {
					comp.setBackground(MultiListUtilities.getBackListColor(MultiCompetitorGroupList.this.getListIndexOfCompetitor((Competitor) value)));
					comp.setForeground(Color.BLACK);
					comp.setFont(comp.getFont().deriveFont(Font.PLAIN));
				}
			}
			return comp;
		}
	}

	@Override
	protected void paintComponent(final Graphics g) {
		super.paintComponent(g);
		int y = 0;
		for (int i = 0; i < competitorGroups.size(); i++) {
			final CompetitorGroup group = competitorGroups.get(i);
			final int listHeight = getListHeight(i);
			final float min = group.getMinGroupWeight();
			final float max = group.getMaxGroupWeight();
			final int purcent = group.getWeightStepInPercent();
			g.drawString(String.valueOf(min) + " à  " + String.valueOf(max) + " kg (" + purcent + "%)", 0, y
					+ listHeight / 2);
			y += listHeight;
		}
	}

	// -------------------------------------------------------------------------
	// MAIN de test
	// -------------------------------------------------------------------------

	public static void main(final String[] args) {
		final JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.setPreferredSize(new Dimension(800, 600));

		final List<CompetitorGroup> competitors = new ArrayList<CompetitorGroup>();
		for (int i = 0; i < 5; i++) {
			final CompetitorList compList = new CompetitorList();
			for (int j = 0; j < 10; j++) {
				final Competitor comp = new Competitor("Compétiteur " + i + "." + j, "prénom", new Club(
						"code" + j, "Dep." + j, "Club " + j), CompetitorCategory.JUNIOR, j);
				compList.add(comp);
			}
			competitors.add(new CompetitorGroup(compList, new Algorithme()));
		}
		final MultiCompetitorGroupList list = new MultiCompetitorGroupList(competitors, 16);

		final JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportView(list);
		frame.getContentPane().add(scrollPane);

		frame.pack();
		frame.setVisible(true);
	}
}
