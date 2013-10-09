package com.ojt.export.xlspoi;

import org.apache.poi.hssf.usermodel.HSSFCell;

public class XlsPoiUtilities {

	public final static String getCellValueAsString(final HSSFCell cell) {
		if(cell != null) {
			switch (cell.getCellType()) {
				case HSSFCell.CELL_TYPE_BLANK:
					return "";
				case HSSFCell.CELL_TYPE_BOOLEAN:
					return String.valueOf(cell.getBooleanCellValue());
				case HSSFCell.CELL_TYPE_ERROR:
					return null;
				case HSSFCell.CELL_TYPE_FORMULA:
					return cell.getCellFormula();
				case HSSFCell.CELL_TYPE_NUMERIC:
					return String.valueOf(cell.getNumericCellValue());
				case HSSFCell.CELL_TYPE_STRING:
					return cell.getRichStringCellValue() == null ? "" : cell.getRichStringCellValue().getString();
			}
		}
		return "";
	}
}
