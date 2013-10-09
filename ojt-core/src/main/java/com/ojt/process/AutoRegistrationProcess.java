/**
 * 
 */
package com.ojt.process;

import com.ojt.balance.BalanceDriver;

import javax.swing.JFrame;

/**
 * Processus de pesée.
 * @author Rémi "DwarfConan" Guitreau
 * @since 17 oct. 2009 : Création
 */
public class AutoRegistrationProcess extends AbstractProcess {

    private final BalanceDriver balanceDriver;

    public AutoRegistrationProcess(final JFrame processFrame, final BalanceDriver balanceDriver) {
        super(processFrame);

        this.balanceDriver = balanceDriver;
    }

    @Override
    protected void initSteps() {
        addStep(new CompetitionInformationStep(CompetitionInformationStep.ONLY_WEIGHING));
        addStep(new PersistancyCreationStep());
        addStep(new AutoRegistrationStep());
        addStep(new WeighingExportStep());
    }

}
