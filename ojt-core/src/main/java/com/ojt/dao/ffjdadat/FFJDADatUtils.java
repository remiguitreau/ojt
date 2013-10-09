package com.ojt.dao.ffjdadat;

public final class FFJDADatUtils {
	
	public static String extractStringValue(final String value) {
		return value.replaceAll("\"", "");
	}

}
