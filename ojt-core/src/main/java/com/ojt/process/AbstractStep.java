/**
 * 
 */
package com.ojt.process;

import java.util.LinkedList;
import java.util.List;

import javax.swing.JPanel;

/**
 * Step abstrait
 *
 * @author Rémi "DwarfConan" Guitreau 
 * @since 17 oct. 2009 : Création
 */
public abstract class AbstractStep implements Step {

	//---------------------------------------------------------
	// Attributs
	//---------------------------------------------------------
	private final List<StepListener> stepListeners;
	
	//---------------------------------------------------------
	// Constructeur
	//---------------------------------------------------------
	public AbstractStep() {
		super();
		
		stepListeners = new LinkedList<StepListener>();
	}
	
	//---------------------------------------------------------
	// Implémentation de Step
	//---------------------------------------------------------

	@Override
	public void addStepListener(StepListener stepListener) {
		if(!stepListeners.contains(stepListener)) {
			stepListeners.add(stepListener);
		}
	}
	
	public void stepFinish() {
		fireStepFinished();
	}
	
	public void stepProcessing() {
		fireStepProcessing();
	}
	
	@Override
	public boolean goBackBeforeEnd() {
		return true;
	}
	
	//---------------------------------------------------------
	// Privées
	//---------------------------------------------------------
	private void fireStepFinished() {
		for(final StepListener stepListener : stepListeners) {
			if(stepListener != null) {
				stepListener.stepFinished();
			}
		}
	}
	
	private void fireStepProcessing() {
		for(final StepListener stepListener : stepListeners) {
			if(stepListener != null) {
				stepListener.stepProcessing();
			}
		}
	}
}
