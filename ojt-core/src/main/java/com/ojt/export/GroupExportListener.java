/**
 * 
 */
package com.ojt.export;

import com.ojt.Competitor;
import com.ojt.CompetitorGroup;

/**
 * Auditeur sur l'export de groupes. 
 *
 * @author R�mi "DwarfConan" Guitreau 
 * @since 23 ao�t 2009 : Cr�ation
 */
public interface GroupExportListener {
	void groupExportBegin(final String groupName, final CompetitorGroup group, final int groupNumber);
	
	void competitorExported(final Competitor competitor);
	
	void exportFinished();
}
