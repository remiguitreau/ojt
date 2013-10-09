package com.ojt;

import com.ojt.algo.Algorithme;

import java.util.ArrayList;

public class CompetitorGroup {

	private final CompetitorCategory groupCategory;

	private final CompetitorList competitors;

	private final Algorithme algorithmes;
	
	private boolean savedForAnotherWeighing;

	public CompetitorGroup(final CompetitorList competitors, final Algorithme algorithmes) {
		this.competitors = competitors;
		this.algorithmes = algorithmes;
		if ((competitors == null) || competitors.isEmpty()
				|| (competitors.size() > OjtConstants.MAX_COMPETITOR_PER_GROUP)) {
			throw new IllegalArgumentException("competitor list must be not empty and 16 compititor maximum");
		}

		groupCategory = algorithmes.getCategory(competitors);
	}

	public CompetitorGroup(final CompetitorGroup group) {
		this.competitors = new CompetitorList(group.competitors);
		this.algorithmes = group.algorithmes;
		this.groupCategory = group.groupCategory;
	}

	public CompetitorList getCompetitors() {
		return competitors;
	}

	public float getMinGroupWeight() {
		return algorithmes.getMinWeight(competitors);
	}

	public float getMaxGroupWeight() {
		return algorithmes.getMaxWeight(competitors);
	}

	public CompetitorCategory getGroupCategory() {
		return groupCategory;
	}

	public ArrayList<CompetitorPoule> createPoules() {
		return algorithmes.getPoules(competitors);
	}
	
	public void setSavedForAnotherWeighing(final boolean savedForAnotherWeighing) {
		this.savedForAnotherWeighing = savedForAnotherWeighing;
	}
	
	public boolean isSavedForAnotherWeighing() {
		return savedForAnotherWeighing;
	}

	@Override
	public String toString() {
		if (OjtConstants.DEBUG) {
			return toDetailedString();
		}
		return toSimpleString();
	}

	public String getName() {
		final StringBuffer sb = new StringBuffer(200);
		sb.append(groupCategory);
		sb.append(" (");
		sb.append(getMinGroupWeight());
		sb.append(" : ");
		sb.append(getMaxGroupWeight());
		sb.append(") kg");
		return sb.toString();
	}

	public int getCompetitorsNumber() {
		return competitors.size();
	}

	public String getNameForSheet(final boolean withDecimal) {
		final StringBuffer sb = new StringBuffer(200);
		sb.append("judokas");
		sb.append(" ");
		if (withDecimal) {
			sb.append(getMinGroupWeight());
			sb.append(" à ");
			sb.append(getMaxGroupWeight());
		} else {
			sb.append((int) getMinGroupWeight());
			sb.append(" à ");
			sb.append((int) getMaxGroupWeight());
		}
		sb.append(" kg");
		return sb.toString();
	}

	public String toSimpleString() {
		final StringBuffer sb = new StringBuffer(200);
		sb.append(getName());
		sb.append(" : ");
		sb.append(createPoules().size());
		sb.append(" poules");
		return sb.toString();
	}

	public String toDetailedString() {
		final StringBuffer sb = new StringBuffer(200);
		sb.append(toSimpleString());
		sb.append("\r\n");
		for (final CompetitorPoule poule : createPoules()) {
			sb.append(poule);
			sb.append("\r\n");
		}
		return sb.toString();
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof CompetitorGroup) {
			final CompetitorGroup group = (CompetitorGroup) obj;
			if (getMinGroupWeight() != group.getMinGroupWeight()) {
				return false;
			}
			if (getMaxGroupWeight() != group.getMaxGroupWeight()) {
				return false;
			}
			if (groupCategory != group.groupCategory) {
				return false;
			}
			return true;
		}
		return false;
	}

	public int getWeightStepInPercent() {
		return (int) ((getMaxGroupWeight() - getMinGroupWeight()) / getMinGroupWeight() * 100.0);
	}

}
