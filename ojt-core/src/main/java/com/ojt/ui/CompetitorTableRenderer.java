package com.ojt.ui;

import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import sun.swing.DefaultLookup;

import com.ojt.Competitor;
import com.ojt.GradeBelt;

/**
 * Renderer pour la table des compétiteurs
 * @author CDa
 * @since 24 janv. 2009 (CDa) : Création
 */
public class CompetitorTableRenderer extends DefaultTableCellRenderer {
	@Override
	public Component getTableCellRendererComponent(final JTable table, final Object value,
			final boolean isSelected, final boolean hasFocus, final int row, final int col) {
		final Competitor competitor = (Competitor) value;
		setIcon(null);
		Object field = "";
		if (col == 0) {
			field = competitor.getDisplayName();
		} else if (col == 1) {
			field = competitor.getLicenseCode();
		} else if (col == 2) {
			field = competitor.getClub();
		} else if (col == CompetitorTableModel.GRADE_INDEX) {
			GradeBeltLabel gradeBeltLabel = new GradeBeltLabel(competitor);
			gradeBeltLabel.setBackground(isSelected ? table.getSelectionBackground() : null);
			gradeBeltLabel.setOpaque(true);
			return gradeBeltLabel;
		} else if (col == CompetitorTableModel.WEIGHT_INDEX) {
			field = competitor.getWeight();
		}
			
		

		super.getTableCellRendererComponent(table, field, isSelected, hasFocus, row, col);
		return this;
	}

	
}
