/**
 * 
 */
package com.ojt.process;

import com.ojt.ui.GroupsViewPanel;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

/**
 * Etape de pesée.
 * @author Rémi "DwarfConan" Guitreau
 * @since 17 oct. 2009 : Création
 */
public class GroupOrganizerStep extends AbstractStep {

	private GroupsViewPanel groupsViewPanel;

	private CompetitionDatas competitionDatas;

	public GroupOrganizerStep() {
		super();

		initStepPanel();
	}

	// ---------------------------------------------------------
	// Implémentation de AbstractStep
	// ---------------------------------------------------------

	@Override
	public JComponent getStepComponent() {
		return groupsViewPanel;
	}

	@Override
	public void process(final CompetitionDatas competDatas) {
		this.competitionDatas = competDatas;
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				groupsViewPanel.initGroups(competDatas.getCompetitorsGroups());
				groupsViewPanel.setVisible(true);
				stepFinish();
			}
		});
	}

	@Override
	public boolean finalizeStep() {
		competitionDatas.setCompetitorsGroup(groupsViewPanel.getGroups());
		return true;
	}

	@Override
	public String getTitle() {
		return "Gestion des groupes de compétiteurs";
	}

	// ---------------------------------------------------------
	// Privées
	// ---------------------------------------------------------
	private void initStepPanel() {
		groupsViewPanel = new GroupsViewPanel();
	}
}
