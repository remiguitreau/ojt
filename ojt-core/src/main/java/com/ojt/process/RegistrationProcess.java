/**
 * 
 */
package com.ojt.process;

import com.ojt.balance.BalanceDriver;

import javax.swing.JFrame;

/**
 * Processus de pes�e.
 * @author R�mi "DwarfConan" Guitreau
 * @since 17 oct. 2009 : Cr�ation
 */
public class RegistrationProcess extends AbstractProcess {

    private final BalanceDriver balanceDriver;

    public RegistrationProcess(final JFrame processFrame, final BalanceDriver balanceDriver) {
        super(processFrame);

        this.balanceDriver = balanceDriver;
    }

    @Override
    protected void initSteps() {
        addStep(new CompetitionInformationStep(CompetitionInformationStep.ONLY_WEIGHING, getProcessFrame()));
        addStep(new PersistancyCreationStep());
        addStep(new RegistrationStep());
        addStep(new WeighingExportStep());
    }

}
