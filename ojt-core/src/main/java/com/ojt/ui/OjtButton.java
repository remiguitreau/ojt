package com.ojt.ui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JButton;
import javax.swing.SwingConstants;

public class OjtButton extends JButton {

	public OjtButton(final String text) {
		super(text);
		setFocusable(false);
		setFont(OjtUiConstants.KATANA_FONT.deriveFont(18f));
		setBorder(null);
		setContentAreaFilled(false);
		setVerticalTextPosition(SwingConstants.CENTER);
		setHorizontalTextPosition(SwingConstants.CENTER);
	}

	@Override
	protected void paintComponent(final Graphics g) {
		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		if (getModel().isPressed()) {
			g.drawImage(OjtUiConstants.PUSHED_BUTTON_BACKGROUND_ICON.getImage(), 0, 0, getWidth(),
					getHeight(), null);
		} else {
			g.drawImage(OjtUiConstants.BUTTON_BACKGROUND_ICON.getImage(), 0, 0, getWidth(), getHeight(), null);
		}
		if (getHeight() < 25) {
			setFont(OjtUiConstants.KATANA_FONT.deriveFont(15f));
		}
		super.paintComponent(g);
		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
	}
}
