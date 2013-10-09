/**
 * 
 */
package com.ojt.process;

/**
 * Processus OJT, on distingue principalement la pes�e, le tri et l'encha�nement des deux.
 *
 * @author R�mi "DwarfConan" Guitreau 
 * @since 17 oct. 2009 : Cr�ation
 */
public interface Process {
	
	/**
	 * Ajoute une �tape du processus.
	 * @param step L'�tape.
	 */
	void addStep(final Step step);
	
	/**
	 * Lance le processus.
	 */
	void launch();
	
	void addProcessListener(final ProcessListener processListener);

}
