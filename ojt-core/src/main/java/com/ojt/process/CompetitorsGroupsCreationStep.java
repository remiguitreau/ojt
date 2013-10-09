package com.ojt.process;

import javax.swing.JComponent;

import org.apache.log4j.Logger;

import com.ojt.CompetitorGroup;
import com.ojt.algo.Algorithme;

public class CompetitorsGroupsCreationStep extends AbstractStep {

	private final Logger logger = Logger.getLogger(getClass());
	
	@Override
	public boolean finalizeStep() {
		return true;
	}

	@Override
	public JComponent getStepComponent() {
		return null;
	}

	@Override
	public String getTitle() {
		return "Création des groupes de compétiteurs";
	}

	@Override
	public void process(final CompetitionDatas competitionDatas) {
		competitionDatas.getValidCompetitors().setMaxCompetitorsPerGroup(competitionDatas.getCompetitorsPerGroup());

		final Algorithme algo = new Algorithme();
		competitionDatas.setCompetitorsGroup(competitionDatas.getValidCompetitors().createGroups(algo));
		for (final CompetitorGroup group : competitionDatas.getCompetitorsGroups()) {
			logger.info(group);
			logger.info("---------------------------");
		}
		stepFinish();
	}

}
