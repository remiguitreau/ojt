package com.ojt.dao.xls;

import com.ojt.Club;

public class BadEntry {

	private int rowNumber;
	private String name;
	private Club club;
	private String weigtValue;

	public BadEntry(int rowNumber, String name, Club club, String weigtValue) {
		this.rowNumber = rowNumber;
		this.name = name;
		this.club = club;
		this.weigtValue = weigtValue;
	}

	@Override
	public String toString(){
		return ""+(rowNumber + 1)+"     -   "+ name + " ("+club+") : "+weigtValue;
	}
}
