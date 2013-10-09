/**
 * 
 */
package com.ojt.export.xlspoi;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.ojt.CompetitionDescriptor;
import com.ojt.CompetitorGroup;
import com.ojt.export.CompetitorsGroupExporter;
import com.ojt.export.GroupExportListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.LinkedList;
import java.util.List;

/**
 * Exporteur de groupes de compétiteurs OpenOffice
 * @author Rémi "DwarfConan" Guitreau
 * @since 20 avr. 2009 : Création
 */
public class XlsPoiCompetitorsGroupExporter implements CompetitorsGroupExporter {

	// -------------------------------------------------------------------------
	// Attributs
	// -------------------------------------------------------------------------
	private final Logger logger = Logger.getLogger(getClass());

	private final CompetitorsGroupSheetCompleter sheetCompleter;

	private final File exportFile;

	private final List<GroupExportListener> listeners = new LinkedList<GroupExportListener>();

	private final File modelsFile;

	// ---------------------------------------------------------
	// Constructeurs
	// ---------------------------------------------------------

	/**
	 * @param exportFile Le fichier dans lequel exporter les poules.
	 * @param modelsFile Le fichier contenant les modèles pour l'export
	 */
	public XlsPoiCompetitorsGroupExporter(final File exportFile, final File modelsFile) {
		super();

		this.exportFile = exportFile;
		this.modelsFile = modelsFile;
		sheetCompleter = new CompetitorsGroupSheetCompleter();
	}

	// ---------------------------------------------------------
	// Implémentations de CompetitorsGroupExporter
	// ---------------------------------------------------------
	@Override
	public void addGroupExportListener(final GroupExportListener listener) {
		this.listeners.add(listener);
		sheetCompleter.addGroupExportListener(listener);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void exportCompetitors(final List<CompetitorGroup> competitorsPoules,
			final CompetitionDescriptor competitionDescriptor) {

		FileOutputStream exportFileOutputStream = null;
		//!!!! ATTENTION !!!! Ici l'ordre est super important. Le but est de ne pas avoir à déplacer de feuille car cela
		//pose des problèmes avec apache POI. Du coup on crée les feuilles de façon à n'avoir qu'à supprimer les feuilles modèles.
		try {
			final HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(modelsFile));
			int sheetNumber = 0;
			for (final CompetitorGroup group : competitorsPoules) {
				final String sheetName = (group.getCompetitors().size() + " "
						+ group.getNameForSheet(true) + "____G" + (sheetNumber + 1));
				fireGroupExportBegin(sheetName, group, (sheetNumber + 1));
				logger.info("Copie de la feuille '"
						+ ("GROUPE DE " + String.valueOf(group.getCompetitors().size())) + "' vers '"
						+ sheetName + "'");
				// Copie de la feuille de modèle pour le groupe.
				workbook.cloneSheet(workbook.getSheetIndex("GROUPE DE "
						+ String.valueOf(group.getCompetitors().size())));
				workbook.setSheetName(workbook.getNumberOfSheets() - 1, sheetName);
				sheetNumber++;
			}
			sheetNumber = 0;
			ResultsSheetCompleter resultsSheetCompleter = null;
			try {
				final int resultsSheetIdx = workbook.getSheetIndex("RESULTATS");
				workbook.cloneSheet(resultsSheetIdx);				
				workbook.removeSheetAt(resultsSheetIdx);
				workbook.setSheetName(workbook.getNumberOfSheets() - 1, "RESULTATS");
				resultsSheetCompleter = new ResultsSheetCompleter(workbook.getSheet("RESULTATS"));
				resultsSheetCompleter.init(competitionDescriptor);
				addGroupExportListener(resultsSheetCompleter);
				sheetCompleter.addGroupExportListener(resultsSheetCompleter);
			} catch (final Exception ex) {
				logger.warn("Pas de feuille résultats correcte trouvées.", ex);
			}
			StatsSheetCompleter statsSheetCompleter = null;
			try {
				final int statsSheetIdx = workbook.getSheetIndex("STATISTIQUES");
				workbook.cloneSheet(statsSheetIdx);				
				workbook.removeSheetAt(statsSheetIdx);
				workbook.setSheetName(workbook.getNumberOfSheets() - 1, "STATISTIQUES");
				statsSheetCompleter = new StatsSheetCompleter(workbook.getSheet("STATISTIQUES"));
				addGroupExportListener(statsSheetCompleter);
			} catch (final Exception ex) {
				logger.warn("Pas de feuille statistiques correcte trouvées.", ex);
			}
			try {
				for (final CompetitorGroup group : competitorsPoules) {
					final String sheetName = (group.getCompetitors().size() + " "
							+ group.getNameForSheet(true) + "____G" + (sheetNumber + 1));
					fireGroupExportBegin(sheetName, group, (sheetNumber + 1));
					logger.info("Copie de la feuille '"
							+ ("GROUPE DE " + String.valueOf(group.getCompetitors().size())) + "' vers '"
							+ sheetName + "'");
					// On complète la feuille avec la poule.
					sheetCompleter.completeSheetWithGroup(workbook.getSheet(sheetName), group,
							sheetNumber + 1, competitionDescriptor);
					sheetNumber++;
				}
				if (resultsSheetCompleter != null) {
					resultsSheetCompleter.cleanSheet();
				}
				if (statsSheetCompleter != null) {
					statsSheetCompleter.generate(competitionDescriptor);
				}
				for (int i = 0; i < 16; i++) {
					workbook.removeSheetAt(0);
				}
			} catch (final Exception ex) {
				logger.error("Error while exporting competitors group.", ex);
			}
			exportFileOutputStream = new FileOutputStream(exportFile);
			workbook.write(exportFileOutputStream);
			exportFileOutputStream.flush();
		} catch (final Exception ex) {
			logger.error("Erreur lors de l'écriture du fichier...", ex);
			throw new RuntimeException(ex.getMessage(), ex);
		} finally {
			IOUtils.closeQuietly(exportFileOutputStream);
			fireExportFinished();
		}
	}

	// -------------------------------------------------------------------------
	// Privées
	// -------------------------------------------------------------------------
	private void fireGroupExportBegin(final String sheetName, final CompetitorGroup group,
			final int groupNumber) {
		for (final GroupExportListener listener : listeners) {
			if (listener != null) {
				listener.groupExportBegin(sheetName, group, groupNumber);
			}
		}
	}

	private void fireExportFinished() {
		for (final GroupExportListener listener : listeners) {
			if (listener != null) {
				listener.exportFinished();
			}
		}
	}
}
