package com.ojt.export;

import com.ojt.OJTConfiguration;
import com.ojt.export.xls.XlsCompetitorsGroupExporter;
import com.ojt.export.xlspoi.XlsPoiCompetitorsGroupExporter;

import java.io.File;
import java.io.IOException;

public class CompetitorsGroupExporterFactory {

	// -------------------------------------------------------------------------
	// Statique
	// -------------------------------------------------------------------------

	private static CompetitorsGroupExporterFactory instance;

	public static CompetitorsGroupExporterFactory getInstance() {
		if (instance == null) {
			instance = new CompetitorsGroupExporterFactory();
		}
		return instance;
	}

	// -------------------------------------------------------------------------
	// Attributs
	// -------------------------------------------------------------------------

	private final DefaultModelsFileExporter defaultModelsFileExporter;

	/**
	 * @return Returns the defaultModelsFileExporter.
	 */
	public DefaultModelsFileExporter getDefaultModelsFileExporter() {
		return defaultModelsFileExporter;
	}

	/*
	 * Singleton
	 */
	private CompetitorsGroupExporterFactory() {
		super();

		defaultModelsFileExporter = new DefaultModelsFileExporter();
	}

	public CompetitorsGroupExporter createCompetitorsGroupExporter(final File exportFile,
			final File modelsFile) throws IOException {
		if ((exportFile != null) && (exportFile.getAbsolutePath().indexOf(".xls") > 0)) {
			if (OJTConfiguration.getInstance().getPropertyAsBoolean(OJTConfiguration.EXPORT_FROM_MODELS)) {
				// on utilise un fichier de modèle
				if (modelsFile == null) {
					return new XlsPoiCompetitorsGroupExporter(exportFile,
							defaultModelsFileExporter.exportDefaultModelsFile());
				} else {
					return new XlsPoiCompetitorsGroupExporter(exportFile, modelsFile);
				}
			} else {
				// sinon
				return new XlsCompetitorsGroupExporter(exportFile);
			}
		}
		throw new IllegalArgumentException("File format not supported : " + exportFile);
	}
}
