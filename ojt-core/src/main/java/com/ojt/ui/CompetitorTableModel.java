package com.ojt.ui;

import org.apache.log4j.Logger;

import com.ojt.CompetitionDescriptor;
import com.ojt.CompetitorList;
import com.ojt.CompetitorSex;
import com.ojt.GradeBelt;
import com.ojt.OJTLauncher;
import com.ojt.dao.CompetitorsDao;
import com.ojt.dao.ffjdadat.FFJDADatConstants;

import java.awt.Toolkit;

import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;

/**
 * Model pour la table des compétiteurs
 * @author CDa
 * @since 24 janv. 2009 (CDa) : Création
 */
public class CompetitorTableModel extends AbstractTableModel {

    public final static int WEIGHT_INDEX = 4;

    public final static int GRADE_INDEX = 3;

    // -------------------------------------------------------------------------
    // Propriétés de l'objet
    // -------------------------------------------------------------------------

    private final CompetitorList data;

    private final CompetitorsDao competitorsDao;

    private final String[] columnNames = { "Nom", "Licence", "Club", "Grade", "Poids" };

    private final Logger logger = Logger.getLogger(getClass());

    private final CompetitorWeightListener competitorWeightListener;

    // -------------------------------------------------------------------------
    // Constructeur
    // -------------------------------------------------------------------------

    private final CompetitionDescriptor competitionDescriptor;

    /**
     * Constructeur par défaut
     * @param competitorList
     */
    public CompetitorTableModel(final CompetitionDescriptor competitionDescriptor,
            final CompetitorList competitorList, final CompetitorsDao competitorsDao,
            final CompetitorWeightListener competitorWeightListener) {
        super();
        this.competitionDescriptor = competitionDescriptor;
        data = competitorList;
        this.competitorsDao = competitorsDao;
        this.competitorWeightListener = competitorWeightListener;
    }

    @Override
    public final String getColumnName(final int col) {
        return getColumnNames()[col];
    }

    public String[] getColumnNames() {
        return columnNames;
    }

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return getColumnNames().length;
    }

    @Override
    public boolean isCellEditable(final int row, final int col) {
        return col == WEIGHT_INDEX;
    }

    @Override
    public Object getValueAt(final int row, final int col) {
        return data.get(row);
    }

    public CompetitorList getData() {
        return data;
    }

    @Override
    public void setValueAt(final Object value, final int rowIndex, final int columnIndex) {
        try {
            switch (columnIndex) {
                case WEIGHT_INDEX:
                    boolean valueChanged = false;
                    if (value.toString().isEmpty()) {
                        data.get(rowIndex).setWeight(null);
                        valueChanged = true;
                    } else {
                        final float weight = Float.parseFloat(value.toString().replaceAll(",", "."));
                        if (weight > 0 && data.get(rowIndex).getWeightAsFloat() != weight) {
                            if (!competitionDescriptor.getCategory().isCompetitorAccepted(data.get(rowIndex))) {
                                if (JOptionPane.showConfirmDialog(
                                        null,
                                        "Attention vous allez saisir le poids pour un compétiteur \""
                                                + data.get(rowIndex).getCategory().getHumanName()
                                                + "\""
                                                + (data.get(rowIndex).getBirthDate() != null ? " - Date de naissance : "
                                                        + FFJDADatConstants.BIRTH_DATE_FORMAT.format(data.get(
                                                                rowIndex).getBirthDate())
                                                        : "")
                                                + (data.get(rowIndex).getSex() != null
                                                        && data.get(rowIndex).getSex() != CompetitorSex.UNKNOWN ? " - Sexe : "
                                                        + data.get(rowIndex).getSex().getHumanName()
                                                        : "") + "\nCatégorie de la compétition : "
                                                + competitionDescriptor.getCategory().getHumanName(),
                                        "OJT - Avertissement", JOptionPane.OK_CANCEL_OPTION,
                                        JOptionPane.WARNING_MESSAGE) == JOptionPane.CANCEL_OPTION) {
                                    return;
                                }
                            }
                            data.get(rowIndex).setWeight(Float.valueOf(weight));
                            valueChanged = true;
                        }
                    }
                    if (competitorsDao != null && valueChanged) {
                        competitorsDao.updateCompetitor(data.get(rowIndex));
                    }
                    if (valueChanged) {
                        Toolkit.getDefaultToolkit().beep();
                        competitorWeightListener.newWeightEntered();
                    }
                    break;
                case GRADE_INDEX:
                    if (value != data.get(rowIndex).getGradeBelt()) {
                        data.get(rowIndex).setGradeBelt((GradeBelt) value);
                        if (competitorsDao != null) {
                            competitorsDao.updateCompetitor(data.get(rowIndex));
                        }
                    }
                    break;
            }
        } catch (final Exception e) {
            logger.error("Impossible de mettre à jour la persistance.", e);
            OJTLauncher.showError("Impossible de mettre à jour la persistance", "Erreur");
        }
        super.setValueAt(value, rowIndex, columnIndex);
    }
}
