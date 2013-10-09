package com.ojt;

import java.util.List;

public class CompetitorsGroupUtilities {

	public static int calculateNbCompetitorsFromGroups(final List<CompetitorGroup> groups) {
		int nbCompetitors = 0;
		for (final CompetitorGroup group : groups) {
			nbCompetitors += group.getCompetitorsNumber();
		}
		return nbCompetitors;
	}
}
