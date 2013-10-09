package com.ojt;

import java.util.Calendar;
import java.util.Date;

public enum CompetitorCategory {

    UNKNOWN("Indéterminé") {
        @Override
        public boolean isCompetitorFromCategory(final int birthYear, final int yearOfReference) {
            return true;
        }
    },
    PREPOUSSIN("Pré poussin") {
        @Override
        public boolean isCompetitorFromCategory(final int birthYear, final int yearOfReference) {
            return yearOfReference - birthYear == 7 || yearOfReference - birthYear == 8;
        }
    },
    POUSSIN("Poussin") {
        @Override
        public boolean isCompetitorFromCategory(final int birthYear, final int yearOfReference) {
            return yearOfReference - birthYear == 9 || yearOfReference - birthYear == 10;
        }
    },
    BENJAMIN("Benjamin") {
        @Override
        public boolean isCompetitorFromCategory(final int birthYear, final int yearOfReference) {
            return yearOfReference - birthYear == 11 || yearOfReference - birthYear == 12;
        }
    },
    MINIME("Minime") {
        @Override
        public boolean isCompetitorFromCategory(final int birthYear, final int yearOfReference) {
            return yearOfReference - birthYear == 13 || yearOfReference - birthYear == 14;
        }
    },
    CADET("Cadet") {
        @Override
        public boolean isCompetitorFromCategory(final int birthYear, final int yearOfReference) {
            return yearOfReference - birthYear == 15 || yearOfReference - birthYear == 16
                    || yearOfReference - birthYear == 17;
        }
    },
    JUNIOR("Junior") {
        @Override
        public boolean isCompetitorFromCategory(final int birthYear, final int yearOfReference) {
            return yearOfReference - birthYear == 18 || yearOfReference - birthYear == 19
                    || yearOfReference - birthYear == 20;
        }
    },
    SENIOR("Senior") {
        @Override
        public boolean isCompetitorFromCategory(final int birthYear, final int yearOfReference) {
            return yearOfReference - birthYear >= 21;
        }
    };

    private final String humanName;

    private CompetitorCategory(final String humanName) {
        this.humanName = humanName;
    }

    public String getHumanName() {
        return humanName;
    }

    public abstract boolean isCompetitorFromCategory(final int birthYear, final int yearOfReference);

    public static CompetitorCategory retrieveCategoryFromBirthDateAndCompetitionDate(final Date birthDate,
            final Date competitionDate) {
        final Calendar cal = Calendar.getInstance();
        cal.setTime(birthDate);
        final int birthYear = cal.get(Calendar.YEAR);
        cal.setTime(competitionDate);
        final int referenceYear;
        if (cal.get(Calendar.MONTH) < Calendar.SEPTEMBER) {
            referenceYear = cal.get(Calendar.YEAR);
        } else {
            referenceYear = cal.get(Calendar.YEAR) + 1;
        }
        for (final CompetitorCategory competitorCategory : CompetitorCategory.values()) {
            if (competitorCategory != UNKNOWN
                    && competitorCategory.isCompetitorFromCategory(birthYear, referenceYear)) {
                return competitorCategory;
            }
        }
        return UNKNOWN;
    }

    public static CompetitorCategory retrieveCategoryFromHumanName(final String humanName) {
        for (final CompetitorCategory competitorCategory : CompetitorCategory.values()) {
            if (humanName.equals(competitorCategory.getHumanName())) {
                return competitorCategory;
            }
        }
        return UNKNOWN;
    }
}
