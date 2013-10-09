/**
 * 
 */
package com.ojt.export.openoffice;

import org.apache.log4j.Logger;

import com.ojt.CompetitionDescriptor;
import com.ojt.CompetitorGroup;
import com.ojt.OJTConfiguration;
import com.ojt.export.CompetitorsGroupExporter;
import com.ojt.export.GroupExportListener;
import com.sun.star.beans.PropertyValue;
import com.sun.star.beans.XPropertySet;
import com.sun.star.bridge.UnoUrlResolver;
import com.sun.star.bridge.XUnoUrlResolver;
import com.sun.star.comp.helper.Bootstrap;
import com.sun.star.frame.XComponentLoader;
import com.sun.star.frame.XFrame;
import com.sun.star.frame.XModel;
import com.sun.star.frame.XStorable;
import com.sun.star.lang.XComponent;
import com.sun.star.lang.XMultiComponentFactory;
import com.sun.star.sheet.XSpreadsheet;
import com.sun.star.sheet.XSpreadsheetDocument;
import com.sun.star.sheet.XSpreadsheets;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.XComponentContext;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Exporteur de groupes de compétiteurs OpenOffice
 * @author Rémi "DwarfConan" Guitreau
 * @since 20 avr. 2009 : Création
 */
public class OOCompetitorsGroupExporter implements CompetitorsGroupExporter {

	// -------------------------------------------------------------------------
	// Constantes
	// -------------------------------------------------------------------------

	private static final String UNO_SOCKET_CONNECT_OPTIONS = "uno:socket,host=localhost,port=8100;urp;StarOffice.ServiceManager";

	private static final String OO_LAUNCH_OPTIONS = " -accept=socket,host=localhost,port=8100;urp -invisible -nologo -headless -norestore -nofirststartwizard";

	// -------------------------------------------------------------------------
	// Attributs
	// -------------------------------------------------------------------------
	private final Logger logger = Logger.getLogger(getClass());

	private Process ooProcess;

	private final CompetitorsGroupSheetCompleter sheetCompleter;

	private final File exportFile;

	private List<GroupExportListener> listeners = new LinkedList<GroupExportListener>();

	private final File modelsFile;

	// ---------------------------------------------------------
	// Constructeurs
	// ---------------------------------------------------------

	/**
	 * @param exportFile Le fichier dans lequel exporter les poules.
	 * @param modelsFile Le fichier contenant les modèles pour l'export
	 */
	public OOCompetitorsGroupExporter(final File exportFile, final File modelsFile) {
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

		try {
			// Lancement du processus OpenOffice.
			launchOpenOfficeListener();
			// 12.10.2009 (FMo) : temporisation pour laisser du temps à
			// Openoffice de se lancer
			Thread.sleep(1000);
			// Chargement des modèles.
			XComponent component = null;
			try {
				component = loadModelsComponent();
			} catch (final Exception ex) {
				// 12.10.2009 (FMo) : on relance le chargement des modèles en
				// cas de problème, openoffice met dès fois tu temps à se lancer
				// après une temporisation de 5s
				Thread.sleep(5000);
				component = loadModelsComponent();
			}
			final XSpreadsheetDocument xdocument = (XSpreadsheetDocument) UnoRuntime.queryInterface(
					XSpreadsheetDocument.class, component);
			final XSpreadsheets xSheets = xdocument.getSheets();
			int sheetNumber = 0;
			ResultsSheetCompleter resultsSheetCompleter = null;
			try {
				resultsSheetCompleter = new ResultsSheetCompleter((XSpreadsheet) UnoRuntime.queryInterface(
						XSpreadsheet.class, xSheets.getByName("RESULTATS")));
				resultsSheetCompleter.init(competitionDescriptor);
				addGroupExportListener(resultsSheetCompleter);
				sheetCompleter.addGroupExportListener(resultsSheetCompleter);
			}
			catch(final Exception ex) {
				logger.warn("Pas de feuille résultats correcte trouvées.", ex);
			}
			StatsSheetCompleter statsSheetCompleter = null;
			try {
				statsSheetCompleter = new StatsSheetCompleter((XSpreadsheet) UnoRuntime.queryInterface(
						XSpreadsheet.class, xSheets.getByName("STATISTIQUES")));
				addGroupExportListener(statsSheetCompleter);
			}
			catch(final Exception ex) {
				logger.warn("Pas de feuille statistiques correcte trouvées.", ex);
			}
			try {
				for (final CompetitorGroup group : competitorsPoules) {
					final String sheetName = (group.getCompetitors().size() + " "
							+ group.getNameForSheet(true) + "____G" + (sheetNumber + 1));
					fireGroupExportBegin(sheetName, group, (sheetNumber+1));
					logger.info("Copie de la feuille '"
							+ ("GROUPE DE " + String.valueOf(group.getCompetitors().size())) + "' vers '"
							+ sheetName + "'");
					// Copie de la feuille de modèle pour le groupe.
					xSheets.copyByName("GROUPE DE " + String.valueOf(group.getCompetitors().size()),
							sheetName, (short) sheetNumber);
					// On complète la feuille avec la poule.
					sheetCompleter.completeSheetWithGroup((XSpreadsheet) UnoRuntime.queryInterface(
							XSpreadsheet.class, xSheets.getByName(sheetName)), group, sheetNumber + 1, competitionDescriptor);
					sheetNumber++;
				}
				for (int i = 1; i <= 16; i++) {
					xSheets.removeByName("GROUPE DE " + i);
				}
				if(resultsSheetCompleter != null) {
					resultsSheetCompleter.cleanSheet();
				}
				if(statsSheetCompleter != null) {
					statsSheetCompleter.generate(competitionDescriptor);
				}
				
			} catch (final Exception ex) {
				logger.error("Error while exporting competitors group.", ex);
			}
			saveExport(component);
		} catch (final Exception ex) {
			throw new RuntimeException(ex.getMessage(), ex);
		} finally {
			// Arrêt du processus OpenOffice.
			terminateOpenOfficeListener();
			fireExportFinished();
		}
	}
	
