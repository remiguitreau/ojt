/**
 * 
 */
package com.ojt.process;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import com.ojt.OJTConfiguration;
import com.ojt.OjtConstants;
import com.ojt.tools.FileNameComposer;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * Etape d'export de la pesée
 * @author Rémi "DwarfConan" Guitreau
 * @since 17 oct. 2009 : Création
 */
public class WeighingExportStep extends AbstractStep {

	private final Logger logger = Logger.getLogger(getClass());

	private JPanel stepPanel;

	private JLabel exportLabel;

	private JLabel addExportLabel;

	public WeighingExportStep() {
		super();

		initStepPanel();
	}

	// ---------------------------------------------------------
	// Implémentation de AbstractStep
	// ---------------------------------------------------------

	@Override
	public JComponent getStepComponent() {
		return stepPanel;
	}

	@Override
	public void process(final CompetitionDatas competitionDatas) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				exportLabel.setForeground(Color.black);
				final File weighingPostFile = new File(OjtConstants.WEIGHING_DIRECTORY,
						FileNameComposer.composeWeighingPostFileName(
								competitionDatas.getCompetitionDescriptor(),
								competitionDatas.getWeighingPost(),
								competitionDatas.getCompetitionFile().getName().substring(
										competitionDatas.getCompetitionFile().getName().lastIndexOf('.'))));
				if (!weighingPostFile.getParentFile().exists()) {
					weighingPostFile.getParentFile().mkdirs();
				}
				try {
					FileUtils.copyFile(competitionDatas.getCompetitionFile(), weighingPostFile);
					exportLabel.setText("Pesée enregistrée dans le fichier : "
							+ weighingPostFile.getAbsolutePath());
				} catch (final Exception ex) {
					logger.error("Erreur lors de la création du fichier de pesée : " + weighingPostFile, ex);
					exportLabel.setForeground(Color.red);
					exportLabel.setText("Erreur lors de l'enregistrement de la pesée.");
				}

				addExportLabel.setForeground(Color.black);
				addExportLabel.setVisible(false);
				final String addWeighingSaveDirPath = OJTConfiguration.getInstance().getProperty(
						OJTConfiguration.ADD_WEIGHING_SAVE_DIRECTORY_PATH);
				if ((addWeighingSaveDirPath != null) && !addWeighingSaveDirPath.trim().isEmpty()) {
					addExportLabel.setVisible(true);
					try {
						final File addWeighingPostFile = new File(new File(addWeighingSaveDirPath),
								FileNameComposer.composeWeighingPostFileName(
										competitionDatas.getCompetitionDescriptor(),
										competitionDatas.getWeighingPost(),
										competitionDatas.getCompetitionFile().getName().substring(
												competitionDatas.getCompetitionFile().getName().lastIndexOf(
														'.'))));
						FileUtils.copyFile(competitionDatas.getCompetitionFile(), addWeighingPostFile);
						addExportLabel.setText("Pesée enregistrée dans le répertoire supplémentaire : "
								+ addWeighingPostFile.getAbsolutePath());
					} catch (final Exception ex) {
						logger.error(
								"Erreur lors de la création du fichier de pesée dans le répertoire supplémentaire de sauvegarde : "
										+ addWeighingSaveDirPath, ex);
						addExportLabel.setForeground(Color.red);
						addExportLabel.setText("Erreur lors de l'enregistrement de la pesée dans le répertoire supplémentaire.");
					}
				}

				stepFinish();
			}
		});
	}

	@Override
	public boolean finalizeStep() {
		return true;
	}

	@Override
	public String getTitle() {
		return "Enregistrement de la pesée";
	}

	// ---------------------------------------------------------
	// Privées
	// ---------------------------------------------------------
	private void initStepPanel() {
		stepPanel = new JPanel(new GridBagLayout());
		exportLabel = new JLabel();
		stepPanel.add(exportLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.SOUTH,
				GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		addExportLabel = new JLabel();
		stepPanel.add(addExportLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH,
				GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
	}

}
