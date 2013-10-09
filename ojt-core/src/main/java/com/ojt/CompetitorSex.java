package com.ojt;

public enum CompetitorSex {
	MALE ("Masculin"),
	FEMALE ("Féminin"),
	UNKNOWN("Inconnnu");
	
	private final String humanName;

	private CompetitorSex(final String humanName) {
		this.humanName = humanName;
	}
	
	public String getHumanName() {
		return humanName;
	}

	public static CompetitorSex retrieveSexFromString(String humanName) {
		for(final CompetitorSex sex : CompetitorSex.values()) {
			if(humanName.equals(sex.getHumanName())) {
				return sex;
			}
		}
		return UNKNOWN;
	}
}
