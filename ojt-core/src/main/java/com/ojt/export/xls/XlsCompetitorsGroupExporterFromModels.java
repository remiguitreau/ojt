/**
 * 
 */
package com.ojt.export.xls;

import org.apache.log4j.Logger;

import com.ojt.Club;
import com.ojt.CompetitionDescriptor;
import com.ojt.Competitor;
import com.ojt.CompetitorGroup;
import com.ojt.CompetitorPoule;
import com.ojt.CompetitorPoule.PouleNumber;
import com.ojt.export.CompetitorsGroupExporter;
import com.ojt.export.GroupExportListener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.ScriptStyle;
import jxl.format.UnderlineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

/**
 * @author cedric
 */
public class XlsCompetitorsGroupExporterFromModels implements CompetitorsGroupExporter {

	// -------------------------------------------------------------------------
	// Constantes
	// -------------------------------------------------------------------------
	public final static String FFJ_LOGO_FILE_NAME = "logoffj.png";

	private static final int COMPETITOR_NUMBER_COLUMN_NUMBER = 1;

	private static final int NAME_COLUMN_NUMBER = 2;

	private static final int CLUB_COLUMN_NUMBER = 3;

	private static final int G_COLUMN_NUMBER = 4;

	private static final int DEFAULT_ROW_SIZE = 18;

	// -------------------------------------------------------------------
	// Attributs
	// -------------------------------------------------------------------
	private WritableWorkbook workbook;

	private final File exportFile;

	private final Logger logger = Logger.getLogger(getClass());

	private List<GroupExportListener> listeners = new LinkedList<GroupExportListener>();

	private final File modelsFile;

	// -------------------------------------------------------------------
	// Constructeur
	// -------------------------------------------------------------------
	public XlsCompetitorsGroupExporterFromModels(final File exportFile, final File modelsFile) {
		super();
		this.exportFile = exportFile;
		this.modelsFile = modelsFile;
	}

	@Override
	public void addGroupExportListener(final GroupExportListener listener) {
		listeners.add(listener);
	}

	@Override
	public void exportCompetitors(final List<CompetitorGroup> competitorsGroups,
			final CompetitionDescriptor comp) {
		initXls();

		int sheetNumber = 0;

		// 05.03.2009 (FMo) : ajout de la feuille de résultats
		final WritableSheet resultsSheet = createSheet("RESULTATS", sheetNumber++);
		createResultHeader(resultsSheet, comp);
		final int rowNumber = 8;
		final int groupNumber = 1;

		for (final CompetitorGroup group : competitorsGroups) {
			final String sheetName = group.getCompetitors().size() + " " + group.getNameForSheet(true)
					+ "____G" + (sheetNumber + 1);
			fireGroupExportBegin(sheetName, group, (sheetNumber+1));
			if (workbook.getSheet("GROUPE DE " + group.getCompetitorsNumber()) != null) {
				workbook.copySheet("GROUPE DE " + group.getCompetitorsNumber(), sheetName, 0);
				((Label) workbook.getSheet(sheetName).findLabelCell("%%NAME_A1%%")).setString(group.getCompetitors().get(
						0).getDisplayName());
			} else {
				logger.info("Pas de feuilles avec " + group.getCompetitorsNumber() + " combattants");
			}

			//			
			// sheet.getSettings().setOrientation(PageOrientation.LANDSCAPE);
			// createFileHeader(sheet, comp, group.getNameForSheet(true), true);
			// try {
			// exportPoules(sheet, group.createPoules());
			//
			// // 05.03.2009 (FMo) : ajout de la feuille de statistiques
			// rowNumber = updateResultsSheet(resultsSheet, group, rowNumber,
			// groupNumber++);
			// } catch (final RowsExceededException ex) {
			// throw new RowsExceeded(ex);
			// } catch (final WriteException ex) {
			// throw new WriteRuntimeException(ex);
			// }
		}

		// 05.03.2009 (FMo) : ajout de la feuille de statistiques
		final WritableSheet statsSheet = createSheet("STATISTIQUES", sheetNumber++);
		createResultHeader(statsSheet, comp);
		try {
			createStatsFile(statsSheet, comp, competitorsGroups);
		} catch (final WriteException ex) {
			throw new WriteRuntimeException(ex);
		}

		uninitXls();
		fireExportFinished();
	}

