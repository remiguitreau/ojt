package com.ojt;

public enum GradeBelt {
	WHITE_YELLOW("BJ","BJ"),
	YELLOW("J","J"),
	YELLOW_ORANGE("JO","JO"),
	ORANGE("O","O"),
	ORANGE_GREEN("OV","OV"),
	GREEN("V","V"),
	BLUE("B", "B"),
	BROWN("M","M"),
	FIRST_DAN("1","1D"),
	SECOND_DAN("2","2D"),
	THIRD_DAN("3","3D"),
	FOURTH_DAN("4","4D"),
	FIFTH_DAN("5","5D"),
	SIXTH_DAN("6","6D");
	
	public final String code;
	
	public final String abbreviation;
	
	private GradeBelt(final String code, final String abbreviation) {
		this.code=code;
		this.abbreviation=abbreviation;
	}

	public static GradeBelt retrieveGradeFromString(
			String extractStringValue) {
		if(extractStringValue == null || extractStringValue.isEmpty()) {
			return null;
		}
		for(final GradeBelt belt : GradeBelt.values()) {
			if(belt.code.equals(extractStringValue)) {
				return belt;
			}
		}
		return null;
	}
}
