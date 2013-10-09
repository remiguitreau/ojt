package com.ojt;

import com.ojt.algo.Algorithme;
import com.ojt.algo.ClubComparator;
import com.ojt.algo.WeightComparator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class CompetitorList extends ArrayList<Competitor> {
	
	private final WeightComparator weightComparator = new WeightComparator();
	private final ClubComparator clubComparator = new ClubComparator();
	private LinkedList<CompetitorGroup> groups;
	private int maxCompetitorsPerGroup = OjtConstants.MAX_COMPETITOR_PER_GROUP;
	
	
	public CompetitorList() {
		super();
	}

	public CompetitorList(List<Competitor> list) {
		super(list);
	}

	public Competitor[] sortByWeight() {
		Competitor[] toSort = this.toArray(new Competitor[0]);
		Arrays.sort(toSort, weightComparator);
		return toSort;
	}

	public Competitor[] sortByClub() {
		Competitor[] toSort = this.toArray(new Competitor[0]);
		Arrays.sort(toSort, clubComparator);
		return toSort;
	}

	public List<CompetitorGroup> createGroups(Algorithme algo) {
		groups = algo.createGroups(this, maxCompetitorsPerGroup);
		return groups;
	}

	public List<CompetitorGroup> getGroups(Algorithme algo) {
		return groups;
	}

	public void setMaxCompetitorsPerGroup(int maxCompetitorsPerGroup) {
		this.maxCompetitorsPerGroup = maxCompetitorsPerGroup;
	}

}
