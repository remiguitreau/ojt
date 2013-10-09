/**
 * 
 */
package com.ojt.export.openoffice;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import com.ojt.CompetitionDescriptor;
import com.ojt.Competitor;
import com.ojt.CompetitorGroup;
import com.ojt.export.CompetitionStatistics;
import com.ojt.export.GroupExportListener;
import com.sun.star.beans.Property;
import com.sun.star.beans.PropertyVetoException;
import com.sun.star.beans.UnknownPropertyException;
import com.sun.star.beans.XPropertySet;
import com.sun.star.container.NoSuchElementException;
import com.sun.star.lang.IllegalArgumentException;
import com.sun.star.lang.IndexOutOfBoundsException;
import com.sun.star.lang.WrappedTargetException;
import com.sun.star.sheet.XSpreadsheet;
import com.sun.star.table.XCell;
import com.sun.star.text.XText;
import com.sun.star.uno.UnoRuntime;

/**
 * Complète la feuille statistiques
 *
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
	
	//---------------------------------------------------------
	// Attributs
	//---------------------------------------------------------
	private int statsTypeColIndex = -1;
	
	private int statsTypeRowIndex = -1;
	
	private final CompetitionStatistics competitionStatistics = new CompetitionStatistics();
	
	private final XSpreadsheet statistiquesSheet;
	
	private List<String> statsDifferentCellProperties = null;
	
	private List<String> titleDifferentCellProperties = null;
	
	private XCell statsCell;

	private int currentRow;
	
	public StatsSheetCompleter(final XSpreadsheet statsSheet) {
		super();
		
		this.statistiquesSheet = statsSheet;
	}
	
	//---------------------------------------------------------
	// Public
	//---------------------------------------------------------

	public void generate(final CompetitionDescriptor competitionDescriptor) throws NoSuchElementException, WrappedTargetException, IndexOutOfBoundsException, RowsExceededException, WriteException, UnknownPropertyException, PropertyVetoException, IllegalArgumentException {
		
		for (int row = 0; row < MAX_ROWS; row++) {
			for (int col = 0; col < MAX_COLS; col++) {
				final XCell cell = statistiquesSheet.getCellByPosition(col, row);
				final XText xCellText = (com.sun.star.text.XText) UnoRuntime.queryInterface(XText.class, cell);
				if ((xCellText.getText() != null) && !xCellText.getString().trim().isEmpty()) {
					final String originalText = xCellText.getString();
					// Remplie les informations de la compétition
					String modifiedText = completeWithCompetitionDescriptor(originalText,
							competitionDescriptor);
					if (!modifiedText.equals(originalText)) {
						cell.setFormula(modifiedText);
					}
					else {
						if(isStatsRow(originalText)) {
							statsTypeRowIndex = row;
							statsTypeColIndex = col;
						}
					}
				}
			}
		}
		
		currentRow = statsTypeRowIndex+2;
		if(statsTypeRowIndex > -1) {
			statsCell = statistiquesSheet.getCellByPosition(statsTypeColIndex, statsTypeRowIndex);
		}
		
		appendStatistics("Départements", competitionStatistics.getCompetitorsNumber(), competitionStatistics.getCounterPerDepartment(), true);
		appendStatistics("Clubs", competitionStatistics.getCompetitorsNumber(), competitionStatistics.getCounterPerClub(), false);
	}
	
	private void appendStatistics(final String name, final int nbCompetitors, final Map<String, Integer> counters, final boolean first) throws IndexOutOfBoundsException, UnknownPropertyException, PropertyVetoException, IllegalArgumentException, WrappedTargetException {
		if(first) {
			statsCell.setFormula(name);
		}
		else {
			final XCell cell = statistiquesSheet.getCellByPosition(statsTypeColIndex, currentRow);
			if(titleDifferentCellProperties == null) {
				titleDifferentCellProperties = copyCellTypeAndGetDifferents(statsCell, cell);
			}
			else {
				copyOnlyDifferentsCellType(statsCell, cell, titleDifferentCellProperties);
			}
			cell.setFormula(name);
			final XCell title1 = statistiquesSheet.getCellByPosition(statsTypeColIndex+1, statsTypeRowIndex);
			final XCell cell1 = statistiquesSheet.getCellByPosition(statsTypeColIndex+1, currentRow);
			copyOnlyDifferentsCellType(title1, cell1, titleDifferentCellProperties);
			cell1.setFormula(((XText) UnoRuntime.queryInterface(XText.class, title1)).getString());
			final XCell title2 = statistiquesSheet.getCellByPosition(statsTypeColIndex+2, statsTypeRowIndex);
			final XCell cell2 = statistiquesSheet.getCellByPosition(statsTypeColIndex+2, currentRow);
			copyOnlyDifferentsCellType(title2, cell2, titleDifferentCellProperties);
			cell2.setFormula(((XText) UnoRuntime.queryInterface(XText.class, title2)).getString());
			currentRow++;
		}
		appendStatistic("Total", competitionStatistics.getCompetitorsNumber(), 100, first);
		for(final Entry<String, Integer> entry : counters.entrySet()) {
			appendStatistic(entry.getKey(), entry.getValue().intValue(), (entry.getValue().intValue()* 100 / competitionStatistics.getCompetitorsNumber()), false);	
		}
		currentRow++;
	}
	
	private void appendStatistic(final String name, final int nbCompetitors, final int percent, final boolean first) throws IndexOutOfBoundsException, UnknownPropertyException, PropertyVetoException, IllegalArgumentException, WrappedTargetException {
		if(first) {
			statistiquesSheet.getCellByPosition(statsTypeColIndex, statsTypeRowIndex+1).setFormula(name);
			statistiquesSheet.getCellByPosition(statsTypeColIndex+1, statsTypeRowIndex+1).setFormula(String.valueOf(nbCompetitors));
			statistiquesSheet.getCellByPosition(statsTypeColIndex+2, statsTypeRowIndex+1).setFormula("'"+percent + " %");
		}
		else {
			final XCell refCell = statistiquesSheet.getCellByPosition(statsTypeColIndex, statsTypeRowIndex+1);
			final XCell nameCell = statistiquesSheet.getCellByPosition(statsTypeColIndex, currentRow);
			if(statsDifferentCellProperties == null) {
				statsDifferentCellProperties = copyCellTypeAndGetDifferents(refCell, nameCell);
			}
			else {
				copyOnlyDifferentsCellType(refCell, nameCell, statsDifferentCellProperties);
			}
			nameCell.setFormula(name);
			final XCell cell1 = statistiquesSheet.getCellByPosition(statsTypeColIndex+1, currentRow);
			copyOnlyDifferentsCellType(refCell, cell1, statsDifferentCellProperties);
			cell1.setFormula(String.valueOf(nbCompetitors));
			final XCell cell2 = statistiquesSheet.getCellByPosition(statsTypeColIndex+2, currentRow);
			copyOnlyDifferentsCellType(refCell, cell2, statsDifferentCellProperties);
			cell2.setFormula("'"+percent+" %");
			currentRow++;
		}
	}
	
	//---------------------------------------------------------
	// Implémentation de GroupExportListener
	//---------------------------------------------------------
	@Override
	public void competitorExported(final Competitor competitor) {
		
	}
	
	@Override
	public void exportFinished() {
		
	}
	
	@Override
	public void groupExportBegin(final String groupName, final CompetitorGroup group,
			final int groupNumber) {
		competitionStatistics.addGroup(group);
	}
	
	//---------------------------------------------------------
	// Privées
	//---------------------------------------------------------
	
	private List<String> copyCellTypeAndGetDifferents(final XCell cell1, final XCell cell2) throws UnknownPropertyException, PropertyVetoException, IllegalArgumentException, WrappedTargetException {
		final List<String> differentsProperties = new LinkedList<String>();
		final XPropertySet set1 = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class, cell1);
		final XPropertySet set2 = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class, cell2);
		for(Property prop : set1.getPropertySetInfo().getProperties()) {
			if(!set1.getPropertyValue(prop.Name).equals(set2.getPropertyValue(prop.Name))) {
				set2.setPropertyValue(prop.Name, set1.getPropertyValue(prop.Name));
				differentsProperties.add(prop.Name);
			}
		}
		return differentsProperties;
	}
	
	private void copyOnlyDifferentsCellType(final XCell cell1, final XCell cell2,final List<String> differentsProperties) throws UnknownPropertyException, PropertyVetoException, IllegalArgumentException, WrappedTargetException {
		final XPropertySet set1 = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class, cell1);
		final XPropertySet set2 = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class, cell2);
		for(final String prop : differentsProperties) {
			set2.setPropertyValue(prop, set1.getPropertyValue(prop));
		}
	}
	
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
