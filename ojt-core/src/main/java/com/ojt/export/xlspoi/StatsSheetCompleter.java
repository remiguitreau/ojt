/**
 * 
 */
package com.ojt.export.xlspoi;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;

import com.ojt.CompetitionDescriptor;
import com.ojt.Competitor;
import com.ojt.CompetitorGroup;
import com.ojt.export.CompetitionStatistics;
import com.ojt.export.GroupExportListener;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Complète la feuille statistiques
 * @author Rémi "DwarfConan" Guitreau
 * @since 21 oct. 2009 : Création
 */
public class StatsSheetCompleter implements GroupExportListener {

	private final static int MAX_ROWS = 10;

	private final static int MAX_COLS = 4;

	private static final String DATE = "%%DATE%%";

	private static final String LOCATION = "%%LOCATION%%";

	private static final String COMPETITION_NAME = "%%COMPETITION_NAME%%";

	private static final String STATS_TYPE = "%%STATS_TYPE%%";

	// ---------------------------------------------------------
	// Attributs
	// ---------------------------------------------------------
	private int statsTypeColIndex = -1;

	private int statsTypeRowIndex = -1;

	private final CompetitionStatistics competitionStatistics = new CompetitionStatistics();

	private final HSSFSheet statistiquesSheet;

	private final List<String> statsDifferentCellProperties = null;

	private final List<String> titleDifferentCellProperties = null;

	private HSSFCell statsCell;

	private int currentRow;

	public StatsSheetCompleter(final HSSFSheet statsSheet) {
		super();

		this.statistiquesSheet = statsSheet;
	}

	// ---------------------------------------------------------
	// Public
	// ---------------------------------------------------------

	public void generate(final CompetitionDescriptor competitionDescriptor) {
		if(statistiquesSheet != null) {
			for (int row = 0; row < MAX_ROWS; row++) {
				final HSSFRow xlsRow = statistiquesSheet.getRow(row);
				if (xlsRow != null) {
					for (int col = 0; col < MAX_COLS; col++) {
						final HSSFCell cell = xlsRow.getCell(col);
						if (cell != null) {
							final String originalText = XlsPoiUtilities.getCellValueAsString(cell);
							if ((originalText != null) && !originalText.trim().isEmpty()) {
								// Remplie les informations de la compétition
								final String modifiedText = completeWithCompetitionDescriptor(originalText,
										competitionDescriptor);
								if (!modifiedText.equals(originalText)) {
									cell.setCellValue(new HSSFRichTextString(modifiedText));
								} else {
									if (isStatsRow(originalText)) {
										statsTypeRowIndex = row;
										statsTypeColIndex = col;
									}
								}
							}
						}
					}
				}
			}
	
			currentRow = statsTypeRowIndex + 2;
			if (statsTypeRowIndex > -1) {
				statsCell = statistiquesSheet.getRow(statsTypeRowIndex).getCell(statsTypeColIndex);
			}
	
			appendStatistics("Départements", competitionStatistics.getCompetitorsNumber(),
					competitionStatistics.getCounterPerDepartment(), true);
			appendStatistics("Clubs", competitionStatistics.getCompetitorsNumber(),
					competitionStatistics.getCounterPerClub(), false);
		}
	}

	private void appendStatistics(final String name, final int nbCompetitors,
			final Map<String, Integer> counters, final boolean first) {
		if (first) {
			statsCell.setCellValue(new HSSFRichTextString(name));
		} else {
			final HSSFRow row = statistiquesSheet.createRow(currentRow);
			final HSSFCell cell = row.createCell(statsTypeColIndex);
			cell.setCellStyle(statsCell.getCellStyle());
			cell.setCellValue(new HSSFRichTextString(name));
			final HSSFCell title1 = statistiquesSheet.getRow(statsTypeRowIndex).getCell(statsTypeColIndex + 1);
			final HSSFCell cell1 = row.createCell(statsTypeColIndex + 1);
			cell1.setCellStyle(title1.getCellStyle());
			cell1.setCellValue(new HSSFRichTextString(XlsPoiUtilities.getCellValueAsString(title1)));
			final HSSFCell title2 = statistiquesSheet.getRow(statsTypeRowIndex).getCell(statsTypeColIndex + 1);
			final HSSFCell cell2 = row.createCell(statsTypeColIndex + 1);
			cell2.setCellStyle(title2.getCellStyle());
			cell2.setCellValue(new HSSFRichTextString(XlsPoiUtilities.getCellValueAsString(title2)));
			currentRow++;
		}
		appendStatistic("Total", competitionStatistics.getCompetitorsNumber(), 100, first);
		for (final Entry<String, Integer> entry : counters.entrySet()) {
			appendStatistic(entry.getKey(), entry.getValue().intValue(),
					(entry.getValue().intValue() * 100 / competitionStatistics.getCompetitorsNumber()), false);
		}
		currentRow++;
	}

	private void appendStatistic(final String name, final int nbCompetitors, final int percent,
			final boolean first) {
		if (first) {
			statistiquesSheet.getRow(statsTypeRowIndex + 1).getCell(statsTypeColIndex).setCellValue(
					new HSSFRichTextString(name));
			statistiquesSheet.getRow(statsTypeRowIndex + 1).getCell(statsTypeColIndex + 1).setCellValue(
					new HSSFRichTextString(String.valueOf(nbCompetitors)));
			statistiquesSheet.getRow(statsTypeRowIndex + 1).getCell(statsTypeColIndex + 2).setCellValue(
					new HSSFRichTextString(percent + " %"));
		} else {
			final HSSFCell refCell = statistiquesSheet.getRow(statsTypeRowIndex + 1).getCell(
					statsTypeColIndex);
			final HSSFRow row = statistiquesSheet.createRow(currentRow);
			final HSSFCell nameCell = row.createCell(statsTypeColIndex);
			nameCell.setCellStyle(refCell.getCellStyle());
			nameCell.setCellValue(new HSSFRichTextString(name));
			final HSSFCell cell1 = row.createCell(statsTypeColIndex + 1);
			cell1.setCellStyle(refCell.getCellStyle());
			cell1.setCellValue(new HSSFRichTextString(String.valueOf(nbCompetitors)));
			final HSSFCell cell2 = row.createCell(statsTypeColIndex + 2);
			cell2.setCellStyle(refCell.getCellStyle());
			cell2.setCellValue(new HSSFRichTextString(percent + " %"));
			currentRow++;
		}
	}

	// ---------------------------------------------------------
	// Implémentation de GroupExportListener
	// ---------------------------------------------------------
	@Override
	public void competitorExported(final Competitor competitor) {

	}

	@Override
	public void exportFinished() {

	}

	@Override
	public void groupExportBegin(final String groupName, final CompetitorGroup group, final int groupNumber) {
		competitionStatistics.addGroup(group);
	}

	// ---------------------------------------------------------
	// Privées
	// ---------------------------------------------------------

	private boolean isStatsRow(final String cellText) {
		return cellText.contains(STATS_TYPE);
	}

	private String completeWithCompetitionDescriptor(final String cellText,
			final CompetitionDescriptor competitionDescriptor) {
		String text = cellText;
		if (text.contains(COMPETITION_NAME)) {
			text = text.replaceAll(COMPETITION_NAME, competitionDescriptor.getCompetitionName());
		}
		if (text.contains(LOCATION)) {
			text = text.replaceAll(LOCATION, competitionDescriptor.getLocation());
		}
		if (text.contains(DATE)) {
			text = text.replaceAll(DATE, competitionDescriptor.getDate());
		}
		return text;
	}
}
