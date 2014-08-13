/**
 *
 */
package com.ojt.process;

import org.apache.log4j.Logger;

import com.ojt.OJTLauncher;
import com.ojt.dao.CompetitorRetrieveException;
import com.ojt.ui.OjtButton;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * Processus abstrait
 * @author Rémi "DwarfConan" Guitreau
 * @since 17 oct. 2009 : Création
 */
public abstract class AbstractProcess implements Process, StepListener {

    private static final GridBagConstraints STEP_PANEL_CONSTRAINTS = new GridBagConstraints(0, 0, 1, 1,
            100.0, 100.0, GridBagConstraints.SOUTH, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);

    private final static Dimension BUTTON_SIZE = new Dimension(170, 22);

    private final static int FORWARD = 0;

    private final static int BACKWARD = 1;

    // ---------------------------------------------------------
    // Attributs
    // ---------------------------------------------------------
    private final Logger logger = Logger.getLogger(getClass());

    private final List<Step> steps = new LinkedList<Step>();

    private final List<ProcessListener> processListeners = new ArrayList<ProcessListener>();

    private final Executor executor = Executors.newSingleThreadExecutor();

    private final JFrame processFrame;

    private JPanel processPanel;

    private JComponent stepComponent;

    private JButton nextButton;

    private JButton previousButton;

    private JLabel stepTitle;

    private Step currentStep;

    private int stepLinkage = FORWARD;

    private CompetitionDatas competitionDatas;

    // ---------------------------------------------------------
    // Constructeur
    // ---------------------------------------------------------

    public AbstractProcess(final JFrame processFrame) {
        super();

        this.processFrame = processFrame;
    }

    // ---------------------------------------------------------
    // Implémentation de Process
    // ---------------------------------------------------------
    JFrame getProcessFrame() {
        return processFrame;
    }

    @Override
    public void addStep(final Step step) {
        steps.add(step);
    }

    @Override
    public void launch() {
        initSteps();
        competitionDatas = new CompetitionDatas();
        if (!steps.isEmpty()) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    initProcessPanel();
                    processFrame.setContentPane(processPanel);
                    processFrame.pack();
                }
            });
            launchStep(0);
        }
    }

    @Override
    public void addProcessListener(final ProcessListener processListener) {
        processListeners.add(processListener);
    }

    // ---------------------------------------------------------
    // Implémentation de StepListener
    // ---------------------------------------------------------
    @Override
    public void stepFinished() {
        if (currentStep.getStepComponent() == null) {
            if (stepLinkage == FORWARD) {
                launchNextStep();
            } else {
                launchPreviousStep();
            }
        } else {
            nextButton.setEnabled(true);
            previousButton.setEnabled(true);
        }
    }

    @Override
    public void stepProcessing() {
        nextButton.setEnabled(false);
        previousButton.setEnabled(currentStep.goBackBeforeEnd());
    }

    // ---------------------------------------------------------
    // Méthodes abstraites
    // ---------------------------------------------------------
    protected abstract void initSteps();

    // ---------------------------------------------------------
    // Privée
    // ---------------------------------------------------------
    private void launchStep(final int stepIndex) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    currentStep = steps.get(stepIndex);
                    currentStep.addStepListener(AbstractProcess.this);
                    // Si on est en train de revenir en arrière, on ne lance que
                    // les étapes graphiques.
                    if (stepLinkage == BACKWARD && currentStep.getStepComponent() == null) {
                        launchPreviousStep();
                        return;
                    }
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            stepTitle.setText(currentStep.getTitle());
                        }
                    });
                    if (currentStep.getStepComponent() != null) {
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                processPanel.remove(stepComponent);
                                stepComponent = currentStep.getStepComponent();
                                processPanel.add(stepComponent, STEP_PANEL_CONSTRAINTS);
                                processPanel.revalidate();
                                processPanel.repaint();
                                stepProcessing();
                            }
                        });
                    }
                    currentStep.process(competitionDatas);
                } catch (final CompetitorRetrieveException ex) {
                    OJTLauncher.showError("Mauvais format de fichier, impossible de passer à l'étape '"
                            + currentStep.getTitle() + "' : "
                            + (ex.getCause() == null ? ex.getMessage() : ex.getCause().getMessage()),
                            "Erreur inatendue");
                    logger.error("Bad file format:", ex);
                } catch (final Exception ex) {
                    logger.error("Erreur inattendue lors du lancement d'une étape.", ex);
                    OJTLauncher.showError(
                            "Erreur inattendue, impossible de passer à l'étape '" + currentStep.getTitle()
                                    + "'", "Erreur inatendue");
                }
            }
        });
    }

    private void launchNextStep() {
        // On demande à l'étape courante si on peut passer à la suite
        if (currentStep.finalizeStep()) {
            stepLinkage = FORWARD;
            final int stepIndex = steps.indexOf(currentStep);
            if (stepIndex < steps.size() - 1) {
                launchStep(stepIndex + 1);
            } else {
                fireProcessEnded();
            }
        }
    }

    private void launchPreviousStep() {
        stepLinkage = BACKWARD;
        final int stepIndex = steps.indexOf(currentStep);
        if (stepIndex > 0) {
            launchStep(stepIndex - 1);
        } else {
            fireProcessCanceled();
        }
    }

    private void initProcessPanel() {
        processPanel = new JPanel(new GridBagLayout());
        final JPanel commandPanel = new JPanel(new GridBagLayout());
        previousButton = new OjtButton("<< Précédent");
        previousButton.setPreferredSize(BUTTON_SIZE);
        previousButton.setMinimumSize(BUTTON_SIZE);
        previousButton.setSize(BUTTON_SIZE);
        previousButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                launchPreviousStep();
            }
        });
        commandPanel.add(previousButton, new GridBagConstraints(0, 0, 1, 1, 0.5, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

        nextButton = new OjtButton("Suivant >>");
        nextButton.setPreferredSize(BUTTON_SIZE);
        nextButton.setMinimumSize(BUTTON_SIZE);
        nextButton.setSize(BUTTON_SIZE);
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                launchNextStep();
            }
        });
        commandPanel.add(nextButton, new GridBagConstraints(2, 0, 1, 1, 0.5, 0.0, GridBagConstraints.EAST,
                GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

        stepTitle = new JLabel("");
        stepTitle.setFont(stepTitle.getFont().deriveFont(Font.ITALIC));
        commandPanel.add(stepTitle, new GridBagConstraints(1, 0, 1, 1, 0.5, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

        stepComponent = new JPanel();
        stepComponent.setPreferredSize(new Dimension(750, 500));
        processPanel.add(stepComponent, STEP_PANEL_CONSTRAINTS);
        processPanel.add(commandPanel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.SOUTH,
                GridBagConstraints.HORIZONTAL, new Insets(10, 50, 10, 50), 0, 0));
    }

    private void fireProcessCanceled() {
        for (final ProcessListener processListener : processListeners) {
            if (processListener != null) {
                processListener.processCanceled();
            }
        }
    }

    private void fireProcessEnded() {
        for (final ProcessListener processListener : processListeners) {
            if (processListener != null) {
                processListener.processEnded();
            }
        }
        getProcessFrame().setTitle("Open Judo Tournament");
    }
}
