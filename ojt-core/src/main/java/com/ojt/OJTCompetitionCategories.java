package com.ojt;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class OJTCompetitionCategories {

    static List<CompetitionCategory> categories = null;

    public static List<CompetitionCategory> getCategories() {
        if (categories == null) {
            categories = loadCategoriesFromConfiguration();
        }
        return categories;
    }

    public static void addNewCompetitionCategory(final CompetitionCategory competitionCategory) {
        getCategories().add(competitionCategory);
        OJTConfiguration.getInstance().updateProperty(OJTConfiguration.COMPETITION_CATEGORIES,
                categoriesAsString());
    }

    private static String categoriesAsString() {
        final StringBuilder buf = new StringBuilder();
        for (final CompetitionCategory categ : getCategories()) {
            if (buf.length() > 0) {
                buf.append(';');
            }
            buf.append(categ.getHumanName()).append('/').append(categ.getCompetitorSexAccepted().toString());
            for (final CompetitorCategory compCateg : categ.getCompetitorCategoriesAccepted()) {
                buf.append('/').append(compCateg.toString());
            }
        }
        return buf.toString();
    }

    private static List<CompetitionCategory> loadCategoriesFromConfiguration() {
        final List<CompetitionCategory> loadedCategories = new LinkedList<CompetitionCategory>();
        final String[] categoriesAsString = OJTConfiguration.getInstance().getProperty(
                OJTConfiguration.COMPETITION_CATEGORIES).split(";");
        if (categoriesAsString != null && categoriesAsString.length > 0) {
            for (final String categoryAsString : categoriesAsString) {
                if (!categoryAsString.trim().isEmpty()) {
                    final String[] categParams = categoryAsString.split("/");
                    final CompetitionCategory category = new CompetitionCategory(categParams[0],
                            CompetitorSex.valueOf(categParams[1]));
                    final List<CompetitorCategory> compCategs = new ArrayList<CompetitorCategory>();
                    for (int i = 2; i < categParams.length; i++) {
                        compCategs.add(CompetitorCategory.valueOf(categParams[i]));
                    }
                    category.setCompetitorCategoriesAccepted(compCategs.toArray(new CompetitorCategory[compCategs.size()]));
                    loadedCategories.add(category);
                }
            }
        }
        return loadedCategories;
    }

    public static CompetitionCategory retrieveCategoryFromHumanName(final String humanName) {
        for (final CompetitionCategory categ : getCategories()) {
            if (formatHumanName(categ.getHumanName()).equals(formatHumanName(humanName))) {
                return categ;
            }
        }
        throw new IllegalArgumentException("Unknow category: " + humanName);
    }

    public final static String formatHumanName(final String humanName) {
        String formattedHumanName = humanName;
        for (final String charSpec : new String[] { "/", " ", "\\+", ";", "!", "\\?" }) {
            formattedHumanName = formattedHumanName.replaceAll(charSpec, "_");
        }
        return formattedHumanName;
    }
}
