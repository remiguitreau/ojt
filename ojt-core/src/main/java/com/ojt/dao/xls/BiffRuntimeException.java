package com.ojt.dao.xls;

import jxl.read.biff.BiffException;

import com.ojt.OJTException;

public class BiffRuntimeException extends OJTException {

	public BiffRuntimeException(final BiffException ex) {
		super(ex);
	}

}
