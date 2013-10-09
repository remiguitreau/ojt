package com.ojt.connector;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.List;
import java.util.TooManyListenersException;

import javax.comm.CommPortIdentifier;
import javax.comm.NoSuchPortException;
import javax.comm.PortInUseException;
import javax.comm.SerialPort;
import javax.comm.SerialPortEvent;
import javax.comm.SerialPortEventListener;
import javax.comm.UnsupportedCommOperationException;

/**
 * Connecteur RS232
 */
public class Rs232Connector extends Connector {
	// -------------------------------------------------------------------------
	// Constantes
	// -------------------------------------------------------------------------

	protected static final int BUFFER_LENGTH = 1000;

	// -------------------------------------------------------------------------
	// Attributs
	// -------------------------------------------------------------------------

	private Rs232ConnectionDescriptor connectionDescriptor;

	// Indentifiant du port s�rie
	private CommPortIdentifier portId;

	// Object pour lire ou �crire des donn�es sur le port
	private SerialPort serialPort;

	// Monitor d'acc�s au p�riph�rique
	private final Object deviceMonitor;

	// Monitor d'acc�s au buffer de donn�es
	private final Object bufferMonitor;

	// le buffer qui contient les donnees
	private final byte buffer[];

	private int writeIndex;

	boolean debug;

	private boolean running;

	private InputStream inputStream;

	private BufferTokenizer bufferCutter;

	private final Logger logger = Logger.getLogger(getClass());

	// -------------------------------------------------------------------------
	// Constructeurs
	// -------------------------------------------------------------------------

	public Rs232Connector() {
		super();
		buffer = new byte[BUFFER_LENGTH];
		writeIndex = 0;
		deviceMonitor = new Object();
		bufferMonitor = new Object();
		debug = false;
	}

	public Enumeration getAvalablePorts() {
		return CommPortIdentifier.getPortIdentifiers();
	}

	@Override
	public void open() {
		// r�cup�ration de l'identifiant du port
		try {
			logger.info("Liste des ports : " + CommPortIdentifier.getPortIdentifiers());
			final Enumeration identifiers = CommPortIdentifier.getPortIdentifiers();
			while (identifiers.hasMoreElements()) {
				logger.info(((CommPortIdentifier) identifiers.nextElement()).getName());
			}
			portId = CommPortIdentifier.getPortIdentifier(connectionDescriptor.getPortName());
		} catch (final NoSuchPortException e) {
			throw new RuntimeException("Rs232Connector (" + connectionDescriptor.getPortName() + ") : "
					+ e.toString());
		}

		// ouverture du port
		try {
			serialPort = (SerialPort) portId.open("driver", 2000);
		} catch (final PortInUseException e) {
			throw new RuntimeException("Rs232Connector (" + connectionDescriptor.getPortName() + ") : "
					+ e.toString());
		}

		// param�trage du port
		// hmmmmmmmmmmmm cf :
		// http://forum.java.sun.com/thread.jspa?threadID=673793&start=15&tstart=0
		boolean result = false;
		while (!result) {
			try {
				serialPort.setSerialPortParams(connectionDescriptor.getBaudRate(),
						connectionDescriptor.getDataBits(), connectionDescriptor.getStopBits(),
						connectionDescriptor.getParity());
				result = true;
			} catch (final Exception ex) {
				logger.error("Unable to set serial ports parameters", ex);
				result = false;
				try {
					Thread.sleep(200);
				} catch (final InterruptedException exc) {
					logger.error("Error while sleeping", exc);
				}
			}
		}
		/** @todo : test */
		// Suppression
		try {
			serialPort.enableReceiveTimeout(5000); // timeout de 1 ms
		} catch (final UnsupportedCommOperationException ex) {
			logger.warn("Warning : impossible to set timeout for port " + connectionDescriptor.getPortName(),
					ex);
		}

		try {
			// inputStream = new BufferedReader(new
			// InputStreamReader(serialPort.getInputStream()));
			inputStream = serialPort.getInputStream();
		} catch (final IOException e) {
			serialPort.close();
			throw new RuntimeException("Rs232Connector (" + connectionDescriptor.getPortName() + ") : "
					+ e.toString());
		}

		try {
			serialPort.addEventListener(new SerialPortEventListener() {

				@Override
				public void serialEvent(final SerialPortEvent evt) {
					if (evt.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
						synchronized (bufferMonitor) {
							if (writeIndex >= BUFFER_LENGTH - 1) {
								flushBuffer();
							}
							try {
								byte newData = 0;
								while (newData != -1) {
									newData = (byte) inputStream.read();
									if (newData == -1) {
										break;
									}
									if (writeIndex >= BUFFER_LENGTH - 1) {
										flushBuffer();
									}
									buffer[writeIndex++] = newData;
								}
							} catch (final Exception exc) {
								logger.error("Error reading input stream from serial port", exc);
							}
							bufferMonitor.notify();
						}
					}
				}

			});
			serialPort.notifyOnDataAvailable(true);
			serialPort.enableReceiveTimeout(20);

		} catch (final TooManyListenersException exc) {
			logger.error("", exc);
		} catch (final UnsupportedCommOperationException exc) {
			logger.error("", exc);
		}

		final Thread threadParse = new Thread("Rs232Connector_" + connectionDescriptor.getPortName()
				+ "_dispatch_thread") {
			@Override
			public void run() {
				try {
					parseThread();
				} catch (final Exception e) {
					logger.error("Error while parsing thread", e);
				}
			}
		};
		threadParse.start();
	}

