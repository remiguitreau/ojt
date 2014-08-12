package com.ojt;

public enum CompetitorSex {
    MALE("Masculin", "M"), FEMALE("Féminin", "F"), UNKNOWN("Inconnnu", "U");

    private final String humanName;

    private final String code;

    private CompetitorSex(final String humanName, final String code) {
        this.humanName = humanName;
        this.code = code;
    }

    public String getHumanName() {
        return humanName;
    }

    public String getCode() {
        return code;
    }

    public static CompetitorSex retrieveSexFromString(final String sexAsString) {
        for (final CompetitorSex sex : CompetitorSex.values()) {
            if (sexAsString.equals(sex.getHumanName()) || sexAsString.equals(sex.getCode())) {
                return sex;
            }
        }
        return UNKNOWN;
    }
}
