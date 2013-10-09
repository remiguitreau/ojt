package com.ojt.export.xls;

import jxl.write.biff.RowsExceededException;

public class RowsExceeded extends RuntimeException {

	public RowsExceeded(final RowsExceededException ex) {
		super(ex);
	}

}
