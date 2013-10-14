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
public class WeighingAndSortProcess extends AbstractProcess {

    private final BalanceDriver balanceDriver;

    public WeighingAndSortProcess(final JFrame processFrame, final BalanceDriver balanceDriver) {
        super(processFrame);

        this.balanceDriver = balanceDriver;
    }

    @Override
    protected void initSteps() {
        addStep(new CompetitionInformationStep(CompetitionInformationStep.WEIGHING_AND_SORT));
        addStep(new PersistancyCreationStep());
        addStep(new WeighingStep(true, balanceDriver));
        addStep(new CompetitorsGroupsCreationStep());
        addStep(new GroupOrganizerStep());
        addStep(new DiscardCompetitorsGroupStep());
        addStep(new PouleExportStep());
    }

}
