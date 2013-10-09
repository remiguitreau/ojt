/**
 * 
 */
package com.ojt.process;

import javax.swing.JFrame;

import com.ojt.balance.BalanceDriver;

/**
 * Processus de génération d'un fichier source.
 *
 * @author Rémi "DwarfConan" Guitreau 
 * @since 17 oct. 2009 : Création
 */
public class SourceFileProcess extends AbstractProcess {

	public SourceFileProcess(final JFrame processFrame) {
		super(processFrame);
	}
	
	@Override
	protected void initSteps() {
		addStep(new CompetitionInformationStep(CompetitionInformationStep.SOURCE_CREATION));
		addStep(new CreateSourceFileStep());
	}
	

}
