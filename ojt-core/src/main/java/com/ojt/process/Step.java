/**
 * 
 */
package com.ojt.process;

import javax.swing.JComponent;

/**
 * Une �tape de processus OJT.
 *
 * @author R�mi "DwarfConan" Guitreau 
 * @since 17 oct. 2009 : Cr�ation
 */
public interface Step {

	/** 
	 * @return Le panneau d'affichage de l'�tape, peut �tre null, dans ce cas il s'agit d'une �tape non graphique.
	 */
	JComponent getStepComponent();
	
	/**
	 * Ex�cute l'�tape.
	 */
	void process(final CompetitionDatas competitionDatas);
	
	void addStepListener(final StepListener stepListener);
	
	/**
	 * Demande si l'on peut passer � l'�tape suivante. Cela permet de faire les derni�res v�rifications avant qu'il ne soit trop tard !
	 */
	boolean finalizeStep();

	String getTitle();
	
	boolean goBackBeforeEnd();
}
