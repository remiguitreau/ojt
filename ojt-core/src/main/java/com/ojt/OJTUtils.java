package com.ojt;

import java.util.Date;

public class OJTUtils {

	private static final String SEPA_CODE = "SEPA";
	
	/**
	 * Extraction du numéro de département à partir de l'identifiant du club.
	 * @param clubId L'identifiant du club.
	 * @return Le numéro de département.
	 */
	public static String extractDepartmentFromClubId(final String clubId) {
		final int sepaPos = clubId.indexOf(SEPA_CODE);
		return sepaPos == -1 ? "" : clubId.substring(sepaPos + SEPA_CODE.length(), sepaPos
				+ SEPA_CODE.length() + 2);
	}
	
	public static void fillCompetitorWithCategory(final Competitor competitor, final Date competitionDate) {
		competitor.setCategory(competitor.getBirthDate() == null ? CompetitorCategory.UNKNOWN : CompetitorCategory.retrieveCategoryFromBirthDateAndCompetitionDate(competitor.getBirthDate(), competitionDate));
	}
}
