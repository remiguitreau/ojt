/**
 * 
 */
package com.ojt.export;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

/**
 * Exporteur du fichier modèle par défaut
 * @author RGu
 * @since 28 août 2009 (RGu) : Création
 */
public class DefaultModelsFileExporter {

	/**
	 * 
	 */
	public DefaultModelsFileExporter() {
		super();
	}

	public void exportDefaultModelsFile(final File modelsFile) throws IOException {		
		FileUtils.copyURLToFile(CompetitorsGroupExporter.class.getResource("models.xls"), modelsFile);
	}
	
	public File exportDefaultModelsFile() throws IOException {
		final File modelsFile = new File(System.getProperty("java.io.tmpdir"), "models.xls");
		exportDefaultModelsFile(modelsFile);
		return modelsFile;
	}
}
