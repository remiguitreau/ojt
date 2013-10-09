package com.ojt;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.ojt.algo.WeightComparator;

public class WeightComparatorTest {

	@Test
	public void testCompare() {
		final WeightComparator wc = new WeightComparator();

		final Competitor comp1 = new Competitor("TOTO", "toto", new Club("SEPA050030", "05", "gap"),
				CompetitorCategory.MINIME, 35);
		final Competitor comp2 = new Competitor("TITI", "titi", new Club("SEPA050030", "05", "gap"),
				CompetitorCategory.MINIME, 35);
		final Competitor comp3 = new Competitor("TUTU", "tutu", new Club("SEPA050030", "13", "marseille"),
				CompetitorCategory.MINIME, 40);
		final Competitor comp4 = new Competitor("TATA", "tata", new Club("SEPA050030", "05", "veynes"),
				CompetitorCategory.MINIME, 20);

		assertEquals(0, wc.compare(null, null));
		assertEquals(1, wc.compare(comp1, null));
		assertEquals(-1, wc.compare(null, comp2));
		assertEquals(0, wc.compare(comp1, comp2));
		assertEquals(-1, wc.compare(comp1, comp3));
		assertEquals(1, wc.compare(comp3, comp1));
		assertEquals(1, wc.compare(comp1, comp4));

		final Competitor comp5 = new Competitor();
		assertEquals(0, wc.compare(comp5, comp5));
		assertEquals(1, wc.compare(comp1, comp5));
		assertEquals(-1, wc.compare(comp5, comp1));
	}

}
