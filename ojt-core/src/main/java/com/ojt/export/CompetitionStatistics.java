/**
 * 
 */
package com.ojt.export;

import com.ojt.Club;
import com.ojt.Competitor;
import com.ojt.CompetitorGroup;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Rémi "DwarfConan" Guitreau
 * @since 22 oct. 2009 : Création
 */
public class CompetitionStatistics {

	private int competitorsNumber = 0;

	private final Map<String, Integer> counterPerClub = new HashMap<String, Integer>();

	private final Map<String, Integer> counterPerDepartment = new HashMap<String, Integer>();

	public void addGroup(final CompetitorGroup group) {
		competitorsNumber += group.getCompetitorsNumber();
		for (final Competitor competitor : group.getCompetitors()) {
			final Club club = competitor.getClub();

			if (counterPerClub.containsKey(club.toString2())) {
				counterPerClub.put(club.toString2(), counterPerClub.get(club.toString2()) + 1);
			} else {
				counterPerClub.put(club.toString2(), 1);
			}

			if (counterPerDepartment.containsKey(club.getDepartment())) {
				counterPerDepartment.put(club.getDepartment(),
						counterPerDepartment.get(club.getDepartment()) + 1);
			} else {
				counterPerDepartment.put(club.getDepartment(), 1);
			}
		}
	}

	public int getCompetitorsNumber() {
		return competitorsNumber;
	}

	public Map<String, Integer> getCounterPerClub() {
		return counterPerClub;
	}

	public Map<String, Integer> getCounterPerDepartment() {
		return counterPerDepartment;
	}

}
