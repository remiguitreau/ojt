package com.ojt.ui;

import org.apache.log4j.Logger;
import org.jdesktop.swingx.JXDatePicker;

import com.ojt.CompetitionCategory;
import com.ojt.CompetitionDescriptor;
import com.ojt.CompetitorCategory;
import com.ojt.OJTCompetitionCategories;
import com.ojt.OJTConfiguration;
import com.ojt.OjtConstants;
import com.ojt.process.CompetitionInformationStep;
import com.ojt.tools.FileNameComposer;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;

public class ImportPanel extends JPanel {

	private static final Dimension BUTTON_SIZE = new Dimension(160, 21);

	// -------------------------------------------------------------------------
	// Constantes
	// -------------------------------------------------------------------------
	public final static String DATE_FORMAT = "dd.MM.yyyy";

	// -------------------------------------------------------------------------
	// Attributes
	// -------------------------------------------------------------------------

	private File competitionFile;

	private CompetitionDescriptor competitionDescriptor;

	private final CompetitionInformationStep competitionInformationStep;

	private JButton chooseButton, openOldFile;

	private JTextField fileField;

	private JTextField manifestation;

	private JTextField competitionLocation;

	private JComboBox cbCategory;

	private JTextField compsPerGroup;

	private JTextField fightTime;

	private JTextField txtWeighingPost;

	private JXDatePicker date;

	private final Logger logger = Logger.getLogger(getClass());

