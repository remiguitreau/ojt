package com.ojt.connector;

import java.util.ArrayList;
import java.util.List;

public abstract class Connector {
	// Les listeners
	protected List<ConnectorListener> listeners;

	public Connector() {
		listeners = new ArrayList<ConnectorListener>(1);
	}

	// -------------------------------------------------------------------------
	// 
	// -------------------------------------------------------------------------

	public abstract void open();

	public abstract void write(byte b[]);

	public abstract void close();

	// -------------------------------------------------------------------------
	// Gestion des listeners
	// -------------------------------------------------------------------------

	/**
	 * Ajoute un listener
	 */
	public void addListener(final ConnectorListener listener) {
		if (listener != null) {
			listeners.add(listener);
		}
	}

	/**
	 * Suppression d'un listener
	 */
	public void removeListener(final ConnectorListener listener) {
		listeners.remove(listener);
	}

	/**
	 * Dispatch les données reÃ§ues vers les Listeners
	 */
	protected void dispatch(final String str) {
		for (final ConnectorListener listener : listeners) {
			listener.onReceive(str);
		}
	}
}
