package com.ojt.balance.kern;

import com.ojt.balance.BalanceDecoder;
import com.ojt.balance.BalanceFrame;

/**
 * @author FMo
 * @since 20 avr. 2009 (FMo) : Création
 */
public class DEDecoder implements BalanceDecoder {

	@Override
	public BalanceFrame decode(final String rawFrame) {
		if (rawFrame == null) {
			throw new IllegalArgumentException();
		}
		final String datas = rawFrame.replaceAll("N:", "");

		final DEFrame frame = new DEFrame(BalanceFrame.TYPE.WEIGHT);
		final String[] filds = datas.trim().split(" ");
		if (filds.length < 1) {
			throw new BadFrameException("Mauvais format de trame : " + datas);
		}
		String data = filds[0];
		data = data.replaceAll(",", ".");
		try {
			frame.setWeight(Float.valueOf(data));
		} catch (final NumberFormatException ex) {
			throw new BadFrameException(ex);
		}
		if ((filds.length > 1) && "Kg".equalsIgnoreCase(filds[1])) {
			frame.setStableWeight(true);
		} else {
			frame.setStableWeight(false);
		}
		return frame;
	}

}
