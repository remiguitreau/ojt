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
public class CompetitorUpdateException extends OJTException {
	public CompetitorUpdateException(final Exception ex) {
		super(ex);
	}
}
