package com.ojt.ui;

import org.apache.log4j.Logger;

import com.ojt.OJTConfiguration;
import com.ojt.OJTLauncher;
import com.ojt.OjtConstants;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;

/**
 * Panneau de configuration d'OJT
 * @author CDa
 * @since 13 oct. 2009 (CDa) : Création
 */
public class ConfigurationDialog extends JDialog {

    // -------------------------------------------------------------------------
    // Constantes
    // -------------------------------------------------------------------------

    private static final Dimension BUTTON_SIZE = new Dimension(80, 20);

    private static final String NO_SERIAL_PORT_AVAILABLE = "Aucun port disponible";

    // -------------------------------------------------------------------------
    // Propriétés de l'objet
    // -------------------------------------------------------------------------

    private JComboBox serialPortComboBox;

    private JButton okButton, cancelButton;

    private JRadioButton noRadioButton;

    private JRadioButton defaultModelRadioButton;

    private JRadioButton myModelRadioButton;

    private List<String> serialPorts;

    private List<ConfigurationChangedListener> configurationChangedListeners;

    private JButton modelsChooseButton;

    private JTextField modelsFileField;

    private JCheckBox weighingSaveDirCheckBox;

    private JButton weighingSaveDirChooseButton;

    private JTextField weighingSaveDirFileField;

    private final Logger logger = Logger.getLogger(OJTLauncher.class);

    private JCheckBox chkBoxGenerateSourceFile;

    private JCheckBox chkBoxWeighing;

    private JCheckBox chkBoxSort;

    private JCheckBox chkBoxWeighingAndSort;

    private JCheckBox chkBoxAutoRegistration;

    // -------------------------------------------------------------------------
    // Constructeur
    // -------------------------------------------------------------------------

    /**
     * Constructeur par défaut
     */
    public ConfigurationDialog(final JFrame frame) {
        super(frame, true);
        setTitle("Configuration de OJT");
        setLayout(new GridBagLayout());
        serialPorts = new ArrayList<String>();
        configurationChangedListeners = new ArrayList<ConfigurationChangedListener>();
    }

    // -------------------------------------------------------------------------
    // Accès aux propriétés de l'objet
    // -------------------------------------------------------------------------

    public void setSerialPorts(final List<String> serialPorts) {
        this.serialPorts = serialPorts;
    }

    public List<String> getSerialPorts() {
        return serialPorts;
    }

    public List<ConfigurationChangedListener> getConfigurationChangedListeners() {
        return configurationChangedListeners;
    }

    public void setConfigurationChangedListeners(
            final List<ConfigurationChangedListener> configurationChangedListeners) {
        this.configurationChangedListeners = configurationChangedListeners;
    }

    // -------------------------------------------------------------------------
    // Méthodes publiques
    // -------------------------------------------------------------------------

    public void init() {
        buildGui();
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        final Dimension labelSize = getPreferredSize();
        setLocation(screenSize.width / 2 - labelSize.width / 2, screenSize.height / 2 - labelSize.height / 2);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setResizable(true);
        pack();
        setVisible(true);

    }

    public void addConfigurationListener(final ConfigurationChangedListener configurationChangedListener0) {
        getConfigurationChangedListeners().add(configurationChangedListener0);
    }

    public void removeConfigurationListener(final ConfigurationChangedListener configurationChangedListener0) {
        getConfigurationChangedListeners().remove(configurationChangedListener0);
    }

    // -------------------------------------------------------------------------
    // Méthodes privées
    // -------------------------------------------------------------------------

