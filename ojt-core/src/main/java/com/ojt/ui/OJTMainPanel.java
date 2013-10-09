/**
 * 
 */
package com.ojt.ui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import com.ojt.OJTConfiguration;
import com.ojt.process.ProcessListener;
import com.ojt.process.SortProcess;
import com.ojt.process.SourceFileProcess;
import com.ojt.process.WeighingAndSortProcess;
import com.ojt.process.WeighingProcess;

/**
 * Panel principal d'OJT
 * @author Rémi "DwarfConan" Guitreau
 * @since 17 oct. 2009 : Création
 */
public class OJTMainPanel extends JPanel implements ConfigurationChangedListener {

	private final static Dimension BUTTON_SIZE = new Dimension(250, 28);

	private final OJTFrame ojtFrame;

	private ProcessListener processListener;

	private OjtButton generateSourceFileButton;

	private OjtButton weighingButton;

	private OjtButton sortButton;

	private OjtButton weighingAndSortButton;

	public OJTMainPanel(final OJTFrame ojtFrame) {
		super();

		this.ojtFrame = ojtFrame;
		initComponents();
		initListeners();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
//		((Graphics2D)g).setRenderingHint(hintKey, hintValue)
		g.drawImage(OjtUiConstants.PANEL_BACKGROUND.getImage(), 0, 0, getWidth(), getHeight(), null);		
	}

	// ---------------------------------------------------------
	// Privées
	// ---------------------------------------------------------

	private void initListeners() {
		processListener = new ProcessListener() {

			@Override
			public void processCanceled() {
				displayMainPanel();
			}

			@Override
			public void processEnded() {
				displayMainPanel();
			}
		};
	}

	private void initComponents() {
		setLayout(new GridBagLayout());
		int y = 0;
		generateSourceFileButton = new OjtButton("Générer un fichier source");
		generateSourceFileButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				startSourceFileProcess();
			}
		});
		generateSourceFileButton.setPreferredSize(BUTTON_SIZE);
		add(generateSourceFileButton, new GridBagConstraints(0, y++, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 5, 10, 5), 0, 0));
		
		weighingButton = new OjtButton("Pesée");
		weighingButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				startWeighingProcess();
			}
		});
		weighingButton.setPreferredSize(BUTTON_SIZE);
		add(weighingButton, new GridBagConstraints(0, y++, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.NONE, new Insets(10, 5, 10, 5), 0, 0));
		
		sortButton = new OjtButton("Tri");
		sortButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				startSortProcess();
			}
		});
		sortButton.setPreferredSize(BUTTON_SIZE);
		add(sortButton, new GridBagConstraints(0, y++, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.NONE, new Insets(10, 5, 10, 5), 0, 0));

		weighingAndSortButton = new OjtButton("Pesée + tri");
		weighingAndSortButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				startWeighingAndSortProcess();
			}
		});
		weighingAndSortButton.setPreferredSize(BUTTON_SIZE);
		add(weighingAndSortButton, new GridBagConstraints(0, y++, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.NONE, new Insets(10, 5, 10, 5), 0, 0));
		configurationUpdated();
	}

	void startSourceFileProcess() {
		startProcess(new SourceFileProcess(ojtFrame));
	}

	void startWeighingProcess() {
		startProcess(new WeighingProcess(ojtFrame, ojtFrame.getBalanceDriver()));
	}

	void startSortProcess() {
		startProcess(new SortProcess(ojtFrame, ojtFrame.getBalanceDriver()));
	}

	void startWeighingAndSortProcess() {
		startProcess(new WeighingAndSortProcess(ojtFrame, ojtFrame.getBalanceDriver()));
	}

	private void startProcess(final com.ojt.process.Process process) {
		process.addProcessListener(processListener);
		process.launch();
	}

	void displayMainPanel() {
		ojtFrame.setContentPane(this);
		ojtFrame.pack();
	}

	@Override
	public void configurationUpdated() {
		
	}

	@Override
	public void availableMenusChanged() {
		generateSourceFileButton.setVisible(OJTConfiguration.getInstance().getPropertyAsBoolean(OJTConfiguration.GENERATE_SOURCE_FILE_MENU));			
		weighingButton.setVisible(OJTConfiguration.getInstance().getPropertyAsBoolean(OJTConfiguration.WEIGHING_MENU));
		sortButton.setVisible(OJTConfiguration.getInstance().getPropertyAsBoolean(OJTConfiguration.SORT_MENU));
		weighingAndSortButton.setVisible(OJTConfiguration.getInstance().getPropertyAsBoolean(OJTConfiguration.WEIGHING_AND_SORT_MENU));
	}
}
