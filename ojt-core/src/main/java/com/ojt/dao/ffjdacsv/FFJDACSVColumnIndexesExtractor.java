package com.ojt.dao.ffjdacsv;

public class FFJDACSVColumnIndexesExtractor {

    public FFJDACSVColumnsIndexes extractColumnIndexesFromLine(final String[] columnIdentifiers) {
        final FFJDACSVColumnsIndexes indexes = new FFJDACSVColumnsIndexes();
        for (int i = 0; i < columnIdentifiers.length; i++) {
            if (columnIdentifiers[i] != null) {
                if (columnIdentifiers[i].equals(FFJDACSVConstants.LICENSE_CODE_IDENTIFIER)) {
                    indexes.licenseCodeIndex = i;
                } else if (columnIdentifiers[i].equals(FFJDACSVConstants.FULL_NAME_IDENTIFIER)) {
                    indexes.fullNameIndex = i;
                } else if (columnIdentifiers[i].equals(FFJDACSVConstants.CLUB_DEPARTMENT_IDENTIFIER)) {
                    indexes.clubDepartmentIndex = i;
                } else if (columnIdentifiers[i].equals(FFJDACSVConstants.CLUB_NAME_IDENTIFIER)) {
                    indexes.clubNameIndex = i;
                } else if (columnIdentifiers[i].equals(FFJDACSVConstants.BIRTH_DATE_IDENTIFIER)) {
                    indexes.birthDateIndex = i;
                } else if (columnIdentifiers[i].equals(FFJDACSVConstants.SEX_IDENTIFIER)) {
                    indexes.sexIndex = i;
                } else if (columnIdentifiers[i].equals(FFJDACSVConstants.GRADE_IDENTIFIER)) {
                    indexes.gradeIndex = i;
                } else if (columnIdentifiers[i].equals(FFJDACSVConstants.WEIGHT_IDENTIFIER)) {
                    indexes.weightIndex = i;
                }
            }
        }
        checkColumnIndexesValidity(indexes);
        return indexes;
    }

    private void checkColumnIndexesValidity(final FFJDACSVColumnsIndexes indexes) {
        if (indexes.clubDepartmentIndex == -1 || indexes.clubNameIndex == -1 || indexes.fullNameIndex == -1
                || indexes.licenseCodeIndex == -1 || indexes.weightIndex == -1) {
            throw new FFJDACSVBadFormatException();
        }
    }
}
