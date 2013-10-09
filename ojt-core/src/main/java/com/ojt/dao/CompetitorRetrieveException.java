/**
 * 
 */
package com.ojt.dao;

import com.ojt.OJTException;

/**
 * Exception survenant lors de la mise à jour d'un compétiteur.
 *
 * @author Rémi "DwarfConan" Guitreau 
 * @since 6 mai 2009 : Création
 */
public class CompetitorRetrieveException extends OJTException {
	public CompetitorRetrieveException(final Exception ex) {
		super(ex);
	}
}
