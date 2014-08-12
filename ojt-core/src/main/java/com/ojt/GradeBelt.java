package com.ojt;

public enum GradeBelt {
    WHITE_YELLOW("BJ", "BJ", "Blanche/Jaune"), YELLOW("J", "J", "Jaune"), YELLOW_ORANGE("JO", "JO",
            "Jaune/Orange"), ORANGE("O", "O", "Orange"), ORANGE_GREEN("OV", "OV", "Orange/Verte"), GREEN("V",
            "V", "Verte"), BLUE("B", "B", "Bleue"), BROWN("M", "M", "Marron"), FIRST_DAN("1", "1D", "1er Dan"), SECOND_DAN(
                            "2", "2D", "2eme Dan"), THIRD_DAN("3", "3D", "3eme Dan"), FOURTH_DAN("4", "4D", "4eme Dan"), FIFTH_DAN(
            "5", "5D", "5eme Dan"), SIXTH_DAN("6", "6D", "6eme Dan");

    public final String code;

    public final String abbreviation;

    public final String fullName;

    private GradeBelt(final String code, final String abbreviation, final String fullName) {
        this.code = code;
        this.abbreviation = abbreviation;
        this.fullName = fullName;
    }

    public static GradeBelt retrieveGradeFromString(final String extractStringValue) {
        if (extractStringValue == null || extractStringValue.isEmpty()) {
            return null;
        }
        for (final GradeBelt belt : GradeBelt.values()) {
            if (belt.code.equals(extractStringValue) || belt.fullName.equals(extractStringValue)) {
                return belt;
            }
        }
        return null;
    }
}
