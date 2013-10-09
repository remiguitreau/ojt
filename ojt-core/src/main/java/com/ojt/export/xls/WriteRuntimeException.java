package com.ojt.export.xls;

import jxl.write.WriteException;

import com.ojt.OJTException;

public class WriteRuntimeException extends OJTException {

	public WriteRuntimeException(final WriteException ex) {
		super(ex);
	}

}
