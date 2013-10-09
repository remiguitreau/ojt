package com.ojt.balance.kern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.ojt.balance.BalanceFrame;

public class DEDecoderTest {

	@Test
	public void testDecode() {
		final byte[] dataOK = new byte[] { ' ', ' ', ' ', ' ', ' ', ' ', ' ', '7', '7', '.', '5', '0', ' ',
				'K', 'g', '\n', '\r', };
		final byte[] dataOK3 = new byte[] { 'N', ':', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '7', '7', '.', '5',
				'0', ' ', 'K', 'g', '\n', '\r', };
		final byte[] dataOK2 = new byte[] { ' ', ' ', ' ', ' ', ' ', ' ', ' ', '7', '7', ',', '5', '0', ' ',
				'K', 'g', '\n', '\r' };
		final byte[] data0 = new byte[] { 'd', 'g', ' ', ' ', ' ', ' ', ' ', '7', '7', '.', '5', '0', ' ',
				'K', 'g', '\n', '\r' };
		final byte[] data1 = new byte[] { ' ', ' ', ' ', ' ', ' ', ' ', ' ', '7', '7', '.', '5', '0' };
		final byte[] data2 = new byte[] { 'b', 'o', 'n', 'j', 'o', 'u', 'r' };

		final DEDecoder decoder = new DEDecoder();
		try {
			decoder.decode(null);
			fail("Une exception aurait du sortir");
		} catch (final IllegalArgumentException ex) {
		}

		BalanceFrame frame = decoder.decode(new String(dataOK));
		assertEquals(77.5, frame.getWeight(), 0.000001);
		assertTrue(frame.isStableWeight());

		frame = decoder.decode(new String(dataOK3));
		assertEquals(77.5, frame.getWeight(), 0.000001);
		assertTrue(frame.isStableWeight());

		frame = decoder.decode(new String(dataOK2));
		assertEquals(77.5, frame.getWeight(), 0.000001);
		assertTrue(frame.isStableWeight());

		try {
			decoder.decode(new String(data0));
			fail("Une exception aurait du sortir");
		} catch (final BadFrameException ex) {
		}

		frame = decoder.decode(new String(data1));
		assertEquals(77.5, frame.getWeight(), 0.000001);
		assertFalse(frame.isStableWeight());
		try {
			decoder.decode(new String(data2));
			fail("Une exception aurait du sortir");
		} catch (final BadFrameException ex) {
		}

	}

}
