package com.ojt.dao.ffjdacsv;

import com.ojt.Competitor;
import com.ojt.CompetitorSex;

class CSVCompetitorLineConvertor {

    public String convertCompetitorInLine(final Competitor competitor,
            final FFJDACSVColumnsIndexes columnIndexes, final int fieldsNumber) {
        final String[] buf = new String[fieldsNumber + 1];
        buf[columnIndexes.licenseCodeIndex] = competitor.getLicenseCode();
        buf[columnIndexes.fullNameIndex] = competitor.getName() + " " + competitor.getFirstName();
        buf[columnIndexes.birthDateIndex] = competitor.getBirthDate() == null ? null
                : FFJDACSVConstants.BIRTH_DATE_FORMAT.format(competitor.getBirthDate());
        buf[columnIndexes.clubDepartmentIndex] = competitor.getClub() == null ? null
                : competitor.getClub().getDepartment();
        buf[columnIndexes.clubNameIndex] = competitor.getClub() == null ? null
                : competitor.getClub().getClubName();
        buf[columnIndexes.sexIndex] = competitor.getSex() == null
                || competitor.getSex().equals(CompetitorSex.UNKNOWN) ? null : competitor.getSex().getCode();
        buf[columnIndexes.gradeIndex] = competitor.getGradeBelt() == null ? null
                : competitor.getGradeBelt().fullName;
        buf[columnIndexes.weightIndex] = competitor.getWeight() == null ? null
                : String.valueOf(competitor.getWeightAsFloat());
        return convertStringArrayToString(buf);
    }

    private String convertStringArrayToString(final String[] buf) {
        final StringBuilder builder = new StringBuilder();
        for (final String str : buf) {
            builder.append(convertToStringValue(str)).append(FFJDACSVConstants.FIELDS_SEPARATOR_PATTERN);
        }
        return builder.toString();
    }

    private String convertToStringValue(final String str) {
        return str == null ? "" : str;
    }
}
