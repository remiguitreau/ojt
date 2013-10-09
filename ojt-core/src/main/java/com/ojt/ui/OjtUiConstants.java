package com.ojt.ui;

import org.apache.log4j.Logger;

import java.awt.Color;
import java.awt.Font;

import javax.swing.ImageIcon;

public final class OjtUiConstants {

	private final static Logger LOGGER = Logger.getLogger(OjtUiConstants.class);

	public static final ImageIcon BUTTON_BACKGROUND_ICON = new ImageIcon(
			OjtUiConstants.class.getResource("bkg-button.gif"));

	public static final ImageIcon PUSHED_BUTTON_BACKGROUND_ICON = new ImageIcon(
			OjtUiConstants.class.getResource("bkg-button-pushed.gif"));

	public static final ImageIcon PANEL_BACKGROUND = new ImageIcon(
			OjtUiConstants.class.getResource("ojt-bkg.png"));
	
	public static final ImageIcon BJ_ICON = new ImageIcon(
			OjtUiConstants.class.getResource("grades/bj.png"));
	public static final ImageIcon J_ICON = new ImageIcon(
			OjtUiConstants.class.getResource("grades/j.png"));
	public static final ImageIcon JO_ICON = new ImageIcon(
			OjtUiConstants.class.getResource("grades/jo.png"));
	public static final ImageIcon O_ICON = new ImageIcon(
			OjtUiConstants.class.getResource("grades/o.png"));
	public static final ImageIcon OV_ICON = new ImageIcon(
			OjtUiConstants.class.getResource("grades/ov.png"));
	public static final ImageIcon V_ICON = new ImageIcon(
			OjtUiConstants.class.getResource("grades/v.png"));
	public static final ImageIcon B_ICON = new ImageIcon(
			OjtUiConstants.class.getResource("grades/b.png"));
	public static final ImageIcon M_ICON = new ImageIcon(
			OjtUiConstants.class.getResource("grades/m.png"));
	public static final ImageIcon FIRST_DAN_ICON = new ImageIcon(
			OjtUiConstants.class.getResource("grades/1d.png"));
	public static final ImageIcon SECOND_DAN_ICON = new ImageIcon(
			OjtUiConstants.class.getResource("grades/2d.png"));
	public static final ImageIcon THIRD_DAN_ICON = new ImageIcon(
			OjtUiConstants.class.getResource("grades/3d.png"));
	public static final ImageIcon FOURTH_DAN_ICON = new ImageIcon(
			OjtUiConstants.class.getResource("grades/4d.png"));
	public static final ImageIcon FIFTH_DAN_ICON = new ImageIcon(
			OjtUiConstants.class.getResource("grades/5d.png"));
	public static final ImageIcon SIXTH_DAN_ICON = new ImageIcon(
			OjtUiConstants.class.getResource("grades/6d.png"));
	
	public static final ImageIcon BJ_ICON_MEDIUM = new ImageIcon(
			OjtUiConstants.class.getResource("grades/bj-medium.png"));
	public static final ImageIcon J_ICON_MEDIUM = new ImageIcon(
			OjtUiConstants.class.getResource("grades/j-medium.png"));
	public static final ImageIcon JO_ICON_MEDIUM = new ImageIcon(
			OjtUiConstants.class.getResource("grades/jo-medium.png"));
	public static final ImageIcon O_ICON_MEDIUM = new ImageIcon(
			OjtUiConstants.class.getResource("grades/o-medium.png"));
	public static final ImageIcon OV_ICON_MEDIUM = new ImageIcon(
			OjtUiConstants.class.getResource("grades/ov-medium.png"));
	public static final ImageIcon V_ICON_MEDIUM = new ImageIcon(
			OjtUiConstants.class.getResource("grades/v-medium.png"));
	public static final ImageIcon B_ICON_MEDIUM = new ImageIcon(
			OjtUiConstants.class.getResource("grades/b-medium.png"));
	public static final ImageIcon M_ICON_MEDIUM = new ImageIcon(
			OjtUiConstants.class.getResource("grades/m-medium.png"));
	public static final ImageIcon FIRST_DAN_ICON_MEDIUM = new ImageIcon(
			OjtUiConstants.class.getResource("grades/1d-medium.png"));
	public static final ImageIcon SECOND_DAN_ICON_MEDIUM = new ImageIcon(
			OjtUiConstants.class.getResource("grades/2d-medium.png"));
	public static final ImageIcon THIRD_DAN_ICON_MEDIUM = new ImageIcon(
			OjtUiConstants.class.getResource("grades/3d-medium.png"));
	public static final ImageIcon FOURTH_DAN_ICON_MEDIUM = new ImageIcon(
			OjtUiConstants.class.getResource("grades/4d-medium.png"));
	public static final ImageIcon FIFTH_DAN_ICON_MEDIUM = new ImageIcon(
			OjtUiConstants.class.getResource("grades/5d-medium.png"));
	public static final ImageIcon SIXTH_DAN_ICON_MEDIUM = new ImageIcon(
			OjtUiConstants.class.getResource("grades/6d-medium.png"));
	

	public final static Color OJT_GREEN = new Color(172, 229, 0);

	public static Font KATANA_FONT = Font.getFont("Arial");

	static {
		try {
			KATANA_FONT = Font.createFont(Font.TRUETYPE_FONT,
					OjtUiConstants.class.getResourceAsStream("Katanf.ttf"));
		} catch (final Exception ex) {
			LOGGER.error("Error while loading Katana font", ex);
		}
	}
}
