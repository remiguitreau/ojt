package com.ojt.balance;

import org.apache.log4j.Logger;

import com.ojt.OJTConfiguration;
import com.ojt.balance.kern.DEDecoder;
import com.ojt.connector.ConnectionDescriptor;
import com.ojt.connector.Rs232ConnectionDescriptor;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;

import javax.comm.CommPortIdentifier;

/**
 * @author FMo
 * @since 4 juin 2009 (FMo) : Création
 */
public class BalanceDriverImpl implements BalanceDriver {

	// -------------------------------------------------------------------------
	// Attributes
	// -------------------------------------------------------------------------

	private final List<BalanceListener> balanceListeners;

	private final BalanceProtocolHandler balanceProtocolHandler;

	private final Logger logger = Logger.getLogger(getClass());

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	public BalanceDriverImpl() {
		balanceListeners = new ArrayList<BalanceListener>();
		balanceProtocolHandler = new BalanceProtocolHandler(new DEDecoder(), this);
	}

	@Override
	public void start() throws Exception {
		balanceProtocolHandler.start();

	}

	public void frameReceived(final BalanceFrame decodedFrame) {
		for (final BalanceListener balanceListener : balanceListeners) {
			if (decodedFrame.isStableWeight()) {
				balanceListener.weightReceived(decodedFrame.getWeight());
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addListener(final BalanceListener listener) {
		balanceListeners.add(listener);
	}

	@Override
	public void setConnectionDescriptor(final ConnectionDescriptor desc) {
		balanceProtocolHandler.setConnectionDescriptor(desc);
	}

	@Override
	public void startAndFoundConnectionPort() throws Exception {
		try {
			start();
		} catch (final Exception ex) {
			logger.error(
					"Unable to connect to balance on port "
							+ balanceProtocolHandler.getConnectionDescriptor(), ex);
			foundBalancePort();
		}
	}

	/**
	 * @return renvoie la liste des ports connus
	 */
	@Override
	public List<String> getAvailablePorts() {
		final List<String> ports = new LinkedList<String>();
		final Enumeration identifiers = balanceProtocolHandler.getAvalablePorts();
		while (identifiers.hasMoreElements()) {
			final String name = ((CommPortIdentifier) identifiers.nextElement()).getName();
			if (name.contains("COM") || name.contains("ttyS")) {
				ports.add(name);
			}
		}
		return ports;
	}

	private void foundBalancePort() throws Exception {
		final ConnectionDescriptor desc = balanceProtocolHandler.getConnectionDescriptor();
		if (desc instanceof Rs232ConnectionDescriptor) {
			final Rs232ConnectionDescriptor descriptor = (Rs232ConnectionDescriptor) desc;

			for (final String name : getAvailablePorts()) {
				logger.info("Test avec le port : '" + name + "'");
				descriptor.setPortName(name);
				setConnectionDescriptor(desc);
				try {
					start();
					// si pas d'erreur, c'est que c'est bon: on sauvegarde
					// et on return
					OJTConfiguration.getInstance().updateProperty(OJTConfiguration.SERIAL_PORT, name);
					return;
				} catch (final Exception ex) {
					logger.error("Unable to connect to balance on port " + name, ex);
				}
			}

			final String portName = OJTConfiguration.getInstance().getProperty(OJTConfiguration.SERIAL_PORT);
			descriptor.setPortName(portName);
			setConnectionDescriptor(desc);
			start();
		}
	}
}
