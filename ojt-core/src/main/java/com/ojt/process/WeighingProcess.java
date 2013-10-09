/**
 * 
 */
package com.ojt.process;

import javax.swing.JFrame;

import com.ojt.balance.BalanceDriver;

/**
 * Processus de pesée.
 *
 * @author Rémi "DwarfConan" Guitreau 
 * @since 17 oct. 2009 : Création
 */
public class WeighingProcess extends AbstractProcess {

	private final BalanceDriver balanceDriver;
	
	public WeighingProcess(final JFrame processFrame, final BalanceDriver balanceDriver) {
		super(processFrame);
		
		this.balanceDriver = balanceDriver;
	}
	
	@Override
	protected void initSteps() {
		addStep(new CompetitionInformationStep(CompetitionInformationStep.ONLY_WEIGHING));
		addStep(new PersistancyCreationStep());
		addStep(new WeighingStep(false, balanceDriver));
		addStep(new WeighingExportStep());
	}
	

}
