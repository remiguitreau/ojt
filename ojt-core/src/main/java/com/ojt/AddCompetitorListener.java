package com.ojt;

/**
 * Ecouteur dédié à la création d'un compétiteur
 * @author CDa
 * @since 9 août 2009 (CDa) : Création
 */
public interface AddCompetitorListener {

	/**
	 * Ecouteur dédié à l'ajout d'un compétiteur
	 * @param competitor Le compétiteur ajouté
	 */
	void addNewCompetitor(Competitor competitor);
}
