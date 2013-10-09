/**
 * 
 */
package com.ojt.ui;

import java.awt.Color;

/**
 * Classe utilitaires pour les listes multiples.
 * @author Rémi "DwarfConan" Guitreau
 * @since 1 mai 08 : Création
 */
public class MultiListUtilities {

	public static Color getListColor(final int listIdx) {
		switch (listIdx % 4) {
			case 0:
				return new Color(65, 45, 242);
			case 1:
				return new Color(234, 222, 2);
			case 2:
				return new Color(67, 169, 69);
			case 3:
				return new Color(197, 39, 51);
			default:
				return Color.GRAY;
		}
	}

	public static Color getBackListColor(final int listIdx) {
		switch (listIdx % 4) {
			case 0:
				return new Color(68, 178, 255);
			case 1:
				return new Color(255, 235, 140);
			case 2:
				return new Color(126, 220, 126);
			case 3:
				return new Color(255, 105, 85);
			default:
				return Color.GRAY;
		}
	}

	// ---------------------------------------------------------
	// Non instantiable.
	// ---------------------------------------------------------

	private MultiListUtilities() {
		super();
	}
}
