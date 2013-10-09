package com.ojt;

import java.util.Arrays;

public class CompetitionCategory {

    public final static CompetitionCategory UNKNOWN_CATEGORY = new CompetitionCategory("Indéterminé",
            CompetitorSex.UNKNOWN, CompetitorCategory.UNKNOWN);

    private CompetitorCategory[] competitorCategoriesAccepted;

    private final CompetitorSex competitorSexAccepted;

    private String humanName;

    public CompetitionCategory(final CompetitorSex competitorSexAccepted,
            final CompetitorCategory... competitorCategoriesAccepted) {
        this.competitorSexAccepted = competitorSexAccepted;
        this.competitorCategoriesAccepted = competitorCategoriesAccepted;
    }

    public CompetitionCategory(final String humanName, final CompetitorSex competitorSexAccepted) {
        this.humanName = humanName;
        this.competitorSexAccepted = competitorSexAccepted;

    }

    public CompetitionCategory(final String humanName, final CompetitorSex competitorSexAccepted,
            final CompetitorCategory... competitorCategoriesAccepted) {
        this.humanName = humanName;
        this.competitorSexAccepted = competitorSexAccepted;
        this.competitorCategoriesAccepted = competitorCategoriesAccepted;
    }

    public String getHumanName() {
        return humanName;
    }

    public CompetitorSex getCompetitorSexAccepted() {
        return competitorSexAccepted;
    }

    public CompetitorCategory[] getCompetitorCategoriesAccepted() {
        return competitorCategoriesAccepted;
    }

    public void setCompetitorCategoriesAccepted(final CompetitorCategory[] competitorCategoriesAccepted) {
        this.competitorCategoriesAccepted = competitorCategoriesAccepted;
    }

    public boolean isCompetitorAccepted(final Competitor competitor) {
        if (competitorSexAccepted != CompetitorSex.UNKNOWN && competitor.getSex() != competitorSexAccepted) {
            return false;
        }
        if (competitorCategoriesAccepted.length == 1
                && competitorCategoriesAccepted[0] == CompetitorCategory.UNKNOWN) {
            return true;
        }
        for (final CompetitorCategory category : competitorCategoriesAccepted) {
            if (category == competitor.getCategory()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof CompetitionCategory
                && competitorSexAccepted == ((CompetitionCategory) obj).competitorSexAccepted) {
            return Arrays.equals(competitorCategoriesAccepted,
                    ((CompetitionCategory) obj).competitorCategoriesAccepted);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
