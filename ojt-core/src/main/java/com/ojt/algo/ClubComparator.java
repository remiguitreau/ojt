package com.ojt.algo;

import com.ojt.Competitor;

import java.util.Comparator;

public class ClubComparator implements Comparator<Competitor> {

	@Override
	public int compare(final Competitor competitor1, final Competitor competitor2) {
		if ((competitor1 == null) && (competitor2 == null)) {
			return 0;
		} else if (competitor1 == null) {
			return -1;
		} else if (competitor2 == null) {
			return 1;
		}

		if ((competitor1.getClub() == null) && (competitor2.getClub() == null)) {
			return 0;
		} else if (competitor1.getClub() == null) {
			return -1;
		} else if (competitor2.getClub() == null) {
			return 1;
		}

		if ((competitor1.getClub().getClubCode() == null) && (competitor2.getClub().getClubCode() == null)) {
			return 0;
		} else if (competitor1.getClub().getClubCode() == null) {
			return -1;
		} else if (competitor2.getClub().getClubCode() == null) {
			return 1;
		}

		if (competitor1.getClub().equals(competitor2.getClub())) {
			return 0;
		}

		return competitor1.getClub().getClubCode().compareTo(competitor2.getClub().getClubCode());

	}

}
