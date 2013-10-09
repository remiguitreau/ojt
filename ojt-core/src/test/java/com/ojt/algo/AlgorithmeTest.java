package com.ojt.algo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;

import com.ojt.Club;
import com.ojt.Competitor;
import com.ojt.CompetitorCategory;
import com.ojt.CompetitorList;
import com.ojt.CompetitorPoule;
import com.ojt.CompetitorPoule.PouleNumber;

public class AlgorithmeTest {
	Algorithme algo = new Algorithme();

	Competitor comp1 = new Competitor("COMP1", "comp1", new Club("SEPA050030", "05", "gap"), CompetitorCategory.MINIME, 35);

	Competitor comp2 = new Competitor("COMP2", "comp2", new Club("SEPA050030", "05", "gap"), CompetitorCategory.MINIME, 35);

	Competitor comp6 = new Competitor("COMP6", "comp6", new Club("SEPA050030", "05", "gap"), CompetitorCategory.MINIME, 35);

	Competitor comp7 = new Competitor("COMP7", "comp7", new Club("SEPA050030", "05", "gap"), CompetitorCategory.MINIME, 35);

	Competitor comp11 = new Competitor("COMP11", "comp11", new Club("SEPA050030", "05", "gap"), CompetitorCategory.MINIME, 35);

	Competitor comp12 = new Competitor("COMP12", "comp12", new Club("SEPA050030", "05", "gap"), CompetitorCategory.MINIME, 35);

	Competitor comp4 = new Competitor("COMP4", "comp4", new Club("SEPA050050", "05", "veynes"), CompetitorCategory.MINIME, 20);

	Competitor comp5 = new Competitor("COMP5", "comp5", new Club("SEPA050050", "05", "veynes"), CompetitorCategory.MINIME, 30);

	Competitor comp9 = new Competitor("COMP9", "comp9", new Club("SEPA050050", "05", "veynes"), CompetitorCategory.MINIME, 20);

	Competitor comp10 = new Competitor("COMP10", "comp10", new Club("SEPA050050", "05", "veynes"), CompetitorCategory.MINIME, 30);

	Competitor comp14 = new Competitor("COMP14", "comp14", new Club("SEPA050050", "05", "veynes"), CompetitorCategory.MINIME, 20);

	Competitor comp15 = new Competitor("COMP15", "comp15", new Club("SEPA050050", "05", "veynes"), CompetitorCategory.MINIME, 30);

	Competitor comp3 = new Competitor("COMP3", "comp3", new Club("SEPA130001", "13", "marseille"), CompetitorCategory.MINIME,
			40);

	Competitor comp8 = new Competitor("COMP8", "comp8", new Club("SEPA130001", "13", "marseille"), CompetitorCategory.MINIME,
			40);

	Competitor comp13 = new Competitor("COMP13", "comp13", new Club("SEPA130001", "13", "marseille"), CompetitorCategory.MINIME,
			40);

	Competitor comp16 = new Competitor("COMP16", "comp16", new Club("SEPA130001", "13", "marseille"), CompetitorCategory.MINIME,
			40);

	CompetitorList competitors = new CompetitorList(Arrays.asList(new Competitor[] { comp1, comp2, comp3,
			comp4, comp5 }));

	@Test
	public void testGetMinWeight() {
		try {
			algo.getMinWeight(null);
			fail("Une exception aurait du étre déclanchée");
		} catch (final IllegalArgumentException e) {
		}
		try {
			algo.getMinWeight(new CompetitorList());
			fail("Une exception aurait du étre déclanchée");
		} catch (final IllegalArgumentException e) {
		}

		assertEquals(20, algo.getMinWeight(competitors), 0);

	}

	@Test
	public void testGetMaxWeight() {
		try {
			algo.getMaxWeight(null);
			fail("Une exception aurait du étre déclanchée");
		} catch (final IllegalArgumentException e) {
		}
		try {
			algo.getMaxWeight(new CompetitorList());
			fail("Une exception aurait du étre déclanchée");
		} catch (final IllegalArgumentException e) {
		}

		assertEquals(40, algo.getMaxWeight(competitors), 0);
	}

	@Test
	public void testGetCatecogie() {
		try {
			algo.getCategory(null);
			fail("Une exception aurait du étre déclanchée");
		} catch (final IllegalArgumentException e) {
		}
		try {
			algo.getCategory(new CompetitorList());
			fail("Une exception aurait du étre déclanchée");
		} catch (final IllegalArgumentException e) {
		}

		assertEquals(CompetitorCategory.MINIME, algo.getCategory(competitors));
	}

