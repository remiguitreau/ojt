package com.ojt.balance.kern;

import com.ojt.OJTException;

/**
 * @author FMo
 * @since 20 avr. 2009 (FMo) : Création
 */
public class BadFrameException extends OJTException {

	public BadFrameException(final String msg) {
		super(msg);
	}

	public BadFrameException(final NumberFormatException ex) {
		super(ex);
	}

}
