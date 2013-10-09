/**
 * 
 */
package com.ojt.export;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

/**
 * Exporteur du fichier mod�le par d�faut
 * @author RGu
 * @since 28 ao�t 2009 (RGu) : Cr�ation
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
