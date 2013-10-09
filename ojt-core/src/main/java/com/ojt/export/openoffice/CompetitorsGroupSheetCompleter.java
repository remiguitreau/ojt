/**
 * 
 */
package com.ojt.export.openoffice;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.ojt.CompetitionDescriptor;
import com.ojt.Competitor;
import com.ojt.CompetitorGroup;
import com.ojt.CompetitorPoule;
import com.ojt.CompetitorPoule.PouleNumber;
import com.ojt.export.GroupExportListener;
import com.sun.star.lang.IndexOutOfBoundsException;
import com.sun.star.sheet.XSpreadsheet;
import com.sun.star.table.XCell;
import com.sun.star.text.XText;
import com.sun.star.uno.UnoRuntime;

/**
 * Complète une feuille Excel d'une poule avec les infos des compétiteurs et de
 * la compétition.
 * @author Rémi "DwarfConan" Guitreau
 * @since 22 août 2009 : Création
 */
public class CompetitorsGroupSheetCompleter {

	private static final String COMPETITOR_CLUB_PREFIX = "%%CLUB_";

	private static final String COMPETITOR_WEIGHT_PREFIX = "%%WEIGHT_";

	private static final String COMPETITOR_NAME_PREFIX = "%%NAME_";

	private static final String DATE = "%%DATE%%";

	private static final String PERCENT_STEP = "%%PERCENT_STEP%%";

	private static final String MAX_WEIGHT = "%%MAX_WEIGHT%%";

	private static final String MIN_WEIGHT = "%%MIN_WEIGHT%%";

	private static final String COMPETITOR_NUMBER = "%%COMPETITOR_NUMBER%%";

	private static final String GROUP_NUMBER = "%%GROUP_NUMBER%%";

	private static final String TIME = "%%TIME%%";

	private static final String LOCATION = "%%LOCATION%%";

	private static final String COMPETITION_NAME = "%%COMPETITION_NAME%%";

	private final static int MAX_ROWS = 78;

	private final static int MAX_COLS = 13;

	// ---------------------------------------------------------
	// Attributs
	// ---------------------------------------------------------
	private final Logger logger = Logger.getLogger(getClass());

	private List<GroupExportListener> listeners = new LinkedList<GroupExportListener>();

	// ---------------------------------------------------------
	// Constructeur
	// ---------------------------------------------------------

	/**
	 * 
	 */
	public CompetitorsGroupSheetCompleter() {
		super();
	}

	// ---------------------------------------------------------
	// Public
	// ---------------------------------------------------------
	public void addGroupExportListener(final GroupExportListener listener) {
		listeners.add(listener);
	}

	/**
	 * Compléte une feuille Excel avec les infos des compétiteurs et de la
	 * compétition.
	 * @param sheet La feuille à compléter
	 * @param group Le groupe de compétiteurs
	 * @param groupNb Le numéro du groupe
	 * @param competitionDescriptor Les infos de la compétition
	 * @throws IndexOutOfBoundsException
	 */
	public void completeSheetWithGroup(final XSpreadsheet sheet, final CompetitorGroup group,
			final int groupNb, final CompetitionDescriptor competitionDescriptor)
			throws IndexOutOfBoundsException {
		final ArrayList<CompetitorPoule> poules = group.createPoules();
		for (int row = 0; row < MAX_ROWS; row++) {
			for (int col = 0; col < MAX_COLS; col++) {
				final XCell cell = sheet.getCellByPosition(col, row);
				final String originalText = ((XText) UnoRuntime.queryInterface(XText.class, cell)).getString();
				if ((originalText != null) && !originalText.trim().isEmpty()) {
					// Remplie les informations de la compétition
					String modifiedText = completeWithCompetitionDescriptor(originalText,
							competitionDescriptor);
					// Remplie les informations spécifiques du groupe
					modifiedText = completeWithGroupInfos(modifiedText, group, groupNb);
					// Remplie les informations des compétiteurs
					modifiedText = completeWithCompetitorsPoules(modifiedText, poules);
					if (!modifiedText.equals(originalText)) {
						cell.setFormula(modifiedText);
					}
				}
			}
		}
	}