    private void buildGui() {
        buildModelFileGui();
        buildWeighingSaveDirGui();
        serialPortComboBox = new JComboBox();
        if (getSerialPorts().size() > 0) {
            for (final String port : getSerialPorts()) {
                serialPortComboBox.addItem(port);
            }
        } else {
            serialPortComboBox.addItem(NO_SERIAL_PORT_AVAILABLE);
            serialPortComboBox.setEnabled(false);
        }
        serialPortComboBox.setSelectedItem(OJTConfiguration.getInstance().getProperty(
                OJTConfiguration.SERIAL_PORT));

        int y = 0;
        add(new JLabel("Port série"), new GridBagConstraints(0, y, 1, 1, 0, 0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.BOTH, new Insets(10, 3, 0, 5), 0, 0));
        add(serialPortComboBox, new GridBagConstraints(1, y++, 1, 1, 0, 0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.NONE, new Insets(10, 5, 0, 0), 0, 0));

        add(new JLabel("Utilsation des modèles"), new GridBagConstraints(0, y, 1, 1, 0, 0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(10, 3, 0, 0), 0, 0));
        add(buildModelRadioButtonPanel(), new GridBagConstraints(1, y++, 1, 1, 0, 0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(10, 0, 0, 0), 0, 0));

        y = addModelFileGui(y);
        y = addWeighingSaveFileGui(y);

        add(new JSeparator(), new GridBagConstraints(0, y++, 3, 1, 0, 0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
        y = addAvailableMenusGui(y);

        add(new JLabel(""), new GridBagConstraints(0, y++, 3, 1, 0, 1.0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        add(buildButtonPanel(), new GridBagConstraints(0, y++, 3, 1, 0, 0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    }

    private JPanel buildModelRadioButtonPanel() {
        final JPanel modelRadioButtonpanel = new JPanel();
        final ButtonGroup exportWithModelMenuItem = new ButtonGroup();
        noRadioButton = new JRadioButton("Non");
        // yesRadioButton = new JRadioButton("Oui");
        defaultModelRadioButton = new JRadioButton("Oui, le modèle par defaut");
        myModelRadioButton = new JRadioButton("Oui, le modèle personnalisé");
        exportWithModelMenuItem.add(noRadioButton);
        exportWithModelMenuItem.add(defaultModelRadioButton);
        exportWithModelMenuItem.add(myModelRadioButton);
        // exportWithModelMenuItem.add(yesRadioButton);
        final boolean exportWithModel = OJTConfiguration.getInstance().getPropertyAsBoolean(
                OJTConfiguration.EXPORT_FROM_MODELS);
        final String modelFile = OJTConfiguration.getInstance().getProperty(OJTConfiguration.MODEL_FILE_PATH);
        modelRadioButtonpanel.add(noRadioButton);
        // modelRadioButtonpanel.add(yesRadioButton);
        // yesRadioButton.setSelected(true);
        modelRadioButtonpanel.add(defaultModelRadioButton);
        modelRadioButtonpanel.add(myModelRadioButton);
        if (exportWithModel) {
            if (modelFile != null && modelFile.trim().length() > 0) {
                modelsChooseButton.setEnabled(true);
                myModelRadioButton.setSelected(true);
            } else {
                modelsChooseButton.setEnabled(false);
                defaultModelRadioButton.setSelected(true);
            }
        } else {
            noRadioButton.setSelected(true);
            modelsChooseButton.setEnabled(false);
        }
        noRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                modelsChooseButton.setEnabled(false);
                updateFileName("");
            }

        });
        // yesRadioButton.addActionListener(new ActionListener() {
        // @Override
        // public void actionPerformed(final ActionEvent e) {
        // modelsChooseButton.setEnabled(true);
        // }
        //
        // });
        defaultModelRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                modelsChooseButton.setEnabled(false);
                updateFileName("");
            }

        });
        myModelRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                modelsChooseButton.setEnabled(true);
            }

        });
        return modelRadioButtonpanel;
    }

    private JPanel buildButtonPanel() {
        final JPanel buttonPanel = new JPanel();
        okButton = new OjtButton("Valider");
        okButton.setPreferredSize(BUTTON_SIZE);
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                saveConfigurationAndExit();
            }
        });
        cancelButton = new OjtButton("Annuler");
        cancelButton.setPreferredSize(BUTTON_SIZE);
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                exit();
            }
        });
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        return buttonPanel;
    }

    private void buildModelFileGui() {
        final String fileModelPath = OJTConfiguration.getInstance().getProperty(
                OJTConfiguration.MODEL_FILE_PATH);
        modelsFileField = new JTextField(fileModelPath);
        modelsFileField.setToolTipText(fileModelPath);
        modelsFileField.setEditable(false);
        modelsFileField.setPreferredSize(new Dimension(150, 5));
        modelsFileField.setMinimumSize(new Dimension(150, 5));

        modelsChooseButton = new OjtButton("Ouvrir");
        modelsChooseButton.setPreferredSize(BUTTON_SIZE);
        modelsChooseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                File parentFile = null;
                try {
                    parentFile = fileModelPath.equals("") ? OjtConstants.MODELS_DIRECTORY : new File(
                            new File(fileModelPath).getParent());
                } catch (final Exception ex) {
                    logger.error("Bad path to models file.", ex);
                }
                final JFileChooser fileChooser = parentFile == null ? new JFileChooser() : new JFileChooser(
                        parentFile);
                fileChooser.setFileFilter(new FileFilter() {
                    @Override
                    public boolean accept(final File f) {
                        return f.isDirectory() || f.getAbsolutePath().indexOf(".xls") > 0;
                    }

                    @Override
                    public String getDescription() {
                        return "xls selection";
                    }

                });

                final int returnVal = fileChooser.showOpenDialog(ConfigurationDialog.this);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    updateFileName(fileChooser.getSelectedFile().getAbsolutePath());
                }
            }

        });
    }

    private void updateFileName(final String fileName) {
        modelsFileField.setText(fileName);
        modelsFileField.setToolTipText(fileName);
    }

    private void buildWeighingSaveDirGui() {
        final String weighingSaveDirPath = OJTConfiguration.getInstance().getProperty(
                OJTConfiguration.ADD_WEIGHING_SAVE_DIRECTORY_PATH);
        weighingSaveDirCheckBox = new JCheckBox(
                "Utiliser un répertoire supplémentaire de sauvegarde des pesées : ");
        weighingSaveDirCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent evt) {
                weighingSaveDirChooseButton.setEnabled(weighingSaveDirCheckBox.isSelected());
                if (!weighingSaveDirCheckBox.isSelected()) {
                    weighingSaveDirFileField.setText("");
                    weighingSaveDirFileField.setToolTipText("");
                }
            }
        });
        weighingSaveDirCheckBox.setSelected(weighingSaveDirPath != null
                && !weighingSaveDirPath.trim().isEmpty());

        weighingSaveDirFileField = new JTextField(weighingSaveDirPath);
        weighingSaveDirFileField.setToolTipText(weighingSaveDirPath);
        weighingSaveDirFileField.setEditable(false);
        weighingSaveDirFileField.setPreferredSize(new Dimension(150, 5));
        weighingSaveDirFileField.setMinimumSize(new Dimension(150, 5));

        weighingSaveDirChooseButton = new OjtButton("Ouvrir");
        weighingSaveDirChooseButton.setPreferredSize(BUTTON_SIZE);
        weighingSaveDirChooseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                final JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                final int returnVal = fileChooser.showOpenDialog(ConfigurationDialog.this);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    weighingSaveDirFileField.setText(fileChooser.getSelectedFile().getAbsolutePath());
                    weighingSaveDirFileField.setToolTipText(fileChooser.getSelectedFile().getAbsolutePath());
                }
            }
        });
        weighingSaveDirChooseButton.setEnabled(weighingSaveDirCheckBox.isSelected());
    }

    private int addModelFileGui(int y) {
        add(new JLabel("Fichier modèle pour l'export : "), new GridBagConstraints(0, y, 1, 1, 0, 0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(3, 3, 3, 3), 0, 0));
        add(modelsFileField, new GridBagConstraints(1, y, 1, 1, 1, 0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(3, 3, 3, 3), 0, 0));

        add(modelsChooseButton, new GridBagConstraints(2, y++, 1, 1, 0, 0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(3, 3, 3, 3), 0, 0));

        return y;
    }

    private int addAvailableMenusGui(int y) {
        add(new JLabel("Menus disponibles : "), new GridBagConstraints(0, y++, 1, 1, 0, 0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(3, 3, 3, 3), 0, 0));
        chkBoxGenerateSourceFile = new JCheckBox("Générer un fichier source");
        add(chkBoxGenerateSourceFile, new GridBagConstraints(0, y++, 1, 1, 0, 0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        chkBoxGenerateSourceFile.setSelected(OJTConfiguration.getInstance().getPropertyAsBoolean(
                OJTConfiguration.GENERATE_SOURCE_FILE_MENU));

        chkBoxWeighing = new JCheckBox("Pesée");
        add(chkBoxWeighing, new GridBagConstraints(0, y++, 1, 1, 0, 0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        chkBoxWeighing.setSelected(OJTConfiguration.getInstance().getPropertyAsBoolean(
                OJTConfiguration.WEIGHING_MENU));

        chkBoxSort = new JCheckBox("Tri");
        add(chkBoxSort, new GridBagConstraints(0, y++, 1, 1, 0, 0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        chkBoxSort.setSelected(OJTConfiguration.getInstance().getPropertyAsBoolean(OJTConfiguration.SORT_MENU));

        chkBoxWeighingAndSort = new JCheckBox("Pesée + tri");
        add(chkBoxWeighingAndSort, new GridBagConstraints(0, y++, 1, 1, 0, 0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        chkBoxWeighingAndSort.setSelected(OJTConfiguration.getInstance().getPropertyAsBoolean(
                OJTConfiguration.WEIGHING_AND_SORT_MENU));

        chkBoxAutoRegistration = new JCheckBox("Enregistrement automatique");
        add(chkBoxAutoRegistration, new GridBagConstraints(0, y++, 1, 1, 0, 0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        chkBoxAutoRegistration.setSelected(OJTConfiguration.getInstance().getPropertyAsBoolean(
                OJTConfiguration.AUTO_REGISTRATION_MENU));

        return y;
    }

    private int addWeighingSaveFileGui(int y) {
        add(weighingSaveDirCheckBox, new GridBagConstraints(0, y, 1, 1, 0, 0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(3, 3, 3, 3), 0, 0));
        add(weighingSaveDirFileField, new GridBagConstraints(1, y, 1, 1, 1, 0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(3, 3, 3, 3), 0, 0));

        add(weighingSaveDirChooseButton, new GridBagConstraints(2, y++, 1, 1, 0, 0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(3, 3, 3, 3), 0, 0));

        return y;
    }

    private void exit() {
        dispose();
    }

    private void saveConfigurationAndExit() {
        OJTConfiguration.getInstance().updateProperty(OJTConfiguration.ADD_WEIGHING_SAVE_DIRECTORY_PATH,
                weighingSaveDirFileField.getText());
        OJTConfiguration.getInstance().updateProperty(OJTConfiguration.MODEL_FILE_PATH,
                modelsFileField.getText());
        OJTConfiguration.getInstance().updateProperty(OJTConfiguration.EXPORT_FROM_MODELS,
                noRadioButton.isSelected() ? Boolean.FALSE.toString() : Boolean.TRUE.toString());

        OJTConfiguration.getInstance().updateProperty(OJTConfiguration.GENERATE_SOURCE_FILE_MENU,
                chkBoxGenerateSourceFile.isSelected() ? Boolean.TRUE.toString() : Boolean.FALSE.toString());
        OJTConfiguration.getInstance().updateProperty(OJTConfiguration.WEIGHING_MENU,
                chkBoxWeighing.isSelected() ? Boolean.TRUE.toString() : Boolean.FALSE.toString());
        OJTConfiguration.getInstance().updateProperty(OJTConfiguration.SORT_MENU,
                chkBoxSort.isSelected() ? Boolean.TRUE.toString() : Boolean.FALSE.toString());
        OJTConfiguration.getInstance().updateProperty(OJTConfiguration.WEIGHING_AND_SORT_MENU,
                chkBoxWeighingAndSort.isSelected() ? Boolean.TRUE.toString() : Boolean.FALSE.toString());
        OJTConfiguration.getInstance().updateProperty(OJTConfiguration.AUTO_REGISTRATION_MENU,
                chkBoxAutoRegistration.isSelected() ? Boolean.TRUE.toString() : Boolean.FALSE.toString());
        fireAvailableMenusChanged();
        dispose();
        if (((String) serialPortComboBox.getSelectedItem()).equals(OJTConfiguration.getInstance().getProperty(
                OJTConfiguration.SERIAL_PORT))) {
            return;
        }

        OJTConfiguration.getInstance().updateProperty(OJTConfiguration.SERIAL_PORT,
                (String) serialPortComboBox.getSelectedItem());
        if (((String) serialPortComboBox.getSelectedItem()).equals(NO_SERIAL_PORT_AVAILABLE)) {
            return;
        }
        fireConfigurationUpdated();
    }

    private void fireAvailableMenusChanged() {
        for (final ConfigurationChangedListener configurationChangedListener : getConfigurationChangedListeners()) {
            configurationChangedListener.availableMenusChanged();
        }
    }

    private void fireConfigurationUpdated() {
        for (final ConfigurationChangedListener configurationChangedListener : getConfigurationChangedListeners()) {
            configurationChangedListener.configurationUpdated();
        }
    }

    // -------------------------------------------------------------------------
    // Méthode de test
    // -------------------------------------------------------------------------

    public static void main(final String[] args) {
        final ConfigurationDialog configurationPanel = new ConfigurationDialog(null);
        configurationPanel.init();
    }

}
