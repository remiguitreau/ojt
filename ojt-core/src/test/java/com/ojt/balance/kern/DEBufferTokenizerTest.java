package com.ojt.balance.kern;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.util.List;

/**
 * @author FMo
 * @since 27 avr. 2009 (FMo) : Création
 */
public class DEBufferTokenizerTest {

	@Test
	public void testCutOnDelimiters() {
		final DEBufferTokenizer tok = new DEBufferTokenizer();

		String test = "       77.50 Kg" + (char) 0x0d + "\r\n       77.50 Kg" + (char) 0x0d + "\r\n";
		List<String> values = tok.cutOnDelimiters(test);
		assertEquals(2, values.size());

		test = "       77.50 Kg" + (char) 0x0d + "\r\n       77.50 Kg" + (char) 0x0d + "\r\n       77.50 Kg"
				+ (char) 0x0d + "\r\n       77.50 Kg" + (char) 0x0d + "\r\n";
		values = tok.cutOnDelimiters(test);
		assertEquals(4, values.size());

	}
}
