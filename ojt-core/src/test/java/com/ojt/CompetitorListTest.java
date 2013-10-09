package com.ojt;

import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;

import java.util.Arrays;

public class CompetitorListTest extends Datas {

	CompetitorList competitors = new CompetitorList(Arrays.asList(new Competitor[] { comp1, comp2, comp3,
			comp4, comp5 }));

	@Test
	public void testSortByWeight() {
		final Competitor[] comps = new Competitor[] { comp4, comp5, comp1, comp2, comp3 };
		assertArrayEquals(comps, competitors.sortByWeight());
	}

	@Test
	public void testSortByClub() {
		final Competitor[] comps = new Competitor[] { comp1, comp2, comp4, comp5, comp3 };
		assertArrayEquals(comps, competitors.sortByClub());

		final Competitor[] comps2 = new Competitor[] { comp1, comp2, comp6, comp7, comp11, comp12, comp4,
				comp5, comp9, comp10, comp14, comp15, comp3, comp8, comp13 };
		final CompetitorList competitors2 = new CompetitorList(Arrays.asList(new Competitor[] { comp1, comp2,
				comp3, comp4, comp5, comp6, comp7, comp8, comp9, comp10, comp11, comp12, comp13, comp14,
				comp15 }));

		assertArrayEquals(comps2, competitors2.sortByClub());
	}

	@Test
	public void testCreateGroups() {
		return;
		/*
		 * final Algorithme algo = new Algorithme(); try { new
		 * CompetitorList().createGroups(algo); fail("Aurait du dclancher une
		 * exception"); } catch (IllegalArgumentException e) {} CompetitorList
		 * competitors2 = new CompetitorList(Arrays.asList( new Competitor[] {
		 * comp1, comp2, comp3, comp4, comp5, comp6, comp7, comp8, comp9,
		 * comp10, } )); List<CompetitorGroup> ref = new LinkedList<CompetitorGroup>();
		 * ref.add(new CompetitorGroup(new
		 * CompetitorList(Arrays.asList(competitors2.sortByWeight())), algo));
		 * System.out.println("fabien >> "+ref); System.out.println("fabien >>
		 * "+competitors2.createGroups(algo)); assertEquals(ref,
		 * competitors2.createGroups(algo)); List<CompetitorGroup> ref3 = new
		 * LinkedList<CompetitorGroup>(); CompetitorList competitors3 = new
		 * CompetitorList(Arrays.asList( new Competitor[] { comp1, comp2, comp3,
		 * comp4, comp5, comp6, comp7, comp8, comp9, comp10, comp11, comp12,
		 * comp13, comp14, comp15, comp16 } )); ref3.add(new CompetitorGroup(new
		 * CompetitorList(Arrays.asList(competitors3.sortByWeight())), algo));
		 * assertEquals(ref3, competitors3.createGroups(algo)); List<CompetitorGroup>
		 * ref4 = new LinkedList<CompetitorGroup>(); CompetitorList
		 * competitors4 = new CompetitorList(Arrays.asList( new Competitor[] {
		 * comp1, comp2, comp3, comp4, comp5, comp6, comp7, comp8, comp9,
		 * comp10, comp11, comp12, comp13, comp14, comp15, comp16, comp17,
		 * comp18 } )); CompetitorList competitors41 = new
		 * CompetitorList(Arrays.asList( new Competitor[] { comp1, comp2, comp3,
		 * comp4, comp5, comp6, comp7, comp8, comp9, comp10, comp11, comp12,
		 * comp13, comp14, comp15, comp16 } )); CompetitorList competitors42 =
		 * new CompetitorList(Arrays.asList( new Competitor[] { comp17, comp18 }
		 * )); ref4.add(new CompetitorGroup(new
		 * CompetitorList(Arrays.asList(competitors41.sortByWeight())), algo));
		 * ref4.add(new CompetitorGroup(new
		 * CompetitorList(Arrays.asList(competitors42.sortByWeight())), algo));
		 * assertEquals(ref4, competitors4.createGroups(algo)); // tester les
		 * carts de poids CompetitorList competitors5 = new
		 * CompetitorList(Arrays.asList( new Competitor[] { comp101, comp102,
		 * comp103, comp104, comp105, comp106, comp107, comp108, comp109,
		 * comp110, comp111, comp112, comp113, comp114, comp115, comp116,
		 * comp117, comp118 } )); List<CompetitorGroup> groups5 =
		 * competitors5.createGroups(algo); for (final CompetitorGroup group :
		 * groups5) { float diff = group.getMaxGroupWeight() -
		 * group.getMinGroupWeight(); float purcent = diff * 100 /
		 * group.getMinGroupWeight(); System.out.println("Groupe : "+group);
		 * System.out.println("Ecart en pourcent : "+purcent);
		 * assertTrue(purcent <= OjtConstants.WEIGHT_DELTA_IN_PURCENT); }
		 */

	}

}
