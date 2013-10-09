/**
 * 
 */
package com.ojt.process;

import com.ojt.Competitor;
import com.ojt.CompetitorGroup;
import com.ojt.export.GroupExportListener;
import com.ojt.ui.ExportPanel;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

/**
 * Etape de pesée.
 * @author Rémi "DwarfConan" Guitreau
 * @since 17 oct. 2009 : Création
 */
public class PouleExportStep extends AbstractStep implements GroupExportListener {

	private ExportPanel exportPanel;

	public PouleExportStep() {
		super();

		initStepPanel();
	}

	// ---------------------------------------------------------
	// Implémentation de AbstractStep
	// ---------------------------------------------------------

	@Override
	public JComponent getStepComponent() {
		return exportPanel;
	}

	@Override
	public void process(final CompetitionDatas competitionDatas) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				exportPanel.process(competitionDatas.getCompetitionFile(),
						competitionDatas.retrieveCompetitorsGroupsForPoulesExport(),
						competitionDatas.getCompetitionDescriptor(), competitionDatas.getModelsFile(),
						PouleExportStep.this);
				exportPanel.setVisible(true);
			}
		});
	}

	@Override
	public boolean finalizeStep() {
		return true;
	}

	@Override
	public String getTitle() {
		return "Export des poules";
	}

	@Override
	public boolean goBackBeforeEnd() {
		return false;
	}

	// ---------------------------------------------------------
	// Implémentation de GroupExportListener
	// ---------------------------------------------------------
	@Override
	public void competitorExported(final Competitor competitor) {

	}

	@Override
	public void exportFinished() {
		stepFinish();
	}

	@Override
	public void groupExportBegin(final String groupName, final CompetitorGroup group, final int groupNumber) {

	}

	// ---------------------------------------------------------
	// Privées
	// ---------------------------------------------------------
	private void initStepPanel() {
		exportPanel = new ExportPanel();
	}
}