	@Test
	public void testGetPoules() {
		try {
			algo.getPoules(null);
			fail("Une exception aurait du étre déclanchée");
		} catch (final IllegalArgumentException e) {
		}
		try {
			algo.getPoules(new CompetitorList());
			fail("Une exception aurait du étre déclanchée");
		} catch (final IllegalArgumentException e) {
		}

		final ArrayList<CompetitorPoule> poules = new ArrayList<CompetitorPoule>();
		poules.add(new CompetitorPoule(PouleNumber.A, new ArrayList<Competitor>(
				Arrays.asList(new Competitor[] { comp1, comp11, comp9, comp3 }))));
		poules.add(new CompetitorPoule(PouleNumber.B, new ArrayList<Competitor>(
				Arrays.asList(new Competitor[] { comp2, comp12, comp10, comp8 }))));
		poules.add(new CompetitorPoule(PouleNumber.C, new ArrayList<Competitor>(
				Arrays.asList(new Competitor[] { comp6, comp4, comp14, comp13 }))));
		poules.add(new CompetitorPoule(PouleNumber.D, new ArrayList<Competitor>(
				Arrays.asList(new Competitor[] { comp7, comp5, comp15 }))));

		final CompetitorList comps = new CompetitorList(Arrays.asList(new Competitor[] { comp1, comp2, comp3,
				comp4, comp5, comp6, comp7, comp8, comp9, comp10, comp11, comp12, comp13, comp14, comp15 }));

		final ArrayList<CompetitorPoule> poules2 = algo.getPoules(comps);
		System.out.println("POULE >> " + poules);
		System.out.println("POULE >> " + poules2);
		assertEquals(poules, poules2);
	}

