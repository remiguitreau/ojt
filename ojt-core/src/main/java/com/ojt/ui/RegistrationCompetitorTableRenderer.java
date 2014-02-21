package com.ojt.ui;

import com.ojt.Competitor;
import com.ojt.CompetitorList;

import java.awt.Component;

import javax.swing.JTable;

public class RegistrationCompetitorTableRenderer extends CompetitorTableRenderer {
    private final CompetitorList registeredCompetitorsList;

    public RegistrationCompetitorTableRenderer(final CompetitorList registeredCompetitorsList) {
        this.registeredCompetitorsList = registeredCompetitorsList;
    }

    @Override
    public Component getTableCellRendererComponent(final JTable table, final Object value,
            final boolean isSelected, final boolean hasFocus, final int row, final int col) {
        final Competitor competitor = (Competitor) value;
        if (col == RegistrationCompetitorTableModel.REGISTRATION_INDEX) {
            return super.getTableCellRendererComponent(table, registeredCompetitorsList.contains(competitor),
                    isSelected, hasFocus, row, col);
        }
        return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
    }

}