	private void fireGroupExportBegin(final String sheetName, final CompetitorGroup group, final int groupNumber) {
		for(final GroupExportListener listener : listeners) {
			if (listener != null) {
				listener.groupExportBegin(sheetName, group, groupNumber);
			}
		}
	}
	
	private void fireCompetitorExported(final Competitor competitor) {
		for(final GroupExportListener listener : listeners) {
			if (listener != null) {
				listener.competitorExported(competitor);
			}
		}
	}
	
	private void fireExportFinished() {
		for(final GroupExportListener listener : listeners) {
			if (listener != null) {
				listener.exportFinished();
			}
		}
	}
	
	private void createResultHeader(final WritableSheet sheet, final CompetitionDescriptor comp) {
		// Cre le format dune cellule
		final WritableFont fileHeaderFont = new WritableFont(WritableFont.ARIAL, 12, WritableFont.BOLD, true,
				UnderlineStyle.NO_UNDERLINE, Colour.BLUE, ScriptStyle.NORMAL_SCRIPT);
		final WritableCellFormat fileHeaderFormat = new WritableCellFormat(fileHeaderFont);

		// Ajout des cellules
		try {
			sheet.addCell(new Label(0, 0, "Manifestation : " + comp.getCompetitionName(), fileHeaderFormat));
			sheet.addCell(new Label(0, 1, "Date : " + comp.getDate(), fileHeaderFormat));
			sheet.addCell(new Label(0, 2, "Lieu : " + comp.getLocation(), fileHeaderFormat));
		} catch (final Exception e) {
			logger.error("", e);
		}

	}

	private void createStatsFile(final WritableSheet statisticSheet, final CompetitionDescriptor comp,
			final List<CompetitorGroup> competitorsGroups) throws WriteException {
		int rowNumber = 8;

		final WritableFont titleFont = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD, true,
				UnderlineStyle.NO_UNDERLINE, Colour.BLUE);
		final WritableCellFormat titleFormat = new WritableCellFormat(titleFont);
		titleFormat.setBorder(Border.ALL, BorderLineStyle.MEDIUM, Colour.BLACK);
		final WritableFont font = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD, true,
				UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
		final WritableCellFormat orderFormat = new WritableCellFormat(font);
		orderFormat.setBorder(Border.ALL, BorderLineStyle.MEDIUM, Colour.BLACK);

		int competitorsNumber = 0;
		final Map<String, Integer> counter = new HashMap<String, Integer>();
		final Map<String, Integer> counterPerDepartment = new HashMap<String, Integer>();
		for (final CompetitorGroup group : competitorsGroups) {
			competitorsNumber += group.getCompetitorsNumber();
			for (final Competitor competitor : group.getCompetitors()) {
				final Club club = competitor.getClub();

				if (counter.containsKey(club.toString2())) {
					counter.put(club.toString2(), counter.get(club.toString2()) + 1);
				} else {
					counter.put(club.toString2(), 1);
				}

				final String clubDep = club.getDepartment();
				if (counterPerDepartment.containsKey(clubDep)) {
					counterPerDepartment.put(clubDep, counterPerDepartment.get(clubDep) + 1);
				} else {
					counterPerDepartment.put(clubDep, 1);
				}
			}
		}

		statisticSheet.addCell(new Label(0, rowNumber, "Département", titleFormat));
		statisticSheet.addCell(new Label(1, rowNumber, "Nombre de judokas", titleFormat));
		statisticSheet.addCell(new Label(2, rowNumber, "Pourcentage", titleFormat));
		int clubLength = "Département".length();
		int valueLength = "Nombre de judokas".length();
		int purcentLength = "Pourcentage".length();
		rowNumber++;

