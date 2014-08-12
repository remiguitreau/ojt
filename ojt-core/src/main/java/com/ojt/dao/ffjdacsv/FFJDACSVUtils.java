package com.ojt.dao.ffjdacsv;

public final class FFJDACSVUtils {
	
	public static String extractStringValue(final String value) {
		return value.replaceAll("\"", "");
	}

}
