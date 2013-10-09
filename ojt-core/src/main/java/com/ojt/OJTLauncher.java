package com.ojt;

import org.apache.log4j.Logger;

import com.ojt.balance.BalanceDriver;
import com.ojt.balance.BalanceDriverImpl;
import com.ojt.balance.mock.MockBalanceDriver;
import com.ojt.connector.Rs232ConnectionDescriptor;
import com.ojt.ui.ConfigurationChangedListener;
import com.ojt.ui.OJTFrame;
import com.ojt.ui.SplashWindow;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * Classe de lancement d'OJT.
 * @author Rémi "DwarfConan" Guitreau
 * @since 17 oct. 2009 : Création
 */
public class OJTLauncher {

	// ---------------------------------------------------------
	// Attributs
	// ---------------------------------------------------------

	private BalanceDriver balanceDriver;

	private final Logger logger = Logger.getLogger(OJTLauncher.class);

	private static OJTFrame ojtFrame;

	// ---------------------------------------------------------
	// Public
	// ---------------------------------------------------------
	public void launch(final String[] args) {
		boolean mockBalance = false;

		for (final String arg : args) {
			if (arg.toLowerCase().equals("mock")) {
				mockBalance = true;
			}
		}

		initBalanceDriver(mockBalance);
		displaySplashScreen();

		displayOJTFrame();
		startBalanceDriver();
	}

	// ---------------------------------------------------------
	// Privée
	// ---------------------------------------------------------

	private void initBalanceDriver(final boolean mockBalance) {
		if (mockBalance) {
			balanceDriver = new MockBalanceDriver();
		} else {
			balanceDriver = new BalanceDriverImpl();
		}
	}

	private void startBalanceDriver() {
		final String portName = OJTConfiguration.getInstance().getProperty(OJTConfiguration.SERIAL_PORT);
		logger.info("PortName=" + portName);

		final Rs232ConnectionDescriptor desc = new Rs232ConnectionDescriptor();
		desc.setPortName(portName);
		desc.setBaudRate(9600);
		desc.setDataBits(8);
		desc.setParity(0);
		desc.setStopBits(1);
		balanceDriver.setConnectionDescriptor(desc);
		try {
			balanceDriver.start();
		} catch (final Exception ex) {
			logger.error("Unable to connect to balance", ex);
			String ports = "";
			for (final String port : balanceDriver.getAvailablePorts()) {
				ports += port + ", ";
			}
			JOptionPane.showMessageDialog(null, "Pas de balance connectée sur le port " + desc.getPortName()
					+ "\n\nListe des ports connus : " + ports);
		}
	}

	private void displaySplashScreen() {
		try {
			final SplashWindow ojtSplashWindow = new SplashWindow(new JFrame(), 100);
			ojtSplashWindow.displaySplashWindow();
		} catch (final Exception ex) {
			logger.error("", ex);
		}
	}

	public static void showError(final String message, final String title) {
		JOptionPane.showMessageDialog(ojtFrame, message, title, JOptionPane.ERROR_MESSAGE);
	}

	private void displayOJTFrame() {
		ojtFrame = new OJTFrame(balanceDriver, new ConfigurationChangedListener() {
			@Override
			public void configurationUpdated() {
				startBalanceDriver();
			}
			@Override
			public void availableMenusChanged() {
				
			}
		});
		ojtFrame.setPreferredSize(new Dimension(800, 450));
		ojtFrame.setLocation(400, 300);
		ojtFrame.pack();
		ojtFrame.setLocationByPlatform(true);
		ojtFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ojtFrame.setVisible(true);
	}

	// ---------------------------------------------------------
	// Main de lancement
	// ---------------------------------------------------------

	public static void main(final String[] args) {
		new OJTLauncher().launch(args);
	}

}
