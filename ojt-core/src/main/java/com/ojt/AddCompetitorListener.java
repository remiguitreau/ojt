package com.ojt;

/**
 * Ecouteur d�di� � la cr�ation d'un comp�titeur
 * @author CDa
 * @since 9 ao�t 2009 (CDa) : Cr�ation
 */
public interface AddCompetitorListener {

	/**
	 * Ecouteur d�di� � l'ajout d'un comp�titeur
	 * @param competitor Le comp�titeur ajout�
	 */
	void addNewCompetitor(Competitor competitor);
}
