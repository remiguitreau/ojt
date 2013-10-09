package com.ojt.ui;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

import com.ojt.Competitor;
import com.ojt.GradeBelt;

public class GradeBeltLabel extends JLabel {

	private final Competitor competitor;

	public GradeBeltLabel(final Competitor competitor) {
		this.competitor = competitor;
		init();
	}
	
	private void init() {
		if(competitor.getGradeBelt() != null) {
			setIcon(retrieveImageFromGrade(competitor.getGradeBelt()));
			setToolTipText(competitor.getGradeBelt().abbreviation);
		}
		setHorizontalAlignment(SwingConstants.CENTER);
	}
	
	public static ImageIcon retrieveBigImageFromGrade(GradeBelt gradeBelt) {
		switch (gradeBelt) {
		case WHITE_YELLOW:
			return OjtUiConstants.BJ_ICON_MEDIUM;
		case YELLOW:
			return OjtUiConstants.J_ICON_MEDIUM;
		case YELLOW_ORANGE:
			return OjtUiConstants.JO_ICON_MEDIUM;
		case ORANGE:
			return OjtUiConstants.O_ICON_MEDIUM;
		case ORANGE_GREEN:
			return OjtUiConstants.OV_ICON_MEDIUM;
		case GREEN:
			return OjtUiConstants.V_ICON_MEDIUM;
		case BLUE:
			return OjtUiConstants.B_ICON_MEDIUM;
		case BROWN:
			return OjtUiConstants.M_ICON_MEDIUM;
		case FIRST_DAN:
			return OjtUiConstants.FIRST_DAN_ICON_MEDIUM;
		case SECOND_DAN:
			return OjtUiConstants.SECOND_DAN_ICON_MEDIUM;
		case THIRD_DAN:
			return OjtUiConstants.THIRD_DAN_ICON_MEDIUM;
		case FOURTH_DAN:
			return OjtUiConstants.FOURTH_DAN_ICON_MEDIUM;
		case FIFTH_DAN:
			return OjtUiConstants.FIFTH_DAN_ICON_MEDIUM;
		case SIXTH_DAN:
			return OjtUiConstants.SIXTH_DAN_ICON_MEDIUM;
		default:
			return null;
	}
	}
	public static ImageIcon retrieveImageFromGrade(GradeBelt gradeBelt) {
		switch (gradeBelt) {
			case WHITE_YELLOW:
				return OjtUiConstants.BJ_ICON;
			case YELLOW:
				return OjtUiConstants.J_ICON;
			case YELLOW_ORANGE:
				return OjtUiConstants.JO_ICON;
			case ORANGE:
				return OjtUiConstants.O_ICON;
			case ORANGE_GREEN:
				return OjtUiConstants.OV_ICON;
			case GREEN:
				return OjtUiConstants.V_ICON;
			case BLUE:
				return OjtUiConstants.B_ICON;
			case BROWN:
				return OjtUiConstants.M_ICON;
			case FIRST_DAN:
				return OjtUiConstants.FIRST_DAN_ICON;
			case SECOND_DAN:
				return OjtUiConstants.SECOND_DAN_ICON;
			case THIRD_DAN:
				return OjtUiConstants.THIRD_DAN_ICON;
			case FOURTH_DAN:
				return OjtUiConstants.FOURTH_DAN_ICON;
			case FIFTH_DAN:
				return OjtUiConstants.FIFTH_DAN_ICON;
			case SIXTH_DAN:
				return OjtUiConstants.SIXTH_DAN_ICON;
			default:
				return null;
		}
	}
}
