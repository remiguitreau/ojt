package com.ojt.balance.kern;

import com.ojt.connector.BufferTokenizer;

import java.util.LinkedList;
import java.util.List;

/**
 * @author FMo
 * @since 27 avr. 2009 (FMo) : Création
 */
public class DEBufferTokenizer implements BufferTokenizer {
	static final String END_OF_FRAME = (char) 0x0d + "\r\n";

	@Override
	public List<String> cutOnDelimiters(final String s) {
		if (s == null) {
			return null;
		}
		final List<String> result = new LinkedList<String>();
		for (final String value : s.split(END_OF_FRAME)) {
			result.add(value);
		}
		return result;
	}
}
