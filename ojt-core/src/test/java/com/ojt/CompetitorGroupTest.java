package com.ojt;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import com.ojt.algo.Algorithme;

public class CompetitorGroupTest extends Datas {

	@Test
	public void testEqualsObject() {
		final Algorithme algo = new Algorithme();
		final CompetitorList competitors = new CompetitorList(Arrays.asList(new Competitor[] { comp1, comp2,
				comp3, comp4, comp5, comp6 }));
		final CompetitorList competitors2 = new CompetitorList(Arrays.asList(new Competitor[] { comp1, comp2,
				comp3, comp4, comp5, comp6, comp7, comp8, comp9, comp10, comp102 }));

		final CompetitorGroup ref = new CompetitorGroup(new CompetitorList(
				Arrays.asList(competitors2.sortByWeight())), algo);

		Assert.assertFalse(ref.equals(null));
		Assert.assertFalse(ref.equals(new String()));
		Assert.assertTrue(ref.equals(ref));
		Assert.assertFalse(ref.equals(new CompetitorGroup(competitors, algo)));
		Assert.assertTrue(ref.equals(new CompetitorGroup(competitors2, algo)));
		Assert.assertTrue(ref.equals(new CompetitorGroup(new CompetitorList(
				Arrays.asList(competitors2.sortByWeight())), algo)));

	}
}
