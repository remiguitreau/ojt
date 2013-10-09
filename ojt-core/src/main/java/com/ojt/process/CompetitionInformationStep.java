/**
 * 
 */
package com.ojt.process;

import org.apache.log4j.Logger;

import com.ojt.OJTConfiguration;
import com.ojt.ui.ImportPanel;

import java.io.File;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

/**
 * Etape de saisie des informations de la compétition
 * @author Rémi "DwarfConan" Guitreau
 * @since 17 oct. 2009 : Création
 */
public class CompetitionInformationStep extends AbstractStep {

    public final static int ONLY_WEIGHING = 0;

    public final static int ONLY_SORT = 1;

    public final static int WEIGHING_AND_SORT = 2;

    public final static int SOURCE_CREATION = 3;

    private ImportPanel stepPanel;

    private CompetitionDatas competitionDatas;

    private final int type;

    public CompetitionInformationStep(final int type) {
        super();

        this.type = type;
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
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                stepPanel.setCompetitionDescriptor(competDatas.getCompetitionDescriptor());
                stepPanel.setVisible(true);
            }
        });
    }

    @Override
    public void stepFinish() {
        stepFinish(false);
    }

    public void stepFinish(final boolean onlyWithWeight) {
        competitionDatas.setOnlyWithWeight(onlyWithWeight);
        competitionDatas.setCompetitionDescriptor(stepPanel.getCompetitionDescriptor());
        competitionDatas.setCompetitionFile(stepPanel.getCompetitionFile());
        final String fileModelPath = OJTConfiguration.getInstance().getProperty(
                OJTConfiguration.MODEL_FILE_PATH);
        final File modelFile = new File(fileModelPath);
        if (modelFile.exists() && modelFile.isFile()) {
            competitionDatas.setModelsFile(modelFile);
        } else {
            Logger.getLogger(getClass()).warn(
                    "Models file does not exists, use default models: " + fileModelPath);
            competitionDatas.setModelsFile(null);
        }
        competitionDatas.setWeighingPost(stepPanel.getWeighingPost());
        competitionDatas.setCompetitorsPerGroup(stepPanel.getCompetitorPerGroup());
        super.stepFinish();
    }

    @Override
    public boolean finalizeStep() {
        return true;
    }

    @Override
    public String getTitle() {
        return "Informations de la compétition";
    }

    // ---------------------------------------------------------
    // Privées
    // ---------------------------------------------------------
    private void initStepPanel() {
        stepPanel = new ImportPanel(this, (type == ONLY_WEIGHING) || (type == WEIGHING_AND_SORT),
                (type == ONLY_SORT) || (type == WEIGHING_AND_SORT), type == SOURCE_CREATION);
    }
}
