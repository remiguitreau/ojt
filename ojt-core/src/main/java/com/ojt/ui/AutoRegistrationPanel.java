/**
 * 
 */
package com.ojt.ui;

import org.apache.log4j.Logger;

import com.ojt.CompetitionDescriptor;
import com.ojt.Competitor;
import com.ojt.CompetitorList;
import com.ojt.OjtConstants;
import com.ojt.dao.CompetitorsDao;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

/**
 * Fenetre permettant de sélectionner un compétiteur et de modifier son poids
 * @author CDa
 * @since 18 janv. 2009 (CDa) : Création
 * @since 09 aout 2009 (CDa) : possibilité d'ajouter un compétiteur à la volée
 */
public final class AutoRegistrationPanel extends JPanel {

    // -------------------------------------------------------------------------
    // Constantes
    // -------------------------------------------------------------------------

    private final static String ADD_COMPETITOR_BUTTON = "Nouveau compétiteur";

    private final static String IMPORT_COMPETITOR_LIST_BUTTON = "Importer une pesée";

    // -------------------------------------------------------------------------
    // Propriétés de l'objet
    // -------------------------------------------------------------------------

    private CompetitorList registeredCompetitorsList = new CompetitorList();

    private CompetitorList competitorsList = new CompetitorList();

    private CompetitorsDao competitorsDao = null;

    private final Logger logger = Logger.getLogger(getClass());

    private CompetitionDescriptor competitionDescriptor;

    private JTable competitorTable;

    private JLabel licensePendingLabel;

    private JLabel competitorsRegisteredNumberLabel;

    private StringBuilder buf = new StringBuilder();

    public AutoRegistrationPanel() {
        buildGui();
    }

    // -------------------------------------------------------------------------
    // Accesseurs
    // -------------------------------------------------------------------------

