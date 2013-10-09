package com.ojt.algo;

import com.ojt.Competitor;

import java.util.Comparator;

public class WeightComparator implements Comparator<Competitor> {

	/**
	 * @param competitor1 the first object to be compared.
	 * @param competitor2 the second object to be compared.
	 * @return a negative integer, zero, or a positive integer as the first
	 *         argument is less than, equal to, or greater than the second.
	 */
	@Override
	public int compare(final Competitor competitor1, final Competitor competitor2) {
		if ((competitor1 == null) && (competitor2 == null)) {
			return 0;
		} else if (competitor1 == null) {
			return -1;
		} else if (competitor2 == null) {
			return 1;
		}
		if ((competitor1.getWeight() == null) && (competitor2.getWeight() == null)) {
			return 0;
		} else if (competitor1.getWeight() == null) {
			return -1;
		} else if (competitor2.getWeight() == null) {
			return 1;
		}

		if (competitor1.getWeight().floatValue() == competitor2.getWeight().floatValue()) {
			return 0;
		}
		if (competitor1.getWeight() < competitor2.getWeight()) {
			return -1;
		}
		return 1;
	}

}
