package com.ojt.balance;

/**
 * @author FMo
 * @since 20 avr. 2009 (FMo) : Cr�ation
 */
public interface BalanceDecoder {

	/**
	 * @param frame
	 * @return renvoie la trame
	 */
	BalanceFrame decode(String frame);
}
