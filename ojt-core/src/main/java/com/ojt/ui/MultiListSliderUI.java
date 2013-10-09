/**
 * 
 */
package com.ojt.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.plaf.ComponentUI;

/**
 * Look and feel interface for {@link MultiListSlider}.
 * @author Rmi "DwarfConan" Guitreau
 * @since 8 avr. 08 : Cration.
 */
public class MultiListSliderUI extends ComponentUI {

	public static ComponentUI createUI(final JComponent c) {
		return new MultiListSliderUI();
	}

	// -------------------------------------------------------------------------
	// Attributes
	// -------------------------------------------------------------------------

	private MultiListSlider slider;

	private final List<MultiListSliderButton> sliderButtons;

	private Point anchor;

	private final Object monAnchor = new Object();

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	public MultiListSliderUI() {
		super();

		this.sliderButtons = new ArrayList<MultiListSliderButton>();
	}

	// -------------------------------------------------------------------------
	// Surcharge de ComponentUI
	// -------------------------------------------------------------------------

	@Override
	public void installUI(final JComponent c) {
		slider = (MultiListSlider) c;
		installDefaults();
		updateSliderButtons();
	}

	// -------------------------------------------------------------------------
	// Public
	// -------------------------------------------------------------------------
	@Override
	public void paint(final Graphics g, final JComponent c) {
		int y = 0;
		if (slider.getMultiListSlidable() != null) {
			for (int i = 0; i < slider.getMultiListSlidable().getNbLists(); i++) {
				final int listHeight = slider.getMultiListSlidable().getListHeight(i);
				paintListRectangle(g, c, new Rectangle(0, y, 20, listHeight), i);
				y += listHeight;
			}
		}
		for (final MultiListSliderButton sliderButton : sliderButtons) {
			sliderButton.paint(g);
		}
	}

	// -------------------------------------------------------------------------
	// Protected
	// -------------------------------------------------------------------------
	protected void paintListRectangle(final Graphics graphics, final JComponent c, final Rectangle rectangle,
			final int listIdx) {
		graphics.setColor(slider.getMultiListSlidable().isListIgnored(listIdx) ? Color.GRAY
				: MultiListUtilities.getListColor(listIdx));
		graphics.fillRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
		// Dessin du nombre d'éléments dans la liste.
		graphics.setColor(Color.WHITE);
		final String strNbElts = String.valueOf(slider.getMultiListSlidable().getNbElements(listIdx));
		graphics.drawString(strNbElts, rectangle.x + rectangle.width / 2
				- graphics.getFontMetrics().charsWidth(strNbElts.toCharArray(), 0, strNbElts.length()) / 2,
				rectangle.y + rectangle.height / 2 + graphics.getFontMetrics().getHeight() / 2 - 1);
	}

	protected void updateSize() {
		updateSliderButtons();
		final Dimension dim;
		if (slider.getMultiListSlidable() == null) {
			dim = new Dimension(0, 0);
		} else {
			int height = 0;
			for (int i = 0; i < slider.getMultiListSlidable().getNbLists(); i++) {
				updateSliderButtonsLocation(i, height);
				height += slider.getMultiListSlidable().getListHeight(i);
			}
			dim = new Dimension(46, height);
		}
		slider.setPreferredSize(dim);
		slider.setSize(dim);
	}

	protected void updateSliderButtons() {
		uninstallComponents();
		installComponents();
		installListeners();
	}

	// -------------------------------------------------------------------------
	// Priv�es
	// -------------------------------------------------------------------------

	private void updateSliderButtonsLocation(final int listIndex, final int y) {
		final MultiListSliderButton sliderButton = getButton(listIndex);
		if (sliderButton != null) {
			sliderButton.setBounds(21, y, sliderButton.getPreferredSize().width,
					sliderButton.getPreferredSize().height);
		}
	}

	private MultiListSliderButton getButton(final int listIndex) {
		for (final MultiListSliderButton sliderButton : sliderButtons) {
			if (listIndex == sliderButton.getListIndex()) {
				return sliderButton;
			}
		}
		return null;
	}

	private void installListeners() {
		for (final MultiListSliderButton sliderButton : sliderButtons) {

			final MouseAdapter mouseAdapter = new MouseAdapter() {
				@Override
				public void mouseDragged(final MouseEvent evt) {
					synchronized (monAnchor) {
						if (anchor == null) {
							anchor = sliderButton.getLocation();
						}
					}
					final Point pt = evt.getLocationOnScreen();
					SwingUtilities.convertPointFromScreen(pt, slider);
					moveMultiListButton(sliderButton, pt);
				}

				@Override
				public void mousePressed(final MouseEvent evt) {
					synchronized (monAnchor) {
						anchor = sliderButton.getLocation();
					}
					sliderButton.paint(sliderButton.getGraphics());
				}

				@Override
				public void mouseReleased(final MouseEvent evt) {
					sliderButton.move(slider.getGraphics(), anchor);
					synchronized (monAnchor) {
						anchor = null;
						paint(slider.getGraphics(), slider);
					}
				}

				@Override
				public void mouseEntered(final MouseEvent e) {
					slider.repaint();
					sliderButton.paint(sliderButton.getGraphics());
				}

				@Override
				public void mouseExited(final MouseEvent e) {
					slider.repaint();
					sliderButton.paint(sliderButton.getGraphics());
				}
			};
			sliderButton.addMouseMotionListener(mouseAdapter);
			sliderButton.addMouseListener(mouseAdapter);
		}
	}

