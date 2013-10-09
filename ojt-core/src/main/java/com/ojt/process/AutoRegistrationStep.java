/**
 * 
 */
package com.ojt.process;

import org.apache.log4j.Logger;

import com.ojt.Competitor;
import com.ojt.OJTConfiguration;
import com.ojt.ui.AutoRegistrationPanel;

import java.awt.Dimension;

import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

public class AutoRegistrationStep extends AbstractStep {

    private AutoRegistrationPanel stepPanel;

    private CompetitionDatas competitionDatas;

    private final Logger logger = Logger.getLogger(getClass());

    public AutoRegistrationStep() {
        initStepPanel();
    }

    // ---------------------------------------------------------
    // Implémentation de AbstractStep
    // ---------------------------------------------------------

    @Override
    public JComponent getStepComponent() {
        return stepPanel;
    }

    @Override
    public void process(final CompetitionDatas competDatas) {
        this.competitionDatas = competDatas;
        stepPanel.setCompetitionDescriptor(competDatas.getCompetitionDescriptor());
        stepPanel.setCompetitorsDao(competDatas.getCompetitorsDao());
        stepPanel.setCompetitorsList(competDatas.getCompetitorsDao().retrieveCompetitors());
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                stepPanel.setFocusable(true);
                stepPanel.setVisible(true);
                stepPanel.requestFocusInWindow();
                stepFinish();
            }
        });
    }

    @Override
    public boolean finalizeStep() {
        // 30.10.2009 (FMo) : ajout d'une prop de config pour savoir si on
        // affiche le dialogue ou pas
        if (OJTConfiguration.getInstance().getPropertyAsBoolean(OJTConfiguration.SHOW_WARN_ON_EMPTY_WEIGHT,
                true)) {
            if (!stepPanel.getUnvalidCompetitors().isEmpty()) {
                switch (JOptionPane.showOptionDialog(null,
                        "Tous les compétiteurs n'ont pas leur poids renseigné", "OJT",
                        JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, new String[] {
                                "Voir les compétiteurs sans poids", "Annuler", "Poursuivre quand même" },
                        "Poursuivre quand même")) {
                    case 0:
                        final JList list = new JList();
                        final DefaultListModel model = new DefaultListModel();
                        for (final Competitor comp : stepPanel.getUnvalidCompetitors()) {
                            model.addElement(comp.getDisplayName() + " [" + comp.getClub() + "]");
                        }
                        list.setModel(model);
                        final JScrollPane scrollPane = new JScrollPane();
                        scrollPane.setViewportView(list);
                        scrollPane.setPreferredSize(new Dimension(500, 500));
                        JOptionPane.showMessageDialog(null, scrollPane, "Compétiteurs sans poids",
                                JOptionPane.INFORMATION_MESSAGE);
                        return finalizeStep();
                    case 1:
                        return false;
                    case 2:
                        competitionDatas.setValidCompetitors(stepPanel.getValidCompetitors());
                        return true;
                    default:
                        return false;
                }
            }
        }
        competitionDatas.setValidCompetitors(stepPanel.getValidCompetitors());
        return true;
    }

    @Override
    public String getTitle() {
        return "Enregistrement automatique";
    }

    // ---------------------------------------------------------
    // Privées
    // ---------------------------------------------------------
    private void initStepPanel() {
        stepPanel = new AutoRegistrationPanel();
    }
}
