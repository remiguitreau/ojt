/**
 * 
 */
package com.ojt.process;

/**
 * Processus OJT, on distingue principalement la pesée, le tri et l'enchaînement des deux.
 *
 * @author Rémi "DwarfConan" Guitreau 
 * @since 17 oct. 2009 : Création
 */
public interface Process {
	
	/**
	 * Ajoute une étape du processus.
	 * @param step L'étape.
	 */
	void addStep(final Step step);
	
	/**
	 * Lance le processus.
	 */
	void launch();
	
	void addProcessListener(final ProcessListener processListener);

}
