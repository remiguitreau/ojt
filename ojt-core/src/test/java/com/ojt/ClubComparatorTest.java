package com.ojt;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.ojt.algo.ClubComparator;

public class ClubComparatorTest {


	@Test
	public void testCompare() {
		ClubComparator cc = new ClubComparator();
		
		Competitor comp1 = new Competitor("TOTO", "toto", new Club("SEPA050030", "05", "gap"), CompetitorCategory.MINIME, 35);
		Competitor comp2 = new Competitor("TITI", "titi", new Club("SEPA050030", "05", "gap"), CompetitorCategory.MINIME, 35);
		Competitor comp3 = new Competitor("TUTU", "tutu", new Club("SEPA130001", "13", "marseille"), CompetitorCategory.MINIME, 35);
		Competitor comp4 = new Competitor("TATA", "tata", new Club("SEPA050050", "05", "veynes"), CompetitorCategory.MINIME, 35);
		Competitor comp5 = new Competitor("TATA", "tata", new Club("SEPA050020", "05", "aye"), CompetitorCategory.MINIME, 35);

		Competitor comp6 = new Competitor("TATA", "tata", null, CompetitorCategory.MINIME, 35);
		Competitor comp7 = new Competitor("TATA", "tata", new Club(null, "05", "aye"), CompetitorCategory.MINIME, 35);

		assertEquals(0,  cc.compare(null, null));
		assertEquals(1,  cc.compare(comp1, null));		
		assertEquals(-1, cc.compare(null, comp2));		

		assertEquals(1,  cc.compare(comp1, comp6));
		assertEquals(1,  cc.compare(comp1, comp7));
		assertEquals(-1,  cc.compare(comp6, comp1));
		assertEquals(-1,  cc.compare(comp7, comp1));

		
		assertEquals(0,  cc.compare(comp1, comp2));
		assertEquals(-1, cc.compare(comp1, comp3));
		assertEquals(1,  cc.compare(comp3, comp1));
		assertTrue(cc.compare(comp1, comp4) < 0);
		assertTrue(cc.compare(comp4, comp1) > 0);
		assertTrue(cc.compare(comp1, comp5) > 0);
		assertTrue(cc.compare(comp5, comp1) < 0);
		
	}

}