	// ---------------------------------------------------------
	// Privées
	// ---------------------------------------------------------
	private String completeWithCompetitionDescriptor(final String cellText,
			final CompetitionDescriptor competitionDescriptor) {
		String text = cellText;
		if (text.contains(COMPETITION_NAME)) {
			text = text.replaceAll(COMPETITION_NAME, competitionDescriptor.getCompetitionName());
		}
		if (text.contains(LOCATION)) {
			text = text.replaceAll(LOCATION, competitionDescriptor.getLocation());
		}
		if (text.contains(TIME)) {
			text = text.replaceAll(TIME, competitionDescriptor.getFightTime());
		}
		if (text.contains(DATE)) {
			text = text.replaceAll(DATE, competitionDescriptor.getDate());
		}
		return text;
	}

	private String completeWithGroupInfos(final String cellText, final CompetitorGroup group,
			final int groupNb) {
		String text = cellText;
		if (text.contains(GROUP_NUMBER)) {
			text = text.replaceAll(GROUP_NUMBER, String.valueOf(groupNb));
		}
		if (text.contains(COMPETITOR_NUMBER)) {
			text = text.replaceAll(COMPETITOR_NUMBER, String.valueOf(group.getCompetitorsNumber()));
		}
		if (text.contains(MIN_WEIGHT)) {
			text = text.replaceAll(MIN_WEIGHT, String.valueOf(group.getMinGroupWeight()));
		}
		if (text.contains(MAX_WEIGHT)) {
			text = text.replaceAll(MAX_WEIGHT, String.valueOf(group.getMaxGroupWeight()));
		}
		if (text.contains(PERCENT_STEP)) {
			text = text.replaceAll(PERCENT_STEP, String.valueOf(group.getWeightStepInPercent()));
		}
		return text;
	}

	private String completeWithCompetitorsPoules(final String cellText,
			final ArrayList<CompetitorPoule> poules) {
		int pos = -1;
		if (((pos = cellText.indexOf(COMPETITOR_NAME_PREFIX)) != -1)
				|| ((pos = cellText.indexOf(COMPETITOR_CLUB_PREFIX)) != -1)) {
			// On récupère la clef du compétiteur dans la feuille.
			final String compKey = cellText.substring(pos + 7, pos + 9);
			final int compNb = Integer.parseInt(String.valueOf(compKey.charAt(1)));
			final CompetitorPoule poule = getPouleFromLetter(String.valueOf(compKey.charAt(0)), poules);
			if ((poule != null) && (poule.getCompetitors().size() >= compNb)) {
				return completeWithCompetitorInfos(cellText, compKey,
						poule.getCompetitors().get((compNb - 1)));
			}
		}
		return cellText;
	}

	private String completeWithCompetitorInfos(final String cellText, final String key,
			final Competitor competitor) {
		String text = cellText;
		if (text.contains(COMPETITOR_NAME_PREFIX + key + "%%")) {
			text = text.replaceAll(COMPETITOR_NAME_PREFIX + key + "%%", competitor.getDisplayName() + " ("
					+ String.valueOf(competitor.getWeight()) + " kg)");
			fireCompetitorExported(competitor);
		}
		if (text.contains(COMPETITOR_CLUB_PREFIX + key + "%%")) {
			text = text.replaceAll(COMPETITOR_CLUB_PREFIX + key + "%%", competitor.getClub().getClubName());
		}
		return text;
	}

	private CompetitorPoule getPouleFromLetter(final String letter, final ArrayList<CompetitorPoule> poules) {
		for (final CompetitorPoule poule : poules) {
			if (poule.getPouleNumber().equals(PouleNumber.valueOf(letter))) {
				return poule;
			}
		}
		return null;
	}
	
	private void fireCompetitorExported(final Competitor competitor) {
		for(final GroupExportListener listener : listeners) {
			if (listener != null) {
				listener.competitorExported(competitor);
			}
		}
	}
}