	// -------------------------------------------------------------------------
	// Privées
	// -------------------------------------------------------------------------
	private void fireGroupExportBegin(final String sheetName, final CompetitorGroup group, final int groupNumber) {
		for(final GroupExportListener listener : listeners) {
			if (listener != null) {
				listener.groupExportBegin(sheetName, group, groupNumber);
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
	
	/**
	 * Lance le processus d'écoute d'OpenOffice.
	 */
	private void launchOpenOfficeListener() throws IOException {
		ooProcess = Runtime.getRuntime().exec(
				OJTConfiguration.getInstance().getProperty(OJTConfiguration.OO_LAUNCH_COMMAND)
						+ OO_LAUNCH_OPTIONS);
	}

	/**
	 * Arrête le processus d'écoute d'OpenOffice.
	 */
	private void terminateOpenOfficeListener() {
		if (ooProcess != null) {
			ooProcess.destroy();
		}
	}

	/**
	 * Charge le document des modèles dans OpenOffice.
	 * @return Le document des modèles.
	 * @throws Exception
	 */
	private XComponent loadModelsComponent() throws Exception {
		final XComponentContext xcomponentcontext = Bootstrap.createInitialComponentContext(null);

		// create a connector, so that it can contact the office
		final XUnoUrlResolver urlResolver = UnoUrlResolver.create(xcomponentcontext);

		final Object initialObject = urlResolver.resolve(UNO_SOCKET_CONNECT_OPTIONS);

		final XMultiComponentFactory xOfficeFactory = (XMultiComponentFactory) UnoRuntime.queryInterface(
				XMultiComponentFactory.class, initialObject);

		// retrieve the component context as property (it is not yet
		// exported from the office)
		// Query for the XPropertySet interface.
		final XPropertySet xProperySet = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class,
				xOfficeFactory);

		// Get the default context from the office server.
		final Object oDefaultContext = xProperySet.getPropertyValue("DefaultContext");

		// Query for the interface XComponentContext.
		final XComponentContext xOfficeComponentContext = (XComponentContext) UnoRuntime.queryInterface(
				XComponentContext.class, oDefaultContext);

		// now create the desktop service
		// NOTE: use the office component context here!
		final Object oDesktop = xOfficeFactory.createInstanceWithContext("com.sun.star.frame.Desktop",
				xOfficeComponentContext);

		final XComponentLoader loader = (XComponentLoader) UnoRuntime.queryInterface(XComponentLoader.class,
				oDesktop);
		final PropertyValue[] loadProps = new PropertyValue[2];
		loadProps[0] = new PropertyValue();
		loadProps[0].Name = "AsTemplate";
		loadProps[1] = new PropertyValue();
		loadProps[0].Value = new Boolean(true);
		loadProps[1].Name = "Hidden";
		loadProps[1].Value = new Boolean(true);
		return loader.loadComponentFromURL("file:///" + modelsFile.getPath(), "_blank", 0, loadProps);
	}

	/**
	 * Sauvegarde du document d'export des poules.
	 * @param component Le composant contenant l'export des poules
	 * @return Le fichier dans lequel a été stocké l'export.
	 * @throws IOException
	 * @throws com.sun.star.io.IOException
	 */
	private File saveExport(final XComponent component) throws IOException, com.sun.star.io.IOException {
		final StringBuffer sStoreFileUrl = new StringBuffer("file:///");
		sStoreFileUrl.append(exportFile.getCanonicalPath().replace('\\', '/'));
		final String storeUrl = sStoreFileUrl.toString();

		final XModel xModel = (XModel) UnoRuntime.queryInterface(XModel.class, component);

		final XFrame xFrame = xModel.getCurrentController().getFrame();
		xFrame.activate();

		final XStorable xStorable = (XStorable) UnoRuntime.queryInterface(XStorable.class, component);
		final PropertyValue[] props = new PropertyValue[1];
		props[0] = new PropertyValue();
		props[0].Name = "FilterName";
		props[0].Value = "MS Excel 97";
		xStorable.storeToURL(storeUrl, props);

		component.dispose();
		return exportFile;
	}
}
