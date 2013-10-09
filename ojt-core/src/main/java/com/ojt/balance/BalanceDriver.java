package com.ojt.balance;

import com.ojt.connector.ConnectionDescriptor;

import java.util.List;

/**
 * Pilote de balance �lectronique
 * @author R�mi "DwarfConan" Guitreau
 * @since 3 juin 2009 : Cr�ation
 */
public interface BalanceDriver {

	/**
	 * Ajoute un auditeur de poids
	 * @param listener
	 */
	void addListener(final BalanceListener listener);

	/**
	 * D�marre le pilote de la balance et renvoi une exception en cas d'erreur
	 * @throws Exception
	 */
	void start() throws Exception;

	/**
	 * @param desc
	 */
	void setConnectionDescriptor(ConnectionDescriptor desc);

	/**
	 * 
	 */
	void startAndFoundConnectionPort() throws Exception;

	/**
	 * @return
	 */
	List<String> getAvailablePorts();
}