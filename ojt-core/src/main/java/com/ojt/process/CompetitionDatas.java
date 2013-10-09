/**
 * 
 */
package com.ojt.process;

import com.ojt.CompetitionDescriptor;
import com.ojt.CompetitorGroup;
import com.ojt.CompetitorList;
import com.ojt.OjtConstants;
import com.ojt.dao.CompetitorsDao;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Rémi "DwarfConan" Guitreau
 * @since 18 oct. 2009 : Création
 */
public class CompetitionDatas {

	private CompetitionDescriptor competitionDescriptor = new CompetitionDescriptor();

	private File competitionFile;

	private File modelsFile;

	private String weighingPost;

	private CompetitorsDao competitorsDao;

	private int competitorsPerGroup = OjtConstants.MAX_COMPETITOR_PER_GROUP;

	private CompetitorList validCompetitors;

	private List<CompetitorGroup> competitorsGroups;

	private boolean onlyWithWeight;

	public final CompetitionDescriptor getCompetitionDescriptor() {
		return competitionDescriptor;
	}

	public final void setCompetitionDescriptor(final CompetitionDescriptor competitionDescriptor) {
		this.competitionDescriptor = competitionDescriptor;
	}

	public final File getCompetitionFile() {
		return competitionFile;
	}

	public final void setCompetitionFile(final File competitionFile) {
		this.competitionFile = competitionFile;
	}

	public final File getModelsFile() {
		return modelsFile;
	}

	public final void setModelsFile(final File modelsFile) {
		this.modelsFile = modelsFile;
	}

	public String getWeighingPost() {
		return weighingPost;
	}

	public void setWeighingPost(final String weighingPost) {
		this.weighingPost = weighingPost;
	}

	public void setCompetitorsDao(final CompetitorsDao competitorsDao) {
		this.competitorsDao = competitorsDao;
	}

	public CompetitorsDao getCompetitorsDao() {
		return competitorsDao;
	}

	public void setCompetitorsPerGroup(final int competitorsPerGroup) {
		this.competitorsPerGroup = competitorsPerGroup;
	}

	public int getCompetitorsPerGroup() {
		return competitorsPerGroup;
	}

	public List<CompetitorGroup> retrieveCompetitorsGroupsForPoulesExport() {
		final List<CompetitorGroup> groupsForPoules = new LinkedList<CompetitorGroup>();
		for (final CompetitorGroup group : competitorsGroups) {
			if (!group.isSavedForAnotherWeighing()) {
				groupsForPoules.add(group);
			}
		}
		return groupsForPoules;
	}

	public void setValidCompetitors(final CompetitorList validCompetitors) {
		this.validCompetitors = validCompetitors;
	}

	public CompetitorList getValidCompetitors() {
		return validCompetitors;
	}

	public List<CompetitorGroup> getCompetitorsGroups() {
		return competitorsGroups;
	}

	public void setCompetitorsGroup(final List<CompetitorGroup> competitorsGroup) {
		this.competitorsGroups = competitorsGroup;
	}

	public boolean getOnlyWithWeight() {
		return onlyWithWeight;
	}

	/**
	 * @param onlyWithWeight The onlyWithWeight to set.
	 */
	public void setOnlyWithWeight(final boolean onlyWithWeight) {
		this.onlyWithWeight = onlyWithWeight;
	}

}
