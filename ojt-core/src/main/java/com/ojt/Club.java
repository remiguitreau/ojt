package com.ojt;

public class Club {
	private String clubCode;

	private String department;

	private String clubName;

	public Club() {
	}

	public Club(final String clubCode, final String department, final String clubName) {
		super();
		this.clubCode = clubCode;
		this.department = department;
		this.clubName = clubName;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(final String department) {
		this.department = department;
	}

	public String getClubName() {
		return clubName;
	}

	public void setClubName(final String clubName) {
		this.clubName = clubName;
	}

	public String getClubCode() {
		return clubCode;
	}

	public void setClubCode(final String clubCode) {
		this.clubCode = clubCode;
	}

	@Override
	public String toString() {
		return clubCode + " (" + department + "-" + clubName + ")";
	}

	public String toString2() {
		return clubName + "- " + department + " (" + clubCode + ")";
	}

}