		statisticSheet.addCell(new Label(0, rowNumber, "TOTAL", orderFormat));
		statisticSheet.addCell(new Label(1, rowNumber, Integer.toString(competitorsNumber), orderFormat));
		statisticSheet.addCell(new Label(2, rowNumber, "100%", orderFormat));
		rowNumber++;

		for (final String dep : counterPerDepartment.keySet()) {
			final String value = Integer.toString(counterPerDepartment.get(dep));
			final String purcent = (counterPerDepartment.get(dep) * 100 / competitorsNumber) + "%";
			statisticSheet.addCell(new Label(0, rowNumber, dep, orderFormat));
			statisticSheet.addCell(new Label(1, rowNumber, value, orderFormat));
			statisticSheet.addCell(new Label(2, rowNumber, purcent, orderFormat));

			if (dep.length() > clubLength) {
				clubLength = dep.length();
			}
			if (value.length() > valueLength) {
				valueLength = value.length();
			}
			if (purcent.length() > purcentLength) {
				purcentLength = purcent.length();
			}

			rowNumber++;
		}

		rowNumber += 2;

		statisticSheet.addCell(new Label(0, rowNumber, "Club", titleFormat));
		statisticSheet.addCell(new Label(1, rowNumber, "Nombre de judokas", titleFormat));
		statisticSheet.addCell(new Label(2, rowNumber, "Pourcentage", titleFormat));
		rowNumber++;

		statisticSheet.addCell(new Label(0, rowNumber, "TOTAL", orderFormat));
		statisticSheet.addCell(new Label(1, rowNumber, Integer.toString(competitorsNumber), orderFormat));
		statisticSheet.addCell(new Label(2, rowNumber, "100%", orderFormat));
		rowNumber++;

		for (final String club : counter.keySet()) {
			final String value = Integer.toString(counter.get(club));
			final String purcent = (counter.get(club) * 100 / competitorsNumber) + "%";
			statisticSheet.addCell(new Label(0, rowNumber, club, orderFormat));
			statisticSheet.addCell(new Label(1, rowNumber, value, orderFormat));
			statisticSheet.addCell(new Label(2, rowNumber, purcent, orderFormat));

			if (club.length() > clubLength) {
				clubLength = club.length();
			}
			if (value.length() > valueLength) {
				valueLength = value.length();
			}
			if (purcent.length() > purcentLength) {
				purcentLength = purcent.length();
			}

			rowNumber++;
		}

