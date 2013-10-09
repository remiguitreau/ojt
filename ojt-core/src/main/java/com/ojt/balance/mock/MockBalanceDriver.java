/**
 * 
 */
package com.ojt.balance.mock;

import org.apache.log4j.Logger;

import com.ojt.balance.BalanceDriver;
import com.ojt.balance.BalanceListener;
import com.ojt.connector.ConnectionDescriptor;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Faux pilote de balance
 * @author Rémi "DwarfConan" Guitreau
 * @since 3 juin 2009 : Création
 */
public class MockBalanceDriver implements BalanceDriver {

	// ---------------------------------------------------------
	// Attributs
	// ---------------------------------------------------------

	private final List<BalanceListener> listeners = new LinkedList<BalanceListener>();

	private final Random random = new Random();

	private final Logger logger = Logger.getLogger(getClass());

	// ---------------------------------------------------------
	// Constructeurs
	// ---------------------------------------------------------

	/**
	 * 
	 */
	public MockBalanceDriver() {
		super();
	}

	@Override
	public void startAndFoundConnectionPort() throws Exception {
		start();
	}

	@Override
	public void start() {
		init();
	}

	// ---------------------------------------------------------
	// Implémentation de BalanceDriver
	// ---------------------------------------------------------

	@Override
	public void addListener(final BalanceListener listener) {
		listeners.add(listener);
	}

	private void init() {
		new Thread() {
			@Override
			public void run() {
				while (true) {
					fireNewWeight(random.nextInt(250));
					try {
						Thread.sleep(10000);
					} catch (final Exception ex) {
						logger.error("Error while sleeping", ex);
					}
				}
			}
		}.start();
	}

	private void fireNewWeight(final float weight) {
		for (final BalanceListener l : listeners) {
			if (l != null) {
				l.weightReceived(weight);
			}
		}
	}

	@Override
	public void setConnectionDescriptor(final ConnectionDescriptor desc) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<String> getAvailablePorts() {
		// TODO Auto-generated method stub
		return null;
	}

}
