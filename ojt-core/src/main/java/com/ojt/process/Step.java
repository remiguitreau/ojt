/**
 * 
 */
package com.ojt.process;

import javax.swing.JComponent;

/**
 * Une étape de processus OJT.
 *
 * @author Rémi "DwarfConan" Guitreau 
 * @since 17 oct. 2009 : Création
 */
public interface Step {

	/** 
	 * @return Le panneau d'affichage de l'étape, peut être null, dans ce cas il s'agit d'une étape non graphique.
	 */
	JComponent getStepComponent();
	
	/**
	 * Exécute l'étape.
	 */
	void process(final CompetitionDatas competitionDatas);
	
	void addStepListener(final StepListener stepListener);
	
	/**
	 * Demande si l'on peut passer à l'étape suivante. Cela permet de faire les dernières vérifications avant qu'il ne soit trop tard !
	 */
	boolean finalizeStep();

	String getTitle();
	
	boolean goBackBeforeEnd();
}