		// 2008.12.11 (FMo) : Modification de la taille des colonnes
		statisticSheet.setColumnView(0, clubLength + 5);
		statisticSheet.setColumnView(1, valueLength + 5);
		statisticSheet.setColumnView(2, purcentLength + 5);

	}

	private int updateResultsSheet(final WritableSheet statisticSheet, final CompetitorGroup group,
			final int rowNumber, final int groupNumber) throws RowsExceededException, WriteException {
		int rowNb = rowNumber;

		final WritableFont font = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD, true,
				UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
		final WritableCellFormat orderFormat = new WritableCellFormat(font);
		orderFormat.setBorder(Border.ALL, BorderLineStyle.MEDIUM, Colour.BLACK);

		statisticSheet.setColumnView(0, 3);

		final WritableFont fileHeaderFont = new WritableFont(WritableFont.ARIAL, 12, WritableFont.BOLD, true,
				UnderlineStyle.NO_UNDERLINE, Colour.BLUE, ScriptStyle.NORMAL_SCRIPT);
		final WritableCellFormat fileHeaderFormat = new WritableCellFormat(fileHeaderFont);
		final String groupLabel = "Groupe " + groupNumber + " : " + group.getCompetitorsNumber() + " "
				+ group.getNameForSheet(true) + " (" + group.getWeightStepInPercent() + "%)";
		statisticSheet.addCell(new Label(1, rowNb, groupLabel, fileHeaderFormat));
		rowNb++;

		int nameLength = groupLabel.length();
		int clubLength = 0;

		for (final Competitor competitor : group.getCompetitors()) {

			if (competitor.getClub().getClubName().toString().length() > clubLength) {
				clubLength = competitor.getClub().toString().length() + 5;
			}
			if (competitor.getNameAndWeight().length() > nameLength) {
				nameLength = competitor.getNameAndWeight().length();
			}

			statisticSheet.addCell(new Label(0, rowNb, "", orderFormat));
			statisticSheet.addCell(new Label(1, rowNb, competitor.getNameAndWeight(), orderFormat));
			statisticSheet.addCell(new Label(2, rowNb, competitor.getClub().getClubName() + " - "
					+ competitor.getClub().getDepartment(), orderFormat));

			rowNb++;
		}

		// 2008.12.11 (FMo) : Modification de la taille des colonnes
		statisticSheet.setColumnView(1, nameLength);
		statisticSheet.setColumnView(2, clubLength);

		return rowNb;
	}

	private void exportPoules(final WritableSheet sheet, final ArrayList<CompetitorPoule> competitorsPoules)
			throws RowsExceededException, WriteException {
		final WritableFont font = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD, true,
				UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
		final WritableCellFormat orderFormat = new WritableCellFormat(font);

		int numRow = 4;
		int nameLength = 0;
		int clubLength = 0;

		sheet.setColumnView(0, 3);

		for (final CompetitorPoule competitorPoule : competitorsPoules) {
			int numCompetitor = 1;
			createPouleTableHeader(sheet, competitorPoule.getPouleNumber(), competitorPoule.getPouleSize(),
					numRow++);
			for (final Competitor competitor : competitorPoule.sortByClub()) {
				logger.info("createPouleTableRow : " + competitor);
				createPouleTableRow(sheet, competitor, numRow, numCompetitor,
						competitorPoule.getCompetitors().size());
				if (competitor.getClub().getClubName().toString().length() > clubLength) {
					clubLength = competitor.getClub().toString().length();
				}
				if (competitor.getNameAndWeight().length() > nameLength) {
					nameLength = competitor.getNameAndWeight().length();
				}

				numCompetitor++;
				numRow++;
			}

			sheet.addCell(new Label(0, numRow, getFightOrder(competitorPoule.getPouleSize()), orderFormat));
			// 2008.12.11 (FMo) : supression de la ligne vide entre les poules
			numRow = numRow += 1;
			if (competitorsPoules.size() == 1) {
				createPouleClassification(competitorsPoules.get(0), numRow, sheet);
				// si on a une seule poule on affiche donc le classement et la
				// colonne A contient 1er et donc on doit agrandir la colonne
				sheet.setColumnView(0, 5);
			} else if (competitorsPoules.size() > 1) {
				createClassificationTree(sheet, numRow, competitorsPoules);
			}
		}

		// 2008.12.11 (FMo) : Modification de la taille des colonnes
		sheet.setColumnView(COMPETITOR_NUMBER_COLUMN_NUMBER, 3);
		sheet.setColumnView(NAME_COLUMN_NUMBER, nameLength);
		sheet.setColumnView(CLUB_COLUMN_NUMBER, clubLength);
		sheet.setColumnView(G_COLUMN_NUMBER, 3);

		// renseigne la taille des collonnes du repport de numéro
		for (int i = 5; i < 9; i++) {
			sheet.setColumnView(i, 8);
		}
		// renseigne la taille des collonnes VPC
		for (int i = 10; i < 13; i++) {
			sheet.setColumnView(i, 6);
		}
	}

	private String getFightOrder(final int pouleSize) {
		final StringBuffer sb = new StringBuffer(60);
		sb.append("Ordre des combats :  ");
		switch (pouleSize) {
			case 1:
				sb.append("");
				break;
			case 2:
				sb.append("1x2");
				break;
			case 3:
				sb.append("1x2,  2x3,  3x1");
				break;
			case 4:
				sb.append("1x2,  3x4,  1x3,  2x4,  1x4,  2x3");
				break;
			case 5:
				sb.append("4x5,  1x2,  3x4,  1x5,  2x3,  1x4,  3x5,  2x4,  1x3,  2x5");
				break;
			default:
				sb.append("");
				break;
		}
		return sb.toString();
	}

	/**
	 * @param sheet
	 * @param numRow
	 * @param competitorsPoules
	 */
	private void createClassificationTree(final WritableSheet sheet, final int numRow,
			final ArrayList<CompetitorPoule> competitorsPoules) {
		// creer l'arbre des rencontres pour les sorties de poules
		// 08.05.2008 (FMo) : optionnel, pas urgent du tout, je sais meme pas si
		// c'est Ã  faire!?
	}

	/**
	 * @param competitorPoule
	 * @param numRow
	 * @param sheet
	 */
	private void createPouleClassification(final CompetitorPoule competitorPoule, final int numRow,
			final WritableSheet sheet) {
		try {
			final WritableFont classificationTitle = new WritableFont(WritableFont.ARIAL, 28,
					WritableFont.BOLD, true, UnderlineStyle.NO_UNDERLINE, Colour.BLACK,
					ScriptStyle.NORMAL_SCRIPT);
			final WritableCellFormat classificationFormat = new WritableCellFormat(classificationTitle);

			final WritableFont classificationRow = new WritableFont(WritableFont.ARIAL, 12,
					WritableFont.BOLD, true, UnderlineStyle.NO_UNDERLINE, Colour.BLACK,
					ScriptStyle.NORMAL_SCRIPT);
			final WritableCellFormat classificationRowFormat = new WritableCellFormat(classificationRow);
			classificationRowFormat.setBorder(Border.ALL, BorderLineStyle.MEDIUM, Colour.BLACK);
			classificationFormat.setAlignment(Alignment.CENTRE);

			sheet.mergeCells(1, numRow, 6 + competitorPoule.getCompetitors().size(), numRow + 2);
			sheet.addCell(new Label(1, numRow, "CLASSEMENT", classificationFormat));
			String classement = "";

			for (int i = 0; i < competitorPoule.getCompetitors().size(); i++) {
				if (i == 0) {
					classement = "er";
				} else {
					classement = "e";
				}

				sheet.addCell(new Label(0, numRow + 4 + i, i + 1 + classement, classificationRowFormat));
				sheet.addCell(new Label(1, numRow + 4 + i, "", classificationRowFormat));
				sheet.mergeCells(1, numRow + 4 + i, 6 + competitorPoule.getCompetitors().size(), numRow + 4
						+ i);
			}

		} catch (final Exception e) {
			logger.error("", e);
		}
	}

	// -------------------------------------------------------------------------
	// 
	// -------------------------------------------------------------------------

	private void createFileHeader(final WritableSheet sheet, final CompetitionDescriptor comp,
			final String category, final boolean fullHeader) {

		// Cre le format dune cellule
		final WritableFont fileHeaderFont = new WritableFont(WritableFont.ARIAL, 12, WritableFont.BOLD, true,
				UnderlineStyle.NO_UNDERLINE, Colour.BLUE, ScriptStyle.NORMAL_SCRIPT);
		final WritableCellFormat fileHeaderFormat = new WritableCellFormat(fileHeaderFont);

		// Ajout des cellules
		try {
			sheet.addCell(new Label(0, 0, "Manifestation : " + comp.getCompetitionName(), fileHeaderFormat));
			sheet.addCell(new Label(4, 0, "Date : " + comp.getDate(), fileHeaderFormat));
			if (fullHeader) {
				sheet.addCell(new Label(0, 1, "Tapis : ", fileHeaderFormat));
			}
			sheet.addCell(new Label(4, 1, "Lieu : " + comp.getLocation(), fileHeaderFormat));
			sheet.addCell(new Label(4, 2, "Temps de combat : " + comp.getFightTime(), fileHeaderFormat));
			if (fullHeader) {
				sheet.addCell(new Label(0, 2, "Catégorie : " + category, fileHeaderFormat));
			}
		} catch (final Exception e) {
			logger.error("", e);
		}
	}

	private void createPouleTableHeader(final WritableSheet sheet, final PouleNumber pouleNumber,
			final int pouleSize, final int begin) {
		final WritableFont rowHeaderFont = new WritableFont(WritableFont.ARIAL, 12, WritableFont.BOLD, true,
				UnderlineStyle.NO_UNDERLINE, Colour.BLACK, ScriptStyle.NORMAL_SCRIPT);
		final WritableCellFormat rowHeaderFormat = new WritableCellFormat(rowHeaderFont);

		try {
			rowHeaderFormat.setBorder(Border.ALL, BorderLineStyle.MEDIUM, Colour.BLACK);
			rowHeaderFormat.setAlignment(Alignment.CENTRE);
			rowHeaderFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
			sheet.mergeCells(0, begin, 0, begin + pouleSize);

			sheet.addCell(new Label(0, begin, pouleNumber + "", rowHeaderFormat));
			sheet.addCell(new Label(1, begin, "", rowHeaderFormat));
			sheet.addCell(new Label(2, begin, "NOM Prénom", rowHeaderFormat));
			sheet.addCell(new Label(3, begin, "Club", rowHeaderFormat));
			sheet.addCell(new Label(4, begin, "G", rowHeaderFormat));
			for (int i = 0; i < pouleSize; i++) {
				sheet.addCell(new jxl.write.Number(5 + i, begin, i + 1, rowHeaderFormat));
			}
			sheet.addCell(new Label(5 + pouleSize, begin, "V", rowHeaderFormat));
			sheet.addCell(new Label(6 + pouleSize, begin, "P", rowHeaderFormat));
			sheet.addCell(new Label(7 + pouleSize, begin, "C", rowHeaderFormat));

		} catch (final Exception e) {
			logger.error("", e);
		}
	}

	private void createPouleTableRow(final WritableSheet sheet, final Competitor competitor,
			final int numRow, final int numCompetitor, final int nbCompetitors) {
		final WritableFont blackCellFont = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD,
				true, UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
		final WritableCellFormat blackCellFormat = new WritableCellFormat(blackCellFont);
		final WritableCellFormat rowCellFormat = new WritableCellFormat(blackCellFont);

		try {
			blackCellFormat.setBackground(Colour.GRAY_25);
			blackCellFormat.setBorder(Border.ALL, BorderLineStyle.MEDIUM, Colour.BLACK);
			rowCellFormat.setBorder(Border.ALL, BorderLineStyle.MEDIUM, Colour.BLACK);
			rowCellFormat.setAlignment(Alignment.CENTRE);
			sheet.addCell(new jxl.write.Number(1, numRow, numCompetitor, rowCellFormat));
			sheet.addCell(new Label(2, numRow, competitor.getNameAndWeight(), rowCellFormat));
			sheet.addCell(new Label(3, numRow, competitor.getClub().getClubName() + " - "
					+ competitor.getClub().getDepartment(), rowCellFormat));
			// on met une bordure a toutes les cases qui n'en n'ont pas
			for (int i = 0; i < nbCompetitors + 4; i++) {
				sheet.addCell(new Label(i + 4, numRow, "", rowCellFormat));
			}
			// on grise les cases nï¿½cessaire
			sheet.addCell(new Label(4 + numCompetitor, numRow, "", blackCellFormat));
		} catch (final Exception e) {
			logger.error("", e);
		}
	}

	private void initXls() {
		try {
			workbook = Workbook.createWorkbook(exportFile, Workbook.getWorkbook(modelsFile));
		} catch (final Exception ex) {
			logger.error("", ex);
		}
	}

	private WritableSheet createSheet(final String sheetName, final int sheetNumber) {
		return workbook.createSheet(sheetName, 0);
	}

	private void uninitXls() {
		try {
			workbook.write();
			workbook.close();
		} catch (final RowsExceededException e1) {
			logger.error("", e1);
		} catch (final WriteException e1) {
			logger.error("", e1);
		} catch (final IOException e) {
			logger.error("", e);
		} finally {
			logger.info("Le fichier \"" + exportFile + "\" à  été généré correctement.");
		}
	}
}
