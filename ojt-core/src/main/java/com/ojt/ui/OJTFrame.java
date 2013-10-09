/**
 * 
 */
package com.ojt.ui;

import org.apache.log4j.Logger;

import com.ojt.OjtConstants;
import com.ojt.balance.BalanceDriver;
import com.ojt.export.DefaultModelsFileExporter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

/**
 * Fenêtre d'OJT
 * @author Rémi "DwarfConan" Guitreau
 * @since 17 oct. 2009 : Création
 */
public class OJTFrame extends JFrame {

    // ---------------------------------------------------------
    // Attributs
    // ---------------------------------------------------------
    private final BalanceDriver balanceDriver;

    private final ConfigurationChangedListener configurationChangedListener;

    private final DefaultModelsFileExporter defaultModelsFileExporter;

    private final Logger logger = Logger.getLogger(getClass());

    private OJTMainPanel ojtMainPanel;

    // ---------------------------------------------------------
    // Constructeur
    // ---------------------------------------------------------

    public OJTFrame(final BalanceDriver balanceDriver,
            final ConfigurationChangedListener configurationChangedListener) {
        super();
        setTitle("Open Judo Tournament");
        this.balanceDriver = balanceDriver;
        this.configurationChangedListener = configurationChangedListener;
        defaultModelsFileExporter = new DefaultModelsFileExporter();
        initComponents();
    }

    // ---------------------------------------------------------
    // Accesseurs
    // ---------------------------------------------------------
    public BalanceDriver getBalanceDriver() {
        return balanceDriver;
    }

    // ---------------------------------------------------------
    // Privées
    // ---------------------------------------------------------

    private void initComponents() {
        setIconImage(OjtConstants.OJT_ICON.getImage());
        initMenu();
        initMainPanel();
    }

    private void initMainPanel() {
        ojtMainPanel = new OJTMainPanel(this);
        setContentPane(ojtMainPanel);
    }

    private void initMenu() {
        final JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(OjtUiConstants.OJT_GREEN);

        // --- Menu OJT ---
        final JMenu ojtMenu = new JMenu("Outils");

        // Configuration
        final JMenuItem configurationMenu = new JMenuItem("Configuration");
        configurationMenu.setBackground(OjtUiConstants.OJT_GREEN);
        configurationMenu.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                displayConfigurationPanel();
            }
        });
        ojtMenu.add(configurationMenu);

        // Export des modèles par défaut
        final JMenuItem defaultModelsExportMenu = new JMenuItem("Entregistrer les modèles par défaut...");
        defaultModelsExportMenu.setBackground(OjtUiConstants.OJT_GREEN);
        defaultModelsExportMenu.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                exportDefaultModels();
            }
        });
        ojtMenu.add(defaultModelsExportMenu);

        menuBar.add(ojtMenu);

        // --- Menu ? ---
        final JMenu othersMenu = new JMenu("?");
        // A propos
        final JMenuItem aboutMenu = new JMenuItem("A propos...");
        aboutMenu.setBackground(OjtUiConstants.OJT_GREEN);
        aboutMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                JOptionPane.showMessageDialog(OJTFrame.this, "Programme OJT\nVersion: "
                        + OjtConstants.VERSION + "\n\n"
                        + "Site internet: https://github.com/remiguitreau/ojt, powered by github.com", "Ojt "
                        + OjtConstants.VERSION, JOptionPane.INFORMATION_MESSAGE, OjtConstants.OJT_LOGO_67x52);
            }
        });
        othersMenu.add(aboutMenu);

        menuBar.add(othersMenu);
        setJMenuBar(menuBar);
    }

    private void displayConfigurationPanel() {
        final ConfigurationDialog configurationPanel = new ConfigurationDialog(this);
        configurationPanel.setSerialPorts(balanceDriver.getAvailablePorts());
        configurationPanel.addConfigurationListener(configurationChangedListener);
        configurationPanel.addConfigurationListener(ojtMainPanel);
        configurationPanel.init();
    }

    private void exportDefaultModels() {
        final JFileChooser fileChooser = new JFileChooser(OjtConstants.MODELS_DIRECTORY);
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
        fileChooser.showSaveDialog(this);
        File exportFile = fileChooser.getSelectedFile();
        if (exportFile != null) {
            if (!exportFile.getName().endsWith(".xls")) {
                exportFile = new File(exportFile.getAbsolutePath() + ".xls");
            }
            try {
                defaultModelsFileExporter.exportDefaultModelsFile(exportFile);
            } catch (final IOException ex) {
                logger.error("Error occured during default models file export.", ex);
                JOptionPane.showMessageDialog(this,
                        "Une erreur est survenue lors de la sauvegarde des modèles par défaut.", "OJT",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
