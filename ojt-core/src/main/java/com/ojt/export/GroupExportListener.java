/**
 * 
 */
package com.ojt.export;

import com.ojt.Competitor;
import com.ojt.CompetitorGroup;

/**
 * Auditeur sur l'export de groupes. 
 *
 * @author Rémi "DwarfConan" Guitreau 
 * @since 23 août 2009 : Création
 */
public interface GroupExportListener {
	void groupExportBegin(final String groupName, final CompetitorGroup group, final int groupNumber);
	
	void competitorExported(final Competitor competitor);
	
	void exportFinished();
}
