package com.ojt.ui;

import com.ojt.CompetitionDescriptor;
import com.ojt.Competitor;
import com.ojt.CompetitorList;
import com.ojt.dao.CompetitorsDao;

import javax.swing.JOptionPane;

public class RegistrationCompetitorTableModel extends CompetitorTableModel {

    public final static int REGISTRATION_INDEX = 5;

    private final String[] columnNames = { "Nom", "Licence", "Club", "Grade", "Poids", "Insc." };

    private final CompetitorList registeredCompetitorList;

    private final CompetitorsDao registeredCompetitorsDao;

    public RegistrationCompetitorTableModel(final CompetitionDescriptor competitionDescriptor,
            final CompetitorList competitorList, final CompetitorsDao competitorsDao,
            final CompetitorWeightListener competitorWeightListener,
            final CompetitorList registeredCompetitorList, final CompetitorsDao registeredCompetitorsDao) {
        super(competitionDescriptor, competitorList, competitorsDao, competitorWeightListener);
        this.registeredCompetitorList = registeredCompetitorList;
        this.registeredCompetitorsDao = registeredCompetitorsDao;
    }

    @Override
    public String[] getColumnNames() {
        return columnNames;
    }

    @Override
    public boolean isCellEditable(final int row, final int col) {
        if (col == REGISTRATION_INDEX) {
            return true;
        }
        return super.isCellEditable(row, col);
    }

    @Override
    public void setValueAt(final Object value, final int rowIndex, final int columnIndex) {
        if (columnIndex == REGISTRATION_INDEX) {
            final Competitor targetCompetitor = getData().get(rowIndex);
            if (value instanceof Boolean && ((Boolean) value).booleanValue()) {
                registeredCompetitorList.add(targetCompetitor);
                registeredCompetitorsDao.createCompetitor(targetCompetitor);
            } else if (JOptionPane.showConfirmDialog(null, "Voulez-vous vraiment désinscrire "
                    + targetCompetitor + " ?") == JOptionPane.YES_OPTION) {
                registeredCompetitorList.remove(targetCompetitor);
                registeredCompetitorsDao.deleteCompetitor(targetCompetitor);
            }
        }
        super.setValueAt(value, rowIndex, columnIndex);
    }
}
