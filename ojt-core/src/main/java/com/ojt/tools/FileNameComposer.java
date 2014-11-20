package com.ojt.tools;

import com.ojt.CompetitionCategory;
import com.ojt.CompetitionDescriptor;
import com.ojt.OJTCompetitionCategories;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author CDa
 * @since 24 août 2009 (CDa) : Création
 */
public class FileNameComposer {

    /**
     * Méthode de création du nom de fichier de réserve de compétiteurs pour un
     * prochain tri
     * @param competitionDescriptor
     * @return le nom du fichier
     */
    public static String composeSavedFileForAnotherSortName(
            final CompetitionDescriptor competitionDescriptor, final String extension) {
        return composeDirectoryName(competitionDescriptor) + "-reserve" + extension;
    }

    /**
     * Méthode de création du nom de fichier de poste
     * @param competitionDescriptor
     * @return le nom du fichier
     */
    public static String composeWeighingPostFileName(final CompetitionDescriptor competitionDescriptor,
            final String weighingPost, final String extension) {
        return composeDirectoryName(competitionDescriptor) + "-poste" + weighingPost + "_"
                + new SimpleDateFormat("dd.MM.yyyy.HH.mm.ss").format(new Date()) + extension;
    }

    /**
     * Méthode de création du nom de la sauvegarde
     * @param competitionDescriptor
     * @return le nom du fichier
     */
    public static String composeFileName(final CompetitionDescriptor competitionDescriptor,
            final String extension) {
        return composeDirectoryName(competitionDescriptor) + "-"
                + new SimpleDateFormat("dd.MM.yyyy.HH.mm").format(new Date()) + extension;
    }

    /**
     * Méthode de création du nom du répertoire de la manifestation
     * @param competitionDescriptor
     * @return le nom du répertoire de la manifestation
     */
    public static String composeDirectoryName(final CompetitionDescriptor competitionDescriptor) {
        return (competitionDescriptor.getCategory() == CompetitionCategory.UNKNOWN_CATEGORY ? ""
                : OJTCompetitionCategories.formatHumanName(competitionDescriptor.getCategory().getHumanName())
                        + "-")
                + competitionDescriptor.getCompetitionName().replaceAll(" ", "_")
                + "-"
                + competitionDescriptor.getLocation().replaceAll(" ", "_")
                + "-"
                + competitionDescriptor.getDate().replaceAll(" ", "_");
    }

    public static CompetitionDescriptor decomposeFileName(final String fileName) {
        final CompetitionDescriptor competitionDescriptor = new CompetitionDescriptor();
        final String file[] = fileName.split("-");

        try {
            competitionDescriptor.setCategory(OJTCompetitionCategories.retrieveCategoryFromHumanName(file[0]));
            competitionDescriptor.setName(file[1]);
            competitionDescriptor.setLocation(file[2]);
            competitionDescriptor.setDate(file[3]);
        } catch (final Exception ex) {
            competitionDescriptor.setName(file[0]);
            competitionDescriptor.setLocation(file[1]);
            competitionDescriptor.setDate(file[2]);
        }

        return competitionDescriptor;
    }
}