	private void flushBuffer() {
		logger.info("Rs232Connector : buffer index > " + BUFFER_LENGTH + " : flush buffer : '"
				+ toString(buffer) + "' : '" + toHexaString(buffer) + "'");
		writeIndex = 0;
	}

	protected String toString(final byte[] buff) {
		final StringBuffer sb = new StringBuffer(buff.length);
		for (final byte element : buff) {
			sb.append((char) element);
		}
		return sb.toString();
	}

	protected String toHexaString(final byte[] buff) {
		final StringBuffer sb = new StringBuffer(buff.length);
		for (final byte element : buff) {
			sb.append("0x");
			sb.append(Integer.toHexString(element));
			sb.append(" ");
		}
		return sb.toString();
	}

	protected String buildHexString(final String s) {
		final StringBuffer _buffer = new StringBuffer();
		for (int index = 0; index < s.length(); index++) {
			final Integer c = new Integer(s.charAt(index));
			_buffer.append(Integer.toHexString(c.intValue()));
			_buffer.append(" ");
		}
		return _buffer.toString();
	}

	/**
	 * M�thode appel�e lors de la r�ception de bytes depuis le lien RS
	 * @param b Les octzets reçus
	 * @param size Le nombre d'octets
	 */
	protected List<String> receiveBytesFromRs(final String toParse) {
		if (toParse.isEmpty()) {
			return null;
		}

		final List<String> cuts = bufferCutter.cutOnDelimiters(toParse);
		// on garde ce qui reste
		final int last = cuts.size() - 1;
		if (last > 0) {
			final String whatsLeft = cuts.get(last);
			whatsLeft.getBytes(0, whatsLeft.length(), buffer, 0);
			writeIndex = whatsLeft.length();
			cuts.remove(last);
		} else {
			writeIndex = 0;
		}

		return cuts;
	}

	/**
	 * Thread pour parser les donn�es reçues via RS. Il est r�veill� par
	 * le thread de reception.
	 */
	protected void parseThread() {
		running = true;
		while (running) {
			List<String> cuts = null;
			synchronized (bufferMonitor) {
				if (running) {
					String str = null;
					// d�synchronisation
					try {
						bufferMonitor.wait();
						str = new String(buffer, 0, writeIndex);
					} catch (final InterruptedException ex) {
						logger.error("Error while waiting", ex);
					}
					if (str != null) {
						cuts = receiveBytesFromRs(str);

					}
				}
			}
			if (cuts != null) {
				// on envoie au listener
				for (final String frame : cuts) {
					dispatch(frame);
				}
			}
		}
	}

	/**
	 * Ecriture sur le lien s�rie
	 * @param b
	 */
	@Override
	public void write(final byte b[]) {
		final String s = new String(b);
		try {
			final PrintWriter outputStream = new PrintWriter(serialPort.getOutputStream());
			outputStream.write(s);
			outputStream.close();
		} catch (final Exception e) {
			logger.error("Error while writing on serial port", e);
		}
	}

	/**
	 * Fermeture du lien s�rie
	 */
	@Override
	public void close() {
		running = false;

		// fermeture du flux et port
		try {
			if (inputStream != null) {
				inputStream.close();
			}
			if (serialPort != null) {
				serialPort.close();
			}
		} catch (final IOException e) {
			logger.error("Error closing COM port inputStream", e);
		}
	}

	/** ********** Setters / Getters ************* */
	public BufferTokenizer getBufferCutter() {
		return bufferCutter;
	}

	public void setBufferCutter(final BufferTokenizer bufferCutter) {
		this.bufferCutter = bufferCutter;
	}

	public Rs232ConnectionDescriptor getConnectionDescriptor() {
		return connectionDescriptor;
	}

	public void setConnectionDescriptor(final Rs232ConnectionDescriptor connectionDescriptor) {
		this.connectionDescriptor = connectionDescriptor;
	}

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(final boolean debug) {
		this.debug = debug;
	}

}
