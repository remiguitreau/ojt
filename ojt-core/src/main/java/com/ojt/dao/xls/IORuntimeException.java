package com.ojt.dao.xls;

import java.io.IOException;

import com.ojt.OJTException;

public class IORuntimeException extends OJTException {

	public IORuntimeException(final IOException ex) {
		super(ex);
	}

}
