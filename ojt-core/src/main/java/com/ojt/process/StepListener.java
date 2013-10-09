/**
 * 
 */
package com.ojt.process;

/**
 * Auditeur d'�tape de processus.
 *
 * @author R�mi "DwarfConan" Guitreau 
 * @since 17 oct. 2009 : Cr�ation
 */
public interface StepListener {
	void stepFinished();
	
	void stepProcessing();
}
