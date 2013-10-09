package com.ojt;

import java.util.ArrayList;
import java.util.Arrays;

import com.ojt.algo.ClubComparator;


public class CompetitorPoule {
	
	public enum PouleNumber {
		A,
		B,
		C,
		D
	}
	
	private final ClubComparator clubComparator = new ClubComparator();

	private final PouleNumber pouleNumber;
	private ArrayList<Competitor> competitors;
	
	public CompetitorPoule(PouleNumber pouleNumber, ArrayList<Competitor> competitors) {
		this.pouleNumber = pouleNumber;
		this.competitors = competitors;
	}

	public CompetitorPoule(PouleNumber pouleNumber) {
		this.pouleNumber = pouleNumber;
	}

	public ArrayList<Competitor> getCompetitors() {
		return competitors;
	}
	
	public Competitor[] sortByClub() {
		final int pouleSize = getPouleSize();
		final CompetitorList comps = new CompetitorList(competitors);
		
		final Competitor[] toSort = competitors.toArray(new Competitor[0]);
		Arrays.sort(toSort, clubComparator);

		Competitor[] result = new Competitor[pouleSize];
		int idx1 = 0;
		int idx2 = 1;
		int idxToInsert = 0;
		while(idx2 < toSort.length){
			if (toSort[idx1].getClub().getClubCode().equals(toSort[idx2].getClub().getClubCode())){
				result[idxToInsert] = toSort[idx1];
				result[idxToInsert+1] = toSort[idx2];
				comps.remove(toSort[idx1]);
				comps.remove(toSort[idx2]);
				idx1 += 2; 
				idx2 += 2;
				if (pouleSize  < 5) {
					idxToInsert += 2;
				} else {
					idxToInsert += 3;
				}
			} else {
				idx1++;
				idx2++;
			}
		}
		
		
		if (pouleSize == 5 && idxToInsert >= 3) {
			result[2] = comps.remove(0);
		}
		
		for (final Competitor comp : comps){
			if (idxToInsert < result.length) {
				result[idxToInsert++] = comp;
			}
		}
		return result;
	}

	public void setCompetitors(ArrayList<Competitor> competitors) {
		this.competitors = competitors;
	}

	public int getPouleSize() {
		return competitors.size();
	}

	public PouleNumber getPouleNumber() {
		return pouleNumber;
	}

	public void addCompetitor(Competitor competitor) {
		if (competitors == null) {
			competitors = new ArrayList<Competitor>();
		}
		competitors.add(competitor);
	}
	
	// -------------------------------------------------------------------------
	// 
	// -------------------------------------------------------------------------
	@Override
	public String toString() {		
		StringBuffer sb = new StringBuffer();
		sb.append("P" + getPouleSize()+ "-" + pouleNumber+" ");
		sb.append(competitors);
		return sb.toString(); 
	}

	@Override
	public boolean equals(Object obj) {
		if (super.equals(obj) ) {
			return true;
		}
		if (obj instanceof CompetitorPoule) {
			final CompetitorPoule poule = (CompetitorPoule)obj;
			if (getPouleSize() != poule.getPouleSize()) {
				return false;
			}
			if (pouleNumber != poule.pouleNumber) {
				return false;
			}
			if (!competitors.equals(poule.competitors)) {
				return false;
			}
			return true;
		}
		return false;
	}
	
}
