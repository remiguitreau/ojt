/**
 * 
 */
package com.ojt.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;

import javax.swing.JLabel;

/**
 * Bouton pour modifier la composition des listes.
 * @author Rémi "DwarfConan" Guitreau
 * @since 14 avr. 08 : Création.
 */
public class MultiListSliderButton extends JLabel {

	// -------------------------------------------------------------------------
	// Attributs
	// -------------------------------------------------------------------------
	private final int listIndex;

	// -------------------------------------------------------------------------
	// Constructeurs
	// -------------------------------------------------------------------------

	public MultiListSliderButton(final int listIndex) {
		super();

		this.listIndex = listIndex;
	}

	// -------------------------------------------------------------------------
	// Public
	// -------------------------------------------------------------------------

	public int getListIndex() {
		return listIndex;
	}

	@Override
	public void paint(final Graphics g) {
		if (isVisible()) {
			final Polygon polygon = new Polygon();
			polygon.addPoint(getBounds().x, getBounds().y + getPreferredSize().height / 2);
			polygon.addPoint(getBounds().x + getPreferredSize().width, getBounds().y);
			polygon.addPoint(getBounds().x + getPreferredSize().width, getBounds().y
					+ getPreferredSize().height);
			g.setColor(Color.ORANGE);
			g.fillPolygon(polygon);
		}
	}

	@Override
	protected void paintComponent(final Graphics g) {
		if (isVisible()) {
			final Polygon polygon = new Polygon();
			polygon.addPoint(getBounds().x, getBounds().y + getPreferredSize().height / 2);
			polygon.addPoint(getBounds().x + getPreferredSize().width, getBounds().y);
			polygon.addPoint(getBounds().x + getPreferredSize().width, getBounds().y
					+ getPreferredSize().height);
			g.setColor(Color.ORANGE);
			g.fillPolygon(polygon);
		}
	}

	public void move(final Graphics g, final Point p) {
		g.clearRect(getBounds().x, getBounds().y, getBounds().width, getBounds().height);
		setLocation(p);
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(15, 15);
	}

	@Override
	public Dimension getMinimumSize() {
		return new Dimension(5, 5);
	}

	@Override
	public Dimension getMaximumSize() {
		return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
	}

	@Override
	public boolean isFocusTraversable() {
		return true;
	}
}