    private void buildGui() {
        licensePendingLabel = new JLabel("Licence en cours de saisie :");
        licensePendingLabel.setPreferredSize(new Dimension(800, 50));
        licensePendingLabel.setSize(new Dimension(800, 50));
        add(licensePendingLabel);

        competitorTable = new JTable();
        competitorTable.setPreferredScrollableViewportSize(new Dimension(800, 300));
        competitorTable.setFillsViewportHeight(true);
        final JScrollPane scrollPane = new JScrollPane(competitorTable);
        scrollPane.setPreferredSize(new Dimension(700, 250));
        scrollPane.setSize(new Dimension(700, 250));
        add(scrollPane);

        competitorsRegisteredNumberLabel = new JLabel("Aucun compétiteur enregistré");
        competitorsRegisteredNumberLabel.setPreferredSize(new Dimension(800, 50));
        competitorsRegisteredNumberLabel.setSize(new Dimension(800, 50));
        add(competitorsRegisteredNumberLabel);

        addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(final KeyEvent keyEvent) {
                switch (keyEvent.getKeyCode()) {
                    case KeyEvent.VK_ENTER:
                        processRegistration(buf.toString());
                        buf = new StringBuilder();
                        licensePendingLabel.setText("Licence en cours de saisie :");
                        break;
                    case KeyEvent.VK_BACK_SPACE:
                        if (buf.length() > 0) {
                            buf = new StringBuilder(buf.substring(0, buf.length() - 1));
                            licensePendingLabel.setText("Licence en cours de saisie : " + buf.toString());
                        }
                        break;
                    default:
                        if (Character.isLetterOrDigit(keyEvent.getKeyChar()) || keyEvent.getKeyChar() == '*'
                                || keyEvent.getKeyChar() == '&' || keyEvent.getKeyChar() == '-'
                                || keyEvent.getKeyChar() == '_') {
                            buf.append(keyEvent.getKeyChar());
                            licensePendingLabel.setText("Licence en cours de saisie : " + buf.toString());
                        }
                        break;
                }
            }
        });
        updateCompetitorsTable();
    }

    private void updateCompetitorsTable() {
        competitorTable.setModel(new CompetitorTableModel(competitionDescriptor, registeredCompetitorsList,
                competitorsDao, new CompetitorWeightListener() {
                    @Override
                    public void newWeightEntered() {

                    }
                }));
        competitorTable.setDefaultRenderer(Object.class, new CompetitorTableRenderer());
        competitorTable.getColumnModel().getColumn(CompetitorTableModel.WEIGHT_INDEX).setPreferredWidth(60);
        competitorTable.getColumnModel().getColumn(CompetitorTableModel.GRADE_INDEX).setPreferredWidth(45);
        updateRegisteredCompetitorsNumberLabel();
    }

    private void updateRegisteredCompetitorsNumberLabel() {
        if (registeredCompetitorsList == null || registeredCompetitorsList.isEmpty()) {
            competitorsRegisteredNumberLabel.setText("Aucun compétiteur enregistré");
        } else if (registeredCompetitorsList.size() == 1) {
            competitorsRegisteredNumberLabel.setText("1 compétiteur enregistré");
        } else {
            competitorsRegisteredNumberLabel.setText(registeredCompetitorsList.size()
                    + " compétiteurs enregistrés");
        }
    }

    private void processRegistration(final String competitorId) {
        final Competitor competitor = retrieveCompetitorByLicenseCode(competitorId);
        if (competitor == null) {
            final AudioClip ac = Applet.newAudioClip(OjtConstants.class.getResource("unknown_license.wav"));
            ac.play();
            return;
        } else if (competitor.getWeight() == null) {
            final AudioClip ac = Applet.newAudioClip(OjtConstants.class.getResource("not_registered_weight.wav"));
            ac.play();
            return;
        }
        if (registeredCompetitorsList.contains(competitor)) {
            logger.info("Competitor already registered: " + competitor);
        } else {
            registeredCompetitorsList.add(competitor);
            Toolkit.getDefaultToolkit().beep();
            try {
                Thread.sleep(100);
            } catch (final Exception ex) {
                // NOP
            }
            Toolkit.getDefaultToolkit().beep();
        }
        updateCompetitorsTable();
    }

    private Competitor retrieveCompetitorByLicenseCode(final String competitorId) {
        for (final Competitor competitor : competitorsList) {
            if (cleanString(competitor.getLicenseCode()).equals(cleanString(competitorId))) {
                return competitor;
            }
        }
        return null;
    }

    private String cleanString(final String name) {
        String temp = name.toLowerCase();
        temp = temp.replace('ý', 'y');
        temp = temp.replaceAll("[ùúûü]", "u");
        temp = temp.replaceAll("òóôõö]", "o");
        temp = temp.replaceAll("[ìíîï]", "i");
        temp = temp.replaceAll("[èéêë]", "e");
        temp = temp.replace('ç', 'c');
        temp = temp.replaceAll("[àáâãäåæ]", "a");
        return temp;
    }

    public void setCompetitionDescriptor(final CompetitionDescriptor competitionDescriptor) {
        this.competitionDescriptor = competitionDescriptor;
    }

    /**
     * Set la liste des compétiteurs
     * @param competitorList
     */
    public void setRegisteredCompetitorList(final CompetitorList competitorList) {
        this.registeredCompetitorsList = competitorList;
    }

    public void setCompetitorsList(final CompetitorList competitorsList) {
        this.competitorsList = competitorsList;
    }

    /**
     * Get sur la liste des compétiteurs
     * @return competitorList
     */
    public CompetitorList getRegisteredCompetitorList() {
        return registeredCompetitorsList;
    }

    // -------------------------------------------------------------------------
    // Méthodes publiques
    // -------------------------------------------------------------------------

    /**
     * @return La {@link CompetitorList} des {@link Competitor} valides (poids
     *         saisi).
     */
    public CompetitorList getValidCompetitors() {
        final CompetitorList validCompetitors = new CompetitorList();
        for (final Competitor competitor : registeredCompetitorsList) {
            if (competitor.getWeight() != null && competitor.getWeight().intValue() > 0) {
                validCompetitors.add(competitor);
            }
        }
        return validCompetitors;
    }

    /**
     * @return La {@link CompetitorList} des {@link Competitor} invalides (pas
     *         de poids saisi).
     */
    public CompetitorList getUnvalidCompetitors() {
        final CompetitorList unvalidCompetitors = new CompetitorList();
        for (final Competitor competitor : registeredCompetitorsList) {
            if (competitor.getWeight() == null || competitor.getWeight().intValue() <= 0) {
                unvalidCompetitors.add(competitor);
            }
        }
        return unvalidCompetitors;
    }

    /**
     * @param competitorsDao the competitorsDao to set
     */
    public void setCompetitorsDao(final CompetitorsDao competitorsDao) {
        this.competitorsDao = competitorsDao;
    }

}
