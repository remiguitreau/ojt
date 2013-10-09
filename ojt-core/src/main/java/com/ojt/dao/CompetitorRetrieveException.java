/**
 * 
 */
package com.ojt.dao;

import com.ojt.OJTException;

/**
 * Exception survenant lors de la mise � jour d'un comp�titeur.
 *
 * @author R�mi "DwarfConan" Guitreau 
 * @since 6 mai 2009 : Cr�ation
 */
public class CompetitorRetrieveException extends OJTException {
	public CompetitorRetrieveException(final Exception ex) {
		super(ex);
	}
}
