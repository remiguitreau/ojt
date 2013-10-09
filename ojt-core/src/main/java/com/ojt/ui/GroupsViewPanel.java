package com.ojt.ui;

import org.apache.log4j.Logger;

import com.ojt.CompetitorGroup;
import com.ojt.CompetitorList;
import com.ojt.OjtConstants;
import com.ojt.algo.Algorithme;
import com.ojt.dao.xls.BiffRuntimeException;
import com.ojt.dao.xls.IORuntimeException;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

public class GroupsViewPanel extends JScrollPane {

	// -------------------------------------------------------------------------
	// Attributes
	// -------------------------------------------------------------------------

	private MultiCompetitorGroupList gprList;

	private final Logger logger = Logger.getLogger(getClass());

	// -------------------------------------------------------------------------
	// Constructor
	// -------------------------------------------------------------------------

	public GroupsViewPanel() {
		super();
	}

	public void initGroups(final List<CompetitorGroup> groups) {
		setViewportView(null);
		try {
			logger.info("Liste des groupes");

			gprList = new MultiCompetitorGroupList(groups, OjtConstants.MAX_COMPETITOR_PER_GROUP);
			setViewportView(gprList);
		} catch (final BiffRuntimeException ex) {
			logger.error("", ex);
			JOptionPane.showMessageDialog(this, "Impossible de lire le fichier, format incorrect.\n\n"
					+ ex.getMessage(), "Erreur d'import...", JOptionPane.ERROR_MESSAGE);
			throw new BadInitialisationException("Erreur d'initialisation des groupes");
		} catch (final IORuntimeException ex) {
			logger.error("", ex);
			JOptionPane.showMessageDialog(this,
					"Impossible de lire le fichier, fichier introuvable ou format incorrect.\n\n"
							+ ex.getMessage(), "Erreur d'import...", JOptionPane.ERROR_MESSAGE);
			throw new BadInitialisationException("Erreur d'initialisation des groupes");
		}
	}

	public List<CompetitorGroup> getGroups() {
		final List<CompetitorGroup> groups = new ArrayList<CompetitorGroup>();
		for (final CompetitorGroup group : gprList.getCompetitorGroups()) {
			final CompetitorGroup newGroup = new CompetitorGroup(group.getCompetitors(), new Algorithme());
			logger.info(newGroup);
			logger.info("---------------------------");
			groups.add(newGroup);
			newGroup.setSavedForAnotherWeighing(group.isSavedForAnotherWeighing());
		}
		return groups;
	}
}
