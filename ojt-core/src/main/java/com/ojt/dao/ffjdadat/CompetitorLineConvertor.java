package com.ojt.dao.ffjdadat;

import com.ojt.Competitor;
import com.ojt.CompetitorSex;

class CompetitorLineConvertor {
	
	public String convertCompetitorInLine(final Competitor competitor, final FFJDADatColumnsIndexes columnIndexes, final int fieldsNumber) {
		final String[] buf = new String[fieldsNumber+1];
		buf[columnIndexes.licenseCodeIndex] = competitor.getLicenseCode();
		buf[columnIndexes.nameIndex] = competitor.getName();
		buf[columnIndexes.firstNameIndex] = competitor.getFirstName();
		buf[columnIndexes.birthDateIndex] = competitor.getBirthDate() == null ? null : FFJDADatConstants.BIRTH_DATE_FORMAT.format(competitor.getBirthDate());
		buf[columnIndexes.clubCodeIndex] = competitor.getClub() == null ? null : competitor.getClub().getClubCode();
		buf[columnIndexes.clubNameIndex] = competitor.getClub() == null ? null : competitor.getClub().getClubName();
		buf[columnIndexes.sexIndex] = competitor.getSex() == null || competitor.getSex().equals(CompetitorSex.UNKNOWN) ? null : competitor.getSex().getHumanName();
		buf[columnIndexes.gradeIndex] = competitor.getGradeBelt() == null ? null : competitor.getGradeBelt().code;
		buf[fieldsNumber] = competitor.getWeight() == null ? null : String.valueOf(competitor.getWeightAsFloat());
		return convertStringArrayToString(buf);
	}

	private String convertStringArrayToString(String[] buf) {
		final StringBuilder builder = new StringBuilder();
		for(final String str : buf) {
			builder.append(convertToStringValue(str)).append(FFJDADatConstants.FIELDS_SEPARATOR_PATTERN);
		}
		return builder.toString();
	}

	private String convertToStringValue(final String str) {
		return "\""+(str==null ? "" : str)+"\"";
	}
}
