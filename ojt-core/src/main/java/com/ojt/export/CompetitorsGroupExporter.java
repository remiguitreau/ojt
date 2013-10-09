package com.ojt.export;

import com.ojt.CompetitionDescriptor;
import com.ojt.CompetitorGroup;

import java.util.List;

public interface CompetitorsGroupExporter {

	/**
	 * Export une liste de poules.
	 * @param competitorsPoules Les poules.
	 * @param comp La description de la compétition.
	 */
	public void exportCompetitors(final List<CompetitorGroup> competitorsPoules,
			final CompetitionDescriptor comp);

	public void addGroupExportListener(final GroupExportListener listener);
}
