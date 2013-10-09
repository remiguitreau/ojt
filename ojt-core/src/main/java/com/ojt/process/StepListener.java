/**
 * 
 */
package com.ojt.process;

/**
 * Auditeur d'étape de processus.
 *
 * @author Rémi "DwarfConan" Guitreau 
 * @since 17 oct. 2009 : Création
 */
public interface StepListener {
	void stepFinished();
	
	void stepProcessing();
}