	private final DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);

	private List<String> serialPorts;

	private final List<ConfigurationChangedListener> configurationChangedListeners;

	private FocusListener focusListener;

	private ActionListener dateActionListener;

	private final boolean weighingPost;

	private final boolean sortPost;

	private final boolean sourceCreation;

	private JButton butModifyWeighingPost;

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	public ImportPanel(final CompetitionInformationStep competitionInformationStep,
			final boolean weighingPost, final boolean sortPost, final boolean sourceCreation) {
		super();

		this.competitionInformationStep = competitionInformationStep;
		this.weighingPost = weighingPost;
		this.sortPost = sortPost;
		this.sourceCreation = sourceCreation;

		serialPorts = new ArrayList<String>();
		configurationChangedListeners = new ArrayList<ConfigurationChangedListener>();
		initListeners();
		buildGui();
	}

	// -------------------------------------------------------------------------
	// Accesseurs
	// -------------------------------------------------------------------------

	public String getWeighingPost() {
		return txtWeighingPost.getText();
	}

	public CompetitionDescriptor getCompetitionDescriptor() {
		return competitionDescriptor;
	}

	public void setCompetitionDescriptor(final CompetitionDescriptor competitionDescriptor) {
		this.competitionDescriptor = competitionDescriptor;
		displayCompetitorDescriptor(competitionDescriptor);
	}

	public File getCompetitionFile() {
		return competitionFile;
	}

	public int getCompetitorPerGroup() {

		return new Integer(compsPerGroup.getText()).intValue();
	}

	public void setSerialPorts(final List<String> serialPorts) {
		this.serialPorts = serialPorts;
	}

	public List<String> getSerialPorts() {
		return serialPorts;
	}

	public List<ConfigurationChangedListener> getConfigurationChangedListeners() {
		return configurationChangedListeners;
	}

	// -------------------------------------------------------------------------
	// Méthodes privées
	// -------------------------------------------------------------------------
	private void initListeners() {
		focusListener = new FocusAdapter() {
			@Override
			public void focusGained(final FocusEvent e) {
				checkCompetitionInformation();
			}

			@Override
			public void focusLost(final FocusEvent evt) {
				if (evt.getComponent() == manifestation) {
					competitionDescriptor.setName(manifestation.getText());
					if ((manifestation.getText() == null) || manifestation.getText().trim().equals("")) {
						showErrorDialog("Vous devez indiquer un nom de manifestation");
					}
				} else if (evt.getComponent() == competitionLocation) {
					competitionDescriptor.setLocation(competitionLocation.getText());
					if ((competitionLocation.getText() == null)
							|| competitionLocation.getText().trim().equals("")) {
						showErrorDialog("Vous devez indiquer un lieu de manifestation");
					}
				} else if (evt.getComponent() == fightTime) {
					competitionDescriptor.setFightTime(fightTime.getText());
					if ((fightTime.getText() == null) || fightTime.getText().trim().equals("")) {
						showErrorDialog("Vous devez indiquer un temps de combat");
					}
				}
				checkCompetitionInformation();
			}

		};

		dateActionListener = new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				try {
					competitionDescriptor.setDate(dateFormat.format(date.getDate()));
				} catch (final Exception ex) {
					showErrorDialog("Vous devez une date pour la compétition");
				}
				checkCompetitionInformation();
			}
		};
	}

	private void showErrorDialog(final String message) {
		JOptionPane.showMessageDialog(null, message, "OJT", JOptionPane.ERROR_MESSAGE);
	}

	private void checkCompetitionInformation() {
		if ((competitionDescriptor.getCompetitionName() != null)
				&& !competitionDescriptor.getCompetitionName().trim().equals("")
				&& (competitionDescriptor.getLocation() != null)
				&& !competitionDescriptor.getLocation().trim().equals("")
				&& (competitionDescriptor.getDate() != null) && (this.competitionFile != null)) {
			competitionInformationStep.stepFinish(!weighingPost && sortPost);
		} else {
			competitionInformationStep.stepProcessing();
		}
	}

	private void initSortComponents(final boolean editable, final String openOldFileButtonLabel,
			final File openFolder) {
		initComponents(editable);

		openOldFile = new OjtButton(openOldFileButtonLabel);
		openOldFile.setPreferredSize(BUTTON_SIZE);
		openOldFile.addActionListener(createOldFileActionListener(openFolder));
	}

	private void initWeighingPostComponents(final boolean editable) {
		initComponents(editable);

		openOldFile = new OjtButton("Ouvrir");
		openOldFile.setPreferredSize(BUTTON_SIZE);
		openOldFile.addActionListener(createOldFileActionListener(OjtConstants.SOURCE_DIRECTORY));
	}

	private ActionListener createOldFileActionListener(final File folderName) {
		return new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				final JFileChooser fileChooser = new JFileChooser(folderName);
				fileChooser.setFileFilter(new FileFilter() {
					@Override
					public boolean accept(final File f) {
						return f.isDirectory() || f.getName().endsWith(".xls")
								|| f.getName().endsWith(".dat");
					}

					@Override
					public String getDescription() {
						return "Fichier source";
					}

				});

				final int returnVal = fileChooser.showOpenDialog(ImportPanel.this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					final File selectedFile = fileChooser.getSelectedFile();
					ImportPanel.this.fileField.setText(selectedFile.getName());
					ImportPanel.this.competitionFile = selectedFile;

					try {
						ImportPanel.this.displayCompetitorDescriptor(FileNameComposer.decomposeFileName(selectedFile.getName()));
					} catch (final Exception ex) {
						logger.error(ex);
						showErrorDialog("Mauvais format du nom de fichier : "
								+ selectedFile.getName()
								+ "\n\nFormat attendu : [<catégorie>-]<nom_manifestation>-<lieu>-<date>....[xls|dat]\n(Exemple de date : 15.10.2009");
					}
					checkCompetitionInformation();
					logger.info("You chose to open this file: " + selectedFile.getName());

				}
			}

		};

	}

	private void initComponents(final boolean editable) {
		manifestation = new JTextField();
		manifestation.addFocusListener(focusListener);
		manifestation.setEditable(editable);

		competitionLocation = new JTextField();
		competitionLocation.addFocusListener(focusListener);
		competitionLocation.setEditable(editable);

		cbCategory = new JComboBox();
		cbCategory.addItem(CompetitionCategory.UNKNOWN_CATEGORY.getHumanName());
		for (final CompetitionCategory competitionCategory : OJTCompetitionCategories.getCategories()) {
			cbCategory.addItem(competitionCategory.getHumanName());
		}
		cbCategory.setEditable(editable);
		cbCategory.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				competitionDescriptor.setCategory(OJTCompetitionCategories.retrieveCategoryFromHumanName((String) cbCategory.getSelectedItem()));
			}
		});

		date = new JXDatePicker();
		date.addActionListener(dateActionListener);
		date.getEditor().setEditable(false);
		date.setEditable(editable);

		fightTime = new JTextField();
		fightTime.setEditable(editable);

		compsPerGroup = new JTextField(String.valueOf(OjtConstants.MAX_COMPETITOR_PER_GROUP));
		compsPerGroup.setEditable(editable);

		txtWeighingPost = new JTextField(OJTConfiguration.getInstance().getProperty(
				OJTConfiguration.WEIGHING_POST));
		txtWeighingPost.setEditable(false);

		butModifyWeighingPost = new OjtButton("Modifier");
		butModifyWeighingPost.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent evt) {
				final Object newPost = JOptionPane.showInputDialog(ImportPanel.this,
						"Sélectionner votre numéro de poste de pesée :", "OJT", JOptionPane.QUESTION_MESSAGE,
						null, new String[] { "1", "2", "3", "4", "5" }, txtWeighingPost.getText());
				if (newPost != null) {
					OJTConfiguration.getInstance().updateProperty(OJTConfiguration.WEIGHING_POST,
							newPost.toString());
					txtWeighingPost.setText(newPost.toString());
					txtWeighingPost.updateUI();
					checkCompetitionInformation();
				}
			}
		});

		fileField = new JTextField("");
		fileField.setEditable(false);
		fileField.setPreferredSize(new Dimension(150, 5));
		fileField.setMinimumSize(fileField.getPreferredSize());

		chooseButton = new OjtButton("Ouvrir");
		chooseButton.setPreferredSize(BUTTON_SIZE);
		chooseButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				final JFileChooser fileChooser = new JFileChooser(OjtConstants.SOURCE_DIRECTORY);
				fileChooser.setFileFilter(new FileFilter() {
					@Override
					public boolean accept(final File f) {
						return f.isDirectory() || f.getName().endsWith(".xls")
								|| f.getName().endsWith(".dat");
					}

					@Override
					public String getDescription() {
						return "Fichier source";
					}

				});

				final int returnVal = fileChooser.showOpenDialog(ImportPanel.this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					ImportPanel.this.fileField.setText(fileChooser.getSelectedFile().getName());
					ImportPanel.this.competitionFile = fileChooser.getSelectedFile();
					checkCompetitionInformation();
					logger.info("You chose to open this file: " + fileChooser.getSelectedFile().getName());
				}
			}

		});
	}

	private void buildSortPostGui(final boolean withWeighingPost) {
		int y = 0;
		add(new JLabel("Manifestation : "), new GridBagConstraints(0, y, 1, 1, 0, 0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(3, 3, 3, 3), 0, 0));
		add(manifestation, new GridBagConstraints(1, y++, 1, 1, 0, 0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(3, 3, 3, 3), 0, 0));
		add(new JLabel("Lieu : "), new GridBagConstraints(0, y, 1, 1, 0, 0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(3, 3, 3, 3), 0, 0));
		add(competitionLocation, new GridBagConstraints(1, y++, 1, 1, 0, 0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(3, 3, 3, 3), 0, 0));

		add(new JLabel("Date : "), new GridBagConstraints(0, y, 1, 1, 0, 0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(3, 3, 3, 3), 0, 0));
		add(date, new GridBagConstraints(1, y++, 1, 1, 0, 0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(3, 3, 3, 3), 0, 0));

		add(new JLabel("Catégorie : "), new GridBagConstraints(0, y, 1, 1, 0, 0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(3, 3, 3, 3), 0, 0));
		add(cbCategory, new GridBagConstraints(1, y++, 1, 1, 0, 0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(3, 3, 3, 3), 0, 0));

		add(new JLabel("Temps de combat : "), new GridBagConstraints(0, y, 1, 1, 0, 0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(3, 3, 3, 3), 0, 0));
		fightTime.addFocusListener(focusListener);
		add(fightTime, new GridBagConstraints(1, y++, 1, 1, 0, 0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(3, 3, 3, 3), 0, 0));

		add(new JLabel("Nombre de compétiteur par groupe : "), new GridBagConstraints(0, y, 1, 1, 0, 0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(3, 3, 3, 3), 0, 0));
		add(compsPerGroup, new GridBagConstraints(1, y++, 1, 1, 0, 0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(3, 3, 3, 3), 0, 0));

		// add(new JLabel("Poste de pesée : "), new GridBagConstraints(0, y, 1,
		// 1, 0, 0,
		// GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(3, 3,
		// 3, 3), 0, 0));
		// add(txtWeighingPost, new GridBagConstraints(1, y, 1, 1, 0, 0,
		// GridBagConstraints.CENTER,
		// GridBagConstraints.BOTH, new Insets(3, 3, 3, 3), 0, 0));
		// add(butModifyWeighingPost, new GridBagConstraints(2, y++, 1, 1, 0, 0,
		// GridBagConstraints.CENTER,
		// GridBagConstraints.BOTH, new Insets(3, 3, 3, 3), 0, 0));
		add(new JLabel("Fichier source : "), new GridBagConstraints(0, y, 1, 1, 0, 0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(3, 3, 3, 3), 0, 0));
		add(fileField, new GridBagConstraints(1, y, 1, 1, 1, 0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(3, 3, 3, 3), 0, 0));

		if (withWeighingPost) {
			add(chooseButton, new GridBagConstraints(2, y++, 1, 1, 0, 0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(3, 3, 3, 3), 0, 0));
		}

		add(openOldFile, new GridBagConstraints(2, y++, 1, 1, 0, 0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(3, 3, 3, 3), 0, 0));

		y = addModelFileGui(y);

		add(new JLabel(""), new GridBagConstraints(0, y++, 3, 1, 1, 1, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(3, 3, 3, 3), 0, 0));
	}

	private void buildWeighingPostGui() {
		int y = 0;

		add(new JLabel("Poste de pesée : "), new GridBagConstraints(0, y, 1, 1, 0, 0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(3, 3, 3, 3), 0, 0));
		add(txtWeighingPost, new GridBagConstraints(1, y, 1, 1, 0, 0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(3, 3, 3, 3), 0, 0));
		add(butModifyWeighingPost, new GridBagConstraints(2, y++, 1, 1, 0, 0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(3, 3, 3, 3), 0, 0));

		add(new JLabel("Fichier source : "), new GridBagConstraints(0, y, 1, 1, 0, 0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(3, 3, 3, 3), 0, 0));
		add(fileField, new GridBagConstraints(1, y, 1, 1, 1, 0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(3, 3, 3, 3), 0, 0));

		add(openOldFile, new GridBagConstraints(2, y++, 1, 1, 0, 0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(3, 3, 3, 3), 0, 0));

		add(new JLabel("Manifestation : "), new GridBagConstraints(0, y, 1, 1, 0, 0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(3, 3, 3, 3), 0, 0));
		add(manifestation, new GridBagConstraints(1, y++, 1, 1, 0, 0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(3, 3, 3, 3), 0, 0));

		add(new JLabel("Lieu : "), new GridBagConstraints(0, y, 1, 1, 0, 0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(3, 3, 3, 3), 0, 0));
		add(competitionLocation, new GridBagConstraints(1, y++, 1, 1, 0, 0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(3, 3, 3, 3), 0, 0));

		add(new JLabel("Date : "), new GridBagConstraints(0, y, 1, 1, 0, 0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(3, 3, 3, 3), 0, 0));
		add(date, new GridBagConstraints(1, y++, 1, 1, 0, 0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(3, 3, 3, 3), 0, 0));

		add(new JLabel("Catégorie : "), new GridBagConstraints(0, y, 1, 1, 0, 0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(3, 3, 3, 3), 0, 0));
		add(cbCategory, new GridBagConstraints(1, y++, 1, 1, 0, 0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(3, 3, 3, 3), 0, 0));

		add(new JLabel(""), new GridBagConstraints(0, y++, 3, 1, 1, 1, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(3, 3, 3, 3), 0, 0));
	}

	private void buildSourceCreationGui() {
		int y = 0;

		add(new JLabel("Manifestation : "), new GridBagConstraints(0, y, 1, 1, 0, 0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(3, 3, 3, 3), 0, 0));
		add(manifestation, new GridBagConstraints(1, y++, 1, 1, 0, 0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(3, 3, 3, 3), 0, 0));

		add(new JLabel("Lieu : "), new GridBagConstraints(0, y, 1, 1, 0, 0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(3, 3, 3, 3), 0, 0));
		add(competitionLocation, new GridBagConstraints(1, y++, 1, 1, 0, 0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(3, 3, 3, 3), 0, 0));

		add(new JLabel("Date : "), new GridBagConstraints(0, y, 1, 1, 0, 0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(3, 3, 3, 3), 0, 0));
		add(date, new GridBagConstraints(1, y++, 1, 1, 0, 0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(3, 3, 3, 3), 0, 0));

		add(new JLabel("Catégorie : "), new GridBagConstraints(0, y, 1, 1, 0, 0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(3, 3, 3, 3), 0, 0));
		add(cbCategory, new GridBagConstraints(1, y++, 1, 1, 0, 0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(3, 3, 3, 3), 0, 0));

		add(new JLabel("Fichier des compétiteurs : "), new GridBagConstraints(0, y, 1, 1, 0, 0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(3, 3, 3, 3), 0, 0));
		add(fileField, new GridBagConstraints(1, y, 1, 1, 1, 0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(3, 3, 3, 3), 0, 0));

		add(chooseButton, new GridBagConstraints(2, y++, 1, 1, 0, 0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(3, 3, 3, 3), 0, 0));

		add(new JLabel(""), new GridBagConstraints(0, y++, 3, 1, 1, 1, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(3, 3, 3, 3), 0, 0));
	}

	private void buildGui() {
		final boolean editable = sortPost || sourceCreation;

		setLayout(new GridBagLayout());

		if (sortPost) {
			if (weighingPost) {
				initSortComponents(editable, "Ouvrir une sauvegarde", OjtConstants.PERSISTANCY_DIRECTORY);
			} else {
				initSortComponents(editable, "Ouvrir une pesée", OjtConstants.WEIGHING_DIRECTORY);
			}
			// poste de trie uniquement
			buildSortPostGui(weighingPost);
		} else if (weighingPost) {
			// poste de pesée seul
			initWeighingPostComponents(editable);
			buildWeighingPostGui();
		} else if (sourceCreation) {
			// création d'un fichier source.
			initComponents(editable);
			buildSourceCreationGui();
		}

	}

	private int addModelFileGui(final int y) {
		// if
		// (OJTConfiguration.getInstance().getProperty(OJTConfiguration.EXPORT_FROM_MODELS).equals("true"))
		// {
		// add(new JLabel("Fichier modèle pour l'export : "), new
		// GridBagConstraints(0, y, 1, 1, 0, 0,
		// GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(3, 3,
		// 3, 3), 0, 0));
		// final String fileModelPath =
		// OJTConfiguration.getInstance().getProperty(
		// OJTConfiguration.MODEL_FILE_PATH);
		// modelsFileField = new JTextField(fileModelPath);
		// modelsFileField.setEditable(false);
		// modelsFileField.setPreferredSize(new Dimension(150, 5));
		// modelsFileField.setMinimumSize(fileFild.getPreferredSize());
		// add(modelsFileField, new GridBagConstraints(1, y, 1, 1, 1, 0,
		// GridBagConstraints.CENTER,
		// GridBagConstraints.BOTH, new Insets(3, 3, 3, 3), 0, 0));
		//
		// modelsChooseButton = new JButton("Ouvrir");
		// modelsChooseButton.addActionListener(new ActionListener() {
		// public void actionPerformed(final ActionEvent e) {
		// final File parentFile = fileModelPath.equals("") ? new File(".." +
		// File.separator
		// + "models") : new File(new File(fileModelPath).getParent());
		// final JFileChooser fileChooser = new JFileChooser(parentFile);
		// fileChooser.setFileFilter(new FileFilter() {
		// @Override
		// public boolean accept(final File f) {
		// return f.isDirectory() || (f.getAbsolutePath().indexOf(".xls") > 0);
		// }
		//
		// @Override
		// public String getDescription() {
		// return "xls selection";
		// }
		//
		// });
		//
		// final int returnVal = fileChooser.showOpenDialog(ImportPanel.this);
		// if (returnVal == JFileChooser.APPROVE_OPTION) {
		// ImportPanel.this.modelsFileField.setText(fileChooser.getSelectedFile().getName());
		// OJTConfiguration.getInstance().updateProperty(OJTConfiguration.MODEL_FILE_PATH,
		// fileChooser.getSelectedFile().getAbsolutePath());
		// ImportPanel.this.modelsFile = fileChooser.getSelectedFile();
		// logger.info("You chose to use this models file: "
		// + fileChooser.getSelectedFile().getName());
		// }
		// }
		//
		// });
		// add(modelsChooseButton, new GridBagConstraints(2, y++, 1, 1, 0, 0,
		// GridBagConstraints.CENTER,
		// GridBagConstraints.BOTH, new Insets(3, 3, 3, 3), 0, 0));
		//
		// }
		return y;
	}

	private void notifyListeners() {
		for (final ConfigurationChangedListener configurationChangedListener : getConfigurationChangedListeners()) {
			configurationChangedListener.configurationUpdated();
		}
	}

	// -------------------------------------------------------------------------
	// Méthodes protégées
	// -------------------------------------------------------------------------

	void displayCompetitorDescriptor(final CompetitionDescriptor competDatas) {
		this.competitionDescriptor = competDatas;
		manifestation.setText(competDatas.getCompetitionName());
		competitionLocation.setText(competDatas.getLocation());
		try {
			date.setDate(dateFormat.parse(competDatas.getDate()));
		} catch (final ParseException ex) {
			date.setDate(new Date());
			competitionDescriptor.setDate(dateFormat.format(new Date()));
			logger.warn("Impossible d'afficher la date '" + competDatas.getDate()
					+ "', on réinitialise le date picker.", ex);
		}
		fightTime.setText(competDatas.getFightTime());
		cbCategory.setSelectedItem(competDatas.getCategory() == null ? CompetitorCategory.UNKNOWN.getHumanName()
				: competDatas.getCategory().getHumanName());
		checkCompetitionInformation();
	}

	// -------------------------------------------------------------------------
	// Méthodes publiques
	// -------------------------------------------------------------------------

	public void addConfigurationListener(final ConfigurationChangedListener configurationChangedListener) {
		getConfigurationChangedListeners().add(configurationChangedListener);
	}

	public void removeConfigurationListener(final ConfigurationChangedListener configurationChangedListener) {
		getConfigurationChangedListeners().remove(configurationChangedListener);
	}

}
