package com.ojt.balance;

import com.ojt.connector.ConnectionDescriptor;

import java.util.List;

/**
 * Pilote de balance électronique
 * @author Rémi "DwarfConan" Guitreau
 * @since 3 juin 2009 : Création
 */
public interface BalanceDriver {

	/**
	 * Ajoute un auditeur de poids
	 * @param listener
	 */
	void addListener(final BalanceListener listener);

	/**
	 * Démarre le pilote de la balance et renvoi une exception en cas d'erreur
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