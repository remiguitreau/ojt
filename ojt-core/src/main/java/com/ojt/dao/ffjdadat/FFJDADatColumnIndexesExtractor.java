package com.ojt.dao.ffjdadat;

public class FFJDADatColumnIndexesExtractor {

    public FFJDADatColumnsIndexes extractColumnIndexesFromLine(final String[] columnIdentifiers) {
        final FFJDADatColumnsIndexes indexes = new FFJDADatColumnsIndexes();
        for (int i = 0; i < columnIdentifiers.length; i++) {
            if (columnIdentifiers[i] != null) {
                if (columnIdentifiers[i].equals(FFJDADatConstants.LICENSE_CODE_IDENTIFIER)) {
                    indexes.licenseCodeIndex = i;
                } else if (columnIdentifiers[i].equals(FFJDADatConstants.NAME_IDENTIFIER)) {
                    indexes.nameIndex = i;
                } else if (columnIdentifiers[i].equals(FFJDADatConstants.FIRST_NAME_IDENTIFIER)
                        || columnIdentifiers[i].equals(FFJDADatConstants.FIRST_NAME2_IDENTIFIER)) {
                    indexes.firstNameIndex = i;
                } else if (columnIdentifiers[i].equals(FFJDADatConstants.CLUB_CODE_IDENTIFIER)) {
                    indexes.clubCodeIndex = i;
                } else if (columnIdentifiers[i].equals(FFJDADatConstants.DEPARTMENT)) {
                    indexes.departmentIndex = i;
                } else if (columnIdentifiers[i].equals(FFJDADatConstants.CLUB_NAME_IDENTIFIER)) {
                    indexes.clubNameIndex = i;
                } else if (columnIdentifiers[i].equals(FFJDADatConstants.BIRTH_DATE_IDENTIFIER)) {
                    indexes.birthDateIndex = i;
                } else if (columnIdentifiers[i].equals(FFJDADatConstants.SEX_IDENTIFIER)) {
                    indexes.sexIndex = i;
                } else if (columnIdentifiers[i].equals(FFJDADatConstants.GRADE_IDENTIFIER)) {
                    indexes.gradeIndex = i;
                }
            }
        }
        checkColumnIndexesValidity(indexes);
        return indexes;
    }

    private void checkColumnIndexesValidity(final FFJDADatColumnsIndexes indexes) {
        if (indexes.clubCodeIndex == -1 && indexes.departmentIndex == -1 || indexes.clubNameIndex == -1
                || indexes.firstNameIndex == -1 || indexes.licenseCodeIndex == -1 || indexes.nameIndex == -1) {
            throw new FFJDADatBadFormatException();
        }
    }
}
