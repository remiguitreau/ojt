package com.ojt.ui;

import org.apache.log4j.Logger;

import com.ojt.OjtConstants;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;

public class SplashWindow extends JWindow {

    // -------------------------------------------------------------------------
    // Propriétés
    // -------------------------------------------------------------------------

    private JProgressBar progressBar = null;

    private int maxValue = 0;

    private final JTextArea textArea;

    private final JPanel panel;

    private final Color fontColor = Color.LIGHT_GRAY;

    private final Logger logger = Logger.getLogger(getClass());

    private final JLabel statusLabel;

    // -------------------------------------------------------------------------
    // Constructeur
    // -------------------------------------------------------------------------

    public SplashWindow(final Frame f, final int intProgressMaxValue) {
        super(f);

        // initialise la valeur a laquelle le splash screen doit etre fermé
        this.maxValue = intProgressMaxValue;

        // ajoute la progress bar
        progressBar = new JProgressBar(0, intProgressMaxValue);
        progressBar.setForeground(OjtUiConstants.OJT_GREEN);

        textArea = new JTextArea("Bienvenu dans le programme OJT\nVersion: " + OjtConstants.VERSION + "\n\n"
                + "Site internet: https://github.com/remiguitreau/ojt , powered by ");
        textArea.setBackground(fontColor);
        statusLabel = new JLabel("Initialisation");

        panel = new JPanel(new GridBagLayout());
        panel.setSize(200, 100);
        panel.setBackground(fontColor);
        panel.add(textArea, new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.NORTHEAST,
                GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
        final JLabel ojtIcon = new JLabel(OjtConstants.OJT_LOGO_67x52);
        panel.add(ojtIcon, new GridBagConstraints(1, 0, 1, 1, 0, 0, GridBagConstraints.NORTHEAST,
                GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
        final JLabel sourceforgeIcon = new JLabel(OjtConstants.GITHUB_LOGO);
        panel.add(sourceforgeIcon, new GridBagConstraints(0, 1, 2, 1, 1.0, 0, GridBagConstraints.CENTER,
                GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
        panel.add(statusLabel, new GridBagConstraints(0, 2, 2, 1, 0, 0, GridBagConstraints.NORTHEAST,
                GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

        panel.add(progressBar, new GridBagConstraints(0, 3, 3, 1, 0, 0, GridBagConstraints.NORTHEAST,
                GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
        // ajoute le label au panel
        getContentPane().add(panel);
        pack();
        // centre le splash screen
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        final Dimension labelSize = panel.getPreferredSize();
        setLocation(screenSize.width / 2 - labelSize.width / 2, screenSize.height / 2 - labelSize.height / 2);
        // affiche le splash screen
        setVisible(true);
    }

    // -------------------------------------------------------------------------
    // Accés aux propriétés de l'objet
    // -------------------------------------------------------------------------

    /**
     * @return le composant gérant l'affichage du statut d'avancement
     */
    public JLabel getStatusLabel() {
        return statusLabel;
    }

    // -------------------------------------------------------------------------
    // Méthodes public
    // -------------------------------------------------------------------------

    /**
     * Change la valeur de la barre de progression
     */
    public void setProgressValue(final int value) {
        progressBar.setValue(value);
        // si est arrivé a la valeur max : ferme le splash screen en lancant le
        // thread
        if (value >= maxValue) {
            try {
                SwingUtilities.invokeAndWait(closerRunner);
            } catch (final InterruptedException e) {
                logger.error("", e);
            } catch (final InvocationTargetException e) {
                logger.error("", e);
            }
        }
    }

    // thread pour fermer le splash screen
    final Runnable closerRunner = new Runnable() {
        @Override
        public void run() {
            setVisible(false);
            dispose();
        }
    };

    /**
     * Gére l'affichage du splashWindow
     */
    public void displaySplashWindow() {
        for (int i = 0; i <= 100; i++) {
            if (i == 20) {
                getStatusLabel().setText("Chargement de l'interface.");
            }
            if (i == 30) {
                getStatusLabel().setText("Chargement de l'interface..");
            }
            if (i == 40) {
                getStatusLabel().setText("Chargement de l'interface...");
            }

            if (i == 50) {
                getStatusLabel().setText("Tentative de connexion à la balance.");
            }
            if (i == 70) {
                getStatusLabel().setText("Tentative de connexion à la balance..");
            }
            if (i == 90) {
                getStatusLabel().setText("Tentative de connexion à la balance...");
            }

            setProgressValue(i);
            try {
                Thread.sleep(30);
            } catch (final InterruptedException ex) {
                logger.error("Une erreur est surevenue");
            }
        }
    }

    // -------------------------------------------------------------------------
    // Programme de test
    // -------------------------------------------------------------------------

    public static void main(final String[] args) {
        final SplashWindow splashWindow1 = new SplashWindow(new Frame(), 10);
        for (int i = 0; i < 100; i++) {
            splashWindow1.setProgressValue(i);
            try {
                Thread.sleep(500);
            } catch (final InterruptedException ex) {

            }
        }
    }

}
