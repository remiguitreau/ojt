package com.ojt.algo;

import com.ojt.Categorie;
import com.ojt.Competitor;
import com.ojt.CompetitorCategory;
import com.ojt.CompetitorGroup;
import com.ojt.CompetitorList;
import com.ojt.CompetitorPoule;
import com.ojt.OjtConstants;
import com.ojt.CompetitorPoule.PouleNumber;

import java.util.ArrayList;
import java.util.LinkedList;

public class Algorithme {

	public float getMinWeight(final CompetitorList competitors) {
		if ((competitors == null) || competitors.isEmpty()) {
			throw new IllegalArgumentException("Competitor list can't be null or empty");
		}
		float min = -1;
		boolean initialized = false;
		for (final Competitor competitor : competitors) {
			if (!initialized) {
				min = competitor.getWeight();
				initialized = true;
			} else if (min > competitor.getWeight()) {
				min = competitor.getWeight();
			}
		}
		return min;
	}

	public float getMaxWeight(final CompetitorList competitors) {
		if ((competitors == null) || competitors.isEmpty()) {
			throw new IllegalArgumentException("Competitor list can't be null");
		}
		float max = -1;
		boolean initialized = false;
		for (final Competitor competitor : competitors) {
			if (!initialized) {
				max = competitor.getWeight();
				initialized = true;
			} else if (max < competitor.getWeight()) {
				max = competitor.getWeight();
			}
		}
		return max;
	}

	public CompetitorCategory getCategory(final CompetitorList competitors) {
		if ((competitors == null) || competitors.isEmpty()) {
			throw new IllegalArgumentException("Competitor list can't be null");
		}
		for (final Competitor competitor : competitors) {
			if (competitor != null) {
				return competitor.getCategory();
			}
		}
		return CompetitorCategory.UNKNOWN;
	}

	/**
	 * 1JUDOKA POULE DE 4 2J P4 3J P4 4J P4 5J P5 6J 2P3 7J P3+P4 8J 2P4 9J 3P3
	 * 10J 2P3+P4 11J 2P4+P3 12J 3P4 13J 3P3+P4 14J 2P4+2P3 15 3P4+P3 16 4P4
	 * @param competitors
	 * @return poules created
	 */
	public ArrayList<CompetitorPoule> getPoules(final CompetitorList competitors) {
		if ((competitors == null) || competitors.isEmpty()) {
			throw new IllegalArgumentException("Competitor list can't be null");
		}

		final int nb = competitors.size();

		final ArrayList<CompetitorPoule> result = new ArrayList<CompetitorPoule>();
		if ((nb < 5) || (nb == 8) || (nb == 11) || (nb == 12) || (nb >= 14)) {
			final CompetitorPoule pouleA = new CompetitorPoule(PouleNumber.A);
			result.add(pouleA);
		}
		if (nb == 5) {
			final CompetitorPoule pouleA = new CompetitorPoule(PouleNumber.A);
			result.add(pouleA);
		}
		if ((nb == 6) || (nb == 9) || (nb == 10) || (nb == 13)) {
			final CompetitorPoule pouleA = new CompetitorPoule(PouleNumber.A);
			final CompetitorPoule pouleB = new CompetitorPoule(PouleNumber.B);
			result.add(pouleA);
			result.add(pouleB);
		}
		if (nb == 7) {
			final CompetitorPoule pouleA = new CompetitorPoule(PouleNumber.A);
			final CompetitorPoule pouleB = new CompetitorPoule(PouleNumber.B);
			result.add(pouleA);
			result.add(pouleB);
		}
		if ((nb == 8) || (nb == 11) || (nb == 12) || (nb == 14) || (nb == 15) || (nb == 16)) {
			final CompetitorPoule pouleB = new CompetitorPoule(PouleNumber.B);
			result.add(pouleB);
		}
		if ((nb == 9) || (nb == 11) || (nb == 13) || (nb == 14)) {
			final CompetitorPoule pouleC = new CompetitorPoule(PouleNumber.C);
			result.add(pouleC);
		}
		if ((nb == 10) || (nb == 12) || (nb == 15) || (nb == 16)) {
			final CompetitorPoule pouleC = new CompetitorPoule(PouleNumber.C);
			result.add(pouleC);
		}
		if ((nb == 14) || (nb == 15)) {
			final CompetitorPoule pouleD = new CompetitorPoule(PouleNumber.D);
			result.add(pouleD);
		}
		if ((nb == 16) || (nb == 13)) {
			final CompetitorPoule pouleD = new CompetitorPoule(PouleNumber.D);
			result.add(pouleD);
		}

		final Competitor[] comps = competitors.sortByClub();
		if (result.isEmpty()) {
			return result;
		}
		for (int idx = 0; idx < comps.length;) {

			for (final CompetitorPoule poule : result) {
				if (idx < comps.length) {
					poule.addCompetitor(comps[idx++]);
				}
			}
		}

		return result;
	}

	public LinkedList<CompetitorGroup> createGroups(final CompetitorList competitorList,
			final int maxCompetitorsPerGroup) {
		final LinkedList<CompetitorGroup> groups = new LinkedList<CompetitorGroup>();

		CompetitorList tmp = new CompetitorList();
		float maxAutorizedWeight = getMinWeight(competitorList) + OjtConstants.WEIGHT_DELTA_IN_PURCENT / 100
				* getMinWeight(competitorList);
		int idx = 0;

		for (final Competitor comp : competitorList.sortByWeight()) {
			if (comp.getWeight() <= maxAutorizedWeight) {
				tmp.add(comp);
				idx++;
				if (idx >= maxCompetitorsPerGroup) {
					if (!tmp.isEmpty()) {
						groups.add(new CompetitorGroup(tmp, this));
					}
					idx = 0;
					/**
					 * 30 avr. 08 (DwarfConan) : C'est pas bien de travailler
					 * avec la même instance de liste.
					 */
					// tmp.clear();
					tmp = new CompetitorList();
				}
			} else {
				if (!tmp.isEmpty()) {
					groups.add(new CompetitorGroup(tmp, this));
				}
				idx = 0;
				/**
				 * 30 avr. 08 (DwarfConan) : C'est pas bien de travailler avec
				 * la même instance de liste.
				 */
				// tmp.clear();
				tmp = new CompetitorList();

				tmp.add(comp);
				idx++;

				maxAutorizedWeight = comp.getWeight() + OjtConstants.WEIGHT_DELTA_IN_PURCENT / 100
						* comp.getWeight();
			}
		}
		if (!tmp.isEmpty()) {
			groups.add(new CompetitorGroup(tmp, this));
		}
		return groups;
	}

}
