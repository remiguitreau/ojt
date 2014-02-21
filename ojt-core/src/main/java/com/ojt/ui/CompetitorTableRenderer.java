package com.ojt.ui;

import com.ojt.Competitor;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * Renderer pour la table des compétiteurs
 * @author CDa
 * @since 24 janv. 2009 (CDa) : Création
 */
public class CompetitorTableRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(final JTable table, final Object value,
            final boolean isSelected, final boolean hasFocus, final int row, final int col) {
        setIcon(null);
        if (col == 0) {
            return super.getTableCellRendererComponent(table, ((Competitor) value).getDisplayName(),
                    isSelected, hasFocus, row, col);
        } else if (col == 1) {
            return super.getTableCellRendererComponent(table, ((Competitor) value).getLicenseCode(),
                    isSelected, hasFocus, row, col);
        } else if (col == 2) {
            return super.getTableCellRendererComponent(table, ((Competitor) value).getClub(), isSelected,
                    hasFocus, row, col);
        } else if (col == CompetitorTableModel.GRADE_INDEX) {
            final GradeBeltLabel gradeBeltLabel = new GradeBeltLabel((Competitor) value);
            gradeBeltLabel.setBackground(isSelected ? table.getSelectionBackground() : null);
            gradeBeltLabel.setOpaque(true);
            return gradeBeltLabel;
        } else if (col == CompetitorTableModel.WEIGHT_INDEX) {
            return super.getTableCellRendererComponent(table, ((Competitor) value).getWeight(), isSelected,
                    hasFocus, row, col);
        }

        return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
    }

}
