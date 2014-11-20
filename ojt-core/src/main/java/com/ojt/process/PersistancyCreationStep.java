/**
 *
 */
package com.ojt.process;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import com.ojt.CompetitionDescriptor;
import com.ojt.OjtConstants;
import com.ojt.dao.CompetitorsDaoFactory;
import com.ojt.tools.FileNameComposer;

import java.io.File;
import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.JOptionPane;

/**
 * Etape de saisie des informations de la compétition
 * @author Rémi "DwarfConan" Guitreau
 * @since 17 oct. 2009 : Création
 */
public class PersistancyCreationStep extends AbstractStep {

    private final Logger logger = Logger.getLogger(getClass());

    public PersistancyCreationStep() {
        super();
    }

    // ---------------------------------------------------------
    // Implémentation de AbstractStep
    // ---------------------------------------------------------

    @Override
    public JComponent getStepComponent() {
        return null;
    }

    @Override
    public void process(final CompetitionDatas competitionDatas) {
        try {
            competitionDatas.setCompetitionFile(createManifestationFile(competitionDatas));
            competitionDatas.setCompetitorsDao(CompetitorsDaoFactory.createCompetitorsDao(
                    competitionDatas.getCompetitionFile(), competitionDatas.getOnlyWithWeight()));
        } catch (final IOException ex) {
            logger.error("Erreur lors de la création de la persistance.", ex);
            JOptionPane.showMessageDialog(
                    null,
                    "Impossible de créer la persistance pour cette manifestation. Les données de poids ne seront pas conservées.",
                    "OJT", JOptionPane.WARNING_MESSAGE);
        }
        stepFinish();
    }

    @Override
    public boolean finalizeStep() {
        return true;
    }

    @Override
    public String getTitle() {
        return "Mise en place de la persistance";
    }

    // ---------------------------------------------------------
    // Privées
    // ---------------------------------------------------------
    private File createManifestationFile(final CompetitionDatas competitionDatas) throws IOException {
        final File dir = new File(OjtConstants.PERSISTANCY_DIRECTORY,
                FileNameComposer.composeDirectoryName(competitionDatas.getCompetitionDescriptor()));
        if (!dir.exists()) {
            dir.mkdirs();
        }
        final CompetitionDescriptor compDescr = competitionDatas.getCompetitionDescriptor();
        File manifFile = new File(dir, FileNameComposer.composeFileName(compDescr,
                "_" + competitionDatas.getWeighingPost() + "_" + System.currentTimeMillis() + "_"
                        + extractExtensionFromFileName(competitionDatas.getCompetitionFile().getName())));
        // Si le fichier chargé a le même nom que la persistance on
        // incrémente le nom de la persistance.
        // Cela peut se produire si on recharge un fichier de
        // persistance avec les mêmes paramètres de compétition
        if (manifFile.getName().equals(competitionDatas.getCompetitionFile().getName())) {
            manifFile = new File(dir, FileNameComposer.composeFileName(compDescr, "_1"
                    + extractExtensionFromFileName(competitionDatas.getCompetitionFile().getName())));
        }
        FileUtils.copyFile(competitionDatas.getCompetitionFile(), manifFile);
        return manifFile;
    }

    private String extractExtensionFromFileName(final String fileName) {
        final int pos = fileName.lastIndexOf('.');
        if (pos != -1) {
            return fileName.substring(pos);
        }
        return "";
    }
}
