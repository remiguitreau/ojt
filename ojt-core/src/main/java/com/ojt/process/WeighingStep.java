/**
 * 
 */
package com.ojt.process;

import org.apache.log4j.Logger;

import com.ojt.Competitor;
import com.ojt.OJTConfiguration;
import com.ojt.balance.BalanceDriver;
import com.ojt.ui.CompetitorWeightEditorPanel;

import java.awt.Dimension;

import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

/**
 * Etape de pes�e.
 * @author R�mi "DwarfConan" Guitreau
 * @since 17 oct. 2009 : Cr�ation
 */
public class WeighingStep extends AbstractStep {

    private CompetitorWeightEditorPanel stepPanel;

    private final BalanceDriver balanceDriver;

    private final boolean enableWeighingImport;

    private CompetitionDatas competitionDatas;

    private final Logger logger = Logger.getLogger(getClass());

    private final JFrame ojtFrame;

    public WeighingStep(final boolean enableWeighingImport, final BalanceDriver balanceDriver,
            final JFrame ojtFrame) {
        super();

        this.enableWeighingImport = enableWeighingImport;
        this.balanceDriver = balanceDriver;
        this.ojtFrame = ojtFrame;
        initStepPanel();
    }

    // ---------------------------------------------------------
    // Impl�mentation de AbstractStep
    // ---------------------------------------------------------

    @Override
    public JComponent getStepComponent() {
        return stepPanel;
    }

    @Override
    public void process(final CompetitionDatas competDatas) {
        this.competitionDatas = competDatas;
        balanceDriver.addListener(stepPanel);
        stepPanel.setCompetitionDescriptor(competDatas.getCompetitionDescriptor());
        stepPanel.setCompetitorsDao(competDatas.getCompetitorsDao());
        stepPanel.setCompetitorList(competDatas.getCompetitorsDao().retrieveCompetitors());
        stepPanel.fillClubList();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ojtFrame.setTitle(competDatas.getCompetitionDescriptor().getCompetitionName() + " - OJT");
                stepPanel.fillTable();
                stepPanel.setVisible(true);
                stepPanel.setFocusOnFieldSearch();
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
                        "Tous les comp�titeurs n'ont pas leur poids renseign�", "OJT",
                        JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, new String[] {
                                "Voir les comp�titeurs sans poids", "Annuler", "Poursuivre quand m�me" },
                        "Poursuivre quand m�me")) {
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
                        JOptionPane.showMessageDialog(null, scrollPane, "Comp�titeurs sans poids",
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
        return "Pes�e";
    }

    // ---------------------------------------------------------
    // Priv�es
    // ---------------------------------------------------------
    private void initStepPanel() {
        stepPanel = new CompetitorWeightEditorPanel(enableWeighingImport);
    }
}
