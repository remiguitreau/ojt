/**
 * 
 */
package com.ojt.process;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;

import com.ojt.OjtConstants;
import com.ojt.dao.CompetitorsDaoFactory;
import com.ojt.tools.FileNameComposer;
import com.ojt.ui.AutoRegistrationPanel;

import java.io.File;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

public class AutoRegistrationStep extends AbstractStep {

    private AutoRegistrationPanel stepPanel;

    private CompetitionDatas competitionDatas;

    private final Logger logger = Logger.getLogger(getClass());

    File autoPersistFile = null;

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
        stepPanel.setCompetitorsList(competDatas.getCompetitorsDao().retrieveCompetitors());
        autoPersistFile = new File(new File(OjtConstants.PERSISTANCY_DIRECTORY,
                FileNameComposer.composeDirectoryName(competDatas.getCompetitionDescriptor())),
                "autoRegistrationPeristancy."
                        + FilenameUtils.getExtension(competDatas.getCompetitionFile().getName()));
        stepPanel.setRegisteredCompetitorsDao(CompetitorsDaoFactory.createCompetitorsDao(autoPersistFile,
                false));
        if (!autoPersistFile.exists()) {
            try {
                FileUtils.copyURLToFile(
                        OjtConstants.class.getResource("emptyCompetitionFile."
                                + FilenameUtils.getExtension(competDatas.getCompetitionFile().getName())),
                        autoPersistFile);
            } catch (final Exception ex) {
                logger.error("Unable to create auto registration persistancy file...", ex);

            }
        } else {
            stepPanel.setRegisteredCompetitorList(stepPanel.getRegisteredCompetitorsDao().retrieveCompetitors());
        }

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
        competitionDatas.setValidCompetitors(stepPanel.getValidCompetitors());
        competitionDatas.setWeighingPost("auto" + competitionDatas.getWeighingPost());
        if (autoPersistFile != null && autoPersistFile.exists()) {
            try {
                FileUtils.copyFile(autoPersistFile, competitionDatas.getCompetitionFile());
            } catch (final Exception ex) {
                logger.error("Error while saving persistancy to competitio file.", ex);
            }
            autoPersistFile.delete();
        }
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
