/**
 * 
 */
package com.ojt.ui;

import java.awt.Graphics;

import javax.swing.JComponent;

/**
 * Slider which allow to manage several lists of elements.
 * @author Rémi "DwarfConan" Guitreau
 * @since 8 avr. 08 : Création.
 */
public class MultiListSlider extends JComponent {

	private static final String uiClassID = "MultiListSliderUI";

	// -------------------------------------------------------------------------
	// Attributs
	// -------------------------------------------------------------------------
	private MultiListSlidable multiListSlidable;

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------
	public MultiListSlider() {
		this(null);
	}

	public MultiListSlider(final MultiListSlidable multiListSlidable) {
		super();

		this.multiListSlidable = multiListSlidable;
		updateUI();
	}

	// -------------------------------------------------------------------------
	// L&F
	// -------------------------------------------------------------------------
	public void setUI(final MultiListSliderUI ui) {
		super.setUI(ui);
	}

	public MultiListSliderUI getUI() {
		return (MultiListSliderUI) ui;
	}

	@Override
	public void updateUI() {
		setUI(MultiListSliderUI.createUI(this));
	}

	@Override
	public String getUIClassID() {
		return uiClassID;
	}

	@Override
	public void paint(final Graphics g) {
		getUI().paint(g, this);
	}

	// -------------------------------------------------------------------------
	// Public
	// -------------------------------------------------------------------------

	/**
	 * @return Returns the multiListSlidable.
	 */
	public final MultiListSlidable getMultiListSlidable() {
		return multiListSlidable;
	}

	/**
	 * @param multiListSlidable The multiListSlidable to set.
	 */
	public final void setMultiListSlidable(final MultiListSlidable multiListSlidable) {
		this.multiListSlidable = multiListSlidable;
		getUI().updateSize();
	}

	public void listIgnoredChanged(final int listIdx, final boolean ignored) {
		getUI().listIgnoredChanged(listIdx, ignored);
	}

	public void listsChanged() {
		getUI().updateSliderButtons();
		repaint();
	}
}