	private void moveMultiListButton(final MultiListSliderButton button, final Point currentPoint) {
		if (isAccessible(button, currentPoint.y)) {
			button.move(slider.getGraphics(), new Point(button.getLocation().x, currentPoint.y));
			checkListChanged(button);
		}
	}

	private void checkListChanged(final MultiListSliderButton button) {
		synchronized (monAnchor) {
			if ((button.getY() < anchor.y)
					&& (button.getY() < anchor.y - slider.getMultiListSlidable().getListElementHeight())) {
				slider.getMultiListSlidable().memberMoved(button.getListIndex() - 1, button.getListIndex());
				slider.repaint();
				anchor = new Point(anchor.x, anchor.y - slider.getMultiListSlidable().getListElementHeight());
			} else if ((button.getY() > anchor.y)
					&& (button.getY() > anchor.y + slider.getMultiListSlidable().getListElementHeight())) {
				slider.getMultiListSlidable().memberMoved(button.getListIndex(), button.getListIndex() - 1);
				slider.repaint();
				anchor = new Point(anchor.x, anchor.y + slider.getMultiListSlidable().getListElementHeight());
			}
		}
	}

	private boolean isAccessible(final MultiListSliderButton button, final int y) {
		synchronized (monAnchor) {
			if (y < anchor.y) {
				return slider.getMultiListSlidable().listCanGrow(button.getListIndex())
						&& !slider.getMultiListSlidable().isListIgnored(button.getListIndex())
						&& slider.getMultiListSlidable().listCanDiminue(button.getListIndex() - 1)
						&& !slider.getMultiListSlidable().isListIgnored(button.getListIndex() - 1);
			} else {
				return slider.getMultiListSlidable().listCanGrow(button.getListIndex() - 1)
						&& !slider.getMultiListSlidable().isListIgnored(button.getListIndex() - 1)
						&& slider.getMultiListSlidable().listCanDiminue(button.getListIndex())
						&& !slider.getMultiListSlidable().isListIgnored(button.getListIndex());
			}
		}

	}

	private void installComponents() {
		if ((slider.getMultiListSlidable() != null) && (slider.getMultiListSlidable().getNbLists() > 1)) {
			for (int i = 1, nbLists = slider.getMultiListSlidable().getNbLists(); i < nbLists; i++) {
				final MultiListSliderButton sliderButton = createSliderButton(i);
				applyVisibleStateToSliderButton(i, slider.getMultiListSlidable().isListIgnored(i),
						sliderButton);
				sliderButtons.add(sliderButton);
				slider.add(sliderButton);
			}
		}
	}

	private void uninstallComponents() {
		anchor = null;
		for (final MultiListSliderButton sliderButton : sliderButtons) {
			slider.remove(sliderButton);
		}
		slider.removeAll();
		sliderButtons.clear();
		slider.revalidate();
		slider.repaint();
	}

	private void installDefaults() {

	}

	private MultiListSliderButton createSliderButton(final int listIndex) {
		return new MultiListSliderButton(listIndex);
	}

	public void listIgnoredChanged(final int listIdx, final boolean ignored) {
		for (final MultiListSliderButton sliderButton : sliderButtons) {
			applyVisibleStateToSliderButton(listIdx, ignored, sliderButton);
		}
	}

	private void applyVisibleStateToSliderButton(final int listIdx, final boolean ignored,
			final MultiListSliderButton sliderButton) {
		if ((sliderButton.getListIndex() == listIdx) || (sliderButton.getListIndex() == listIdx + 1)) {
			if (ignored) {
				sliderButton.setVisible(false);
			} else {
				boolean visible = true;
				if ((sliderButton.getListIndex() == listIdx + 1)
						&& (listIdx < slider.getMultiListSlidable().getNbLists() - 1)) {
					visible = !slider.getMultiListSlidable().isListIgnored(listIdx + 1);
				} else if ((sliderButton.getListIndex() == listIdx) && (listIdx > 0)) {
					visible = !slider.getMultiListSlidable().isListIgnored(listIdx - 1);
				}
				sliderButton.setVisible(visible);
			}
		}
	}
}