	/**
	 * Test poules size 1JUDOKA POULE DE 4 2JP4 3JP4 4JP4 5JP5 6J 2P3 7JP3+P4
	 * 8J2P4 9J 3P3 10J 2P3+P4 11J 2P4+P3 12J 3P4 13J 3P3+P4 14J 2P4+2P3 15
	 * 3P4+P3 16 4P4
	 */
	@Test
	public void testPoulesSize() {
		CompetitorList comps = new CompetitorList(Arrays.asList(new Competitor[] { comp1, comp2, comp3,
				comp4, comp5, comp6, comp7, comp8, comp9, comp10, comp11, comp12, comp13, comp14, comp15 }));

		// 1 : P4
		comps = new CompetitorList(Arrays.asList(new Competitor[] { comp1 }));
		ArrayList<CompetitorPoule> poules = algo.getPoules(comps);
		assertEquals(1, poules.size());
		assertEquals(1, poules.get(0).getPouleSize());

		// 3 : P4
		comps = new CompetitorList(Arrays.asList(new Competitor[] { comp1, comp2, comp3 }));
		poules = algo.getPoules(comps);
		assertEquals(1, poules.size());
		assertEquals(3, poules.get(0).getPouleSize());

		// 4 : P4
		comps = new CompetitorList(Arrays.asList(new Competitor[] { comp1, comp2, comp3, comp4 }));
		poules = algo.getPoules(comps);
		assertEquals(1, poules.size());
		assertEquals(4, poules.get(0).getPouleSize());

		// 5 : P5
		comps = new CompetitorList(Arrays.asList(new Competitor[] { comp1, comp2, comp3, comp4, comp5 }));
		poules = algo.getPoules(comps);
		assertEquals(1, poules.size());
		assertEquals(5, poules.get(0).getPouleSize());

		// 6 : 2P3
		comps = new CompetitorList(
				Arrays.asList(new Competitor[] { comp1, comp2, comp3, comp4, comp5, comp6 }));
		poules = algo.getPoules(comps);
		assertEquals(2, poules.size());
		assertEquals(3, poules.get(0).getPouleSize());
		assertEquals(3, poules.get(1).getPouleSize());

		// 7 : P3 + P4
		comps = new CompetitorList(Arrays.asList(new Competitor[] { comp1, comp2, comp3, comp4, comp5, comp6,
				comp7 }));
		poules = algo.getPoules(comps);
		assertEquals(2, poules.size());
		assertEquals(4, poules.get(0).getPouleSize());
		assertEquals(3, poules.get(1).getPouleSize());

		// 8 : 2*P4
		comps = new CompetitorList(Arrays.asList(new Competitor[] { comp1, comp2, comp3, comp4, comp5, comp6,
				comp7, comp8 }));
		poules = algo.getPoules(comps);
		assertEquals(2, poules.size());
		assertEquals(4, poules.get(0).getPouleSize());
		assertEquals(4, poules.get(1).getPouleSize());

		// 9 : 3*P3
		comps = new CompetitorList(Arrays.asList(new Competitor[] { comp1, comp2, comp3, comp4, comp5, comp6,
				comp7, comp8, comp9 }));
		poules = algo.getPoules(comps);
		assertEquals(3, poules.size());
		assertEquals(3, poules.get(0).getPouleSize());
		assertEquals(3, poules.get(1).getPouleSize());
		assertEquals(3, poules.get(2).getPouleSize());

		// 10 : 2*P3 + P4
		comps = new CompetitorList(Arrays.asList(new Competitor[] { comp1, comp2, comp3, comp4, comp5, comp6,
				comp7, comp8, comp9, comp10 }));
		poules = algo.getPoules(comps);
		assertEquals(3, poules.size());
		assertEquals(4, poules.get(0).getPouleSize());
		assertEquals(3, poules.get(1).getPouleSize());
		assertEquals(3, poules.get(2).getPouleSize());

		// 11 : 2*P4 + P3
		comps = new CompetitorList(Arrays.asList(new Competitor[] { comp1, comp2, comp3, comp4, comp5, comp6,
				comp7, comp8, comp9, comp10, comp11 }));
		poules = algo.getPoules(comps);
		assertEquals(3, poules.size());
		assertEquals(4, poules.get(0).getPouleSize());
		assertEquals(4, poules.get(1).getPouleSize());
		assertEquals(3, poules.get(2).getPouleSize());

		// 12 : 3*P4
		comps = new CompetitorList(Arrays.asList(new Competitor[] { comp1, comp2, comp3, comp4, comp5, comp6,
				comp7, comp8, comp9, comp10, comp11, comp12 }));
		poules = algo.getPoules(comps);
		assertEquals(3, poules.size());
		assertEquals(4, poules.get(0).getPouleSize());
		assertEquals(4, poules.get(1).getPouleSize());
		assertEquals(4, poules.get(2).getPouleSize());

		// 13 : 3*P3 + P4
		comps = new CompetitorList(Arrays.asList(new Competitor[] { comp1, comp2, comp3, comp4, comp5, comp6,
				comp7, comp8, comp9, comp10, comp11, comp12, comp13 }));
		poules = algo.getPoules(comps);
		assertEquals(4, poules.size());
		assertEquals(4, poules.get(0).getPouleSize());
		assertEquals(3, poules.get(1).getPouleSize());
		assertEquals(3, poules.get(2).getPouleSize());
		assertEquals(3, poules.get(3).getPouleSize());

		// 14 : 2*P4 + 2*P3
		comps = new CompetitorList(Arrays.asList(new Competitor[] { comp1, comp2, comp3, comp4, comp5, comp6,
				comp7, comp8, comp9, comp10, comp11, comp12, comp13, comp14 }));
		poules = algo.getPoules(comps);
		assertEquals(4, poules.size());
		assertEquals(4, poules.get(0).getPouleSize());
		assertEquals(4, poules.get(1).getPouleSize());
		assertEquals(3, poules.get(2).getPouleSize());
		assertEquals(3, poules.get(3).getPouleSize());

		// 15 : 3*P4 + 1*P3
		comps = new CompetitorList(Arrays.asList(new Competitor[] { comp1, comp2, comp3, comp4, comp5, comp6,
				comp7, comp8, comp9, comp10, comp11, comp12, comp13, comp14, comp15 }));
		poules = algo.getPoules(comps);
		assertEquals(4, poules.size());
		assertEquals(4, poules.get(0).getPouleSize());
		assertEquals(4, poules.get(1).getPouleSize());
		assertEquals(4, poules.get(2).getPouleSize());
		assertEquals(3, poules.get(3).getPouleSize());

		// 16 : 4*P4
		comps = new CompetitorList(Arrays.asList(new Competitor[] { comp1, comp2, comp3, comp4, comp5, comp6,
				comp7, comp8, comp9, comp10, comp11, comp12, comp13, comp14, comp15, comp16 }));
		poules = algo.getPoules(comps);
		assertEquals(4, poules.size());
		assertEquals(4, poules.get(0).getPouleSize());
		assertEquals(4, poules.get(1).getPouleSize());
		assertEquals(4, poules.get(2).getPouleSize());
		assertEquals(4, poules.get(3).getPouleSize());

	}

}
