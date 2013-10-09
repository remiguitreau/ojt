package com.ojt.balance.kern;

import com.ojt.balance.BalanceFrame;

/**
 * @author FMo
 * @since 20 avr. 2009 (FMo) : Création
 */
public class DEFrame implements BalanceFrame {
	final TYPE type;

	private float weight;

	public void setStableWeight(final boolean stable) {
		this.stable = stable;
	}

	private boolean stable;

	// -------------------------------------------------------------------------
	// constructeur
	// -------------------------------------------------------------------------

	public DEFrame(final TYPE type) {
		this.type = type;
	}

	// -------------------------------------------------------------------------
	// Implémentation de l'interface BalanceFrame
	// -------------------------------------------------------------------------

	@Override
	public TYPE getType() {
		return type;
	}

	@Override
	public float getWeight() {
		return weight;
	}

	public void setWeight(final float weight) {
		this.weight = weight;
	}

	@Override
	public boolean isStableWeight() {
		return stable;
	}
}
