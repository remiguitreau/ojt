package com.ojt.balance.kern;

import org.apache.log4j.Logger;

import com.ojt.connector.BufferTokenizer;
import com.ojt.connector.ConnectorListener;
import com.ojt.connector.Rs232ConnectionDescriptor;
import com.ojt.connector.Rs232Connector;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * @author FMo
 * @since 20 avr. 2009 (FMo) : Création
 */
public class DEConnector implements ConnectorListener {

	// -------------------------------------------------------------------------
	// Membres
	// -------------------------------------------------------------------------

	private final Rs232Connector connector;

	private final List<SerialDataListener> serialDateListeners;

	private Rs232ConnectionDescriptor connectionDescriptor;

	private final BufferTokenizer bufferCutter;

	private final Logger logger = Logger.getLogger(getClass());

	// -------------------------------------------------------------------------
	// Accesseurs
	// -------------------------------------------------------------------------

	public Rs232ConnectionDescriptor getConnectionDescriptor() {
		return connectionDescriptor;
	}

	public void setConnectionDescriptor(final Rs232ConnectionDescriptor connectionDescriptor) {
		this.connectionDescriptor = connectionDescriptor;
	}

	// -------------------------------------------------------------------------
	// Constructeur
	// -------------------------------------------------------------------------

	public DEConnector() {
		serialDateListeners = new ArrayList<SerialDataListener>();
		connector = new Rs232Connector();
		bufferCutter = new DEBufferTokenizer();
		connectionDescriptor = new Rs232ConnectionDescriptor();
	}

	// -------------------------------------------------------------------------
	// Méthodes publiques
	// -------------------------------------------------------------------------

	public void start() {
		try {
			connector.setConnectionDescriptor(connectionDescriptor);
			connector.addListener(this);
			connector.setBufferCutter(bufferCutter);
			connector.open();

		} catch (final Exception ex) {
			throw new OpenConnectionFailed(ex);
		}
	}

	public Enumeration getAvalablePorts() {
		return connector.getAvalablePorts();
	}

	public void addListener(final SerialDataListener listener) {
		serialDateListeners.add(listener);
	}

	public void stop() {
		// connector.stop();
	}

	// -------------------------------------------------------------------------
	// Implémentation de ConnectorListener
	// -------------------------------------------------------------------------

	@Override
	public void onReceive(final String str) {
		logger.info("REICEIVED : " + str);
		for (final SerialDataListener listener : serialDateListeners) {
			listener.dataReceived(str);
		}
	}

	// -------------------------------------------------------------------------
	// 
	// -------------------------------------------------------------------------

	public static void main(final String[] args) {
		final DEConnector connector = new DEConnector();
		connector.start();
	}

}
