package com.ojt;

import org.junit.Assert;
import org.junit.Test;


public class CompetitionCategoryTest {

	@Test
	public void testWithMaleAndOneCategory() {
		final CompetitionCategory categ = new CompetitionCategory(CompetitorSex.MALE, CompetitorCategory.PREPOUSSIN);
		Assert.assertTrue(categ.isCompetitorAccepted(createCompetitor(CompetitorSex.MALE, CompetitorCategory.PREPOUSSIN)));
		Assert.assertFalse(categ.isCompetitorAccepted(createCompetitor(CompetitorSex.MALE, CompetitorCategory.SENIOR)));
		Assert.assertFalse(categ.isCompetitorAccepted(createCompetitor(CompetitorSex.FEMALE, CompetitorCategory.PREPOUSSIN)));
		Assert.assertFalse(categ.isCompetitorAccepted(createCompetitor(CompetitorSex.UNKNOWN, CompetitorCategory.PREPOUSSIN)));
		Assert.assertFalse(categ.isCompetitorAccepted(createCompetitor(CompetitorSex.MALE, CompetitorCategory.UNKNOWN)));
	}
	
	@Test
	public void testWithBothSexAndOneCategory() {
		final CompetitionCategory categ = new CompetitionCategory(CompetitorSex.UNKNOWN, CompetitorCategory.PREPOUSSIN);
		Assert.assertTrue(categ.isCompetitorAccepted(createCompetitor(CompetitorSex.MALE, CompetitorCategory.PREPOUSSIN)));
		Assert.assertFalse(categ.isCompetitorAccepted(createCompetitor(CompetitorSex.MALE, CompetitorCategory.SENIOR)));
		Assert.assertTrue(categ.isCompetitorAccepted(createCompetitor(CompetitorSex.FEMALE, CompetitorCategory.PREPOUSSIN)));
		Assert.assertTrue(categ.isCompetitorAccepted(createCompetitor(CompetitorSex.UNKNOWN, CompetitorCategory.PREPOUSSIN)));
		Assert.assertFalse(categ.isCompetitorAccepted(createCompetitor(CompetitorSex.MALE, CompetitorCategory.UNKNOWN)));
	}
	
	@Test
	public void testWithBothSexAndUnknownCategory() {
		final CompetitionCategory categ = new CompetitionCategory(CompetitorSex.UNKNOWN, CompetitorCategory.UNKNOWN);
		Assert.assertTrue(categ.isCompetitorAccepted(createCompetitor(CompetitorSex.MALE, CompetitorCategory.PREPOUSSIN)));
		Assert.assertTrue(categ.isCompetitorAccepted(createCompetitor(CompetitorSex.MALE, CompetitorCategory.SENIOR)));
		Assert.assertTrue(categ.isCompetitorAccepted(createCompetitor(CompetitorSex.FEMALE, CompetitorCategory.PREPOUSSIN)));
		Assert.assertTrue(categ.isCompetitorAccepted(createCompetitor(CompetitorSex.UNKNOWN, CompetitorCategory.PREPOUSSIN)));
		Assert.assertTrue(categ.isCompetitorAccepted(createCompetitor(CompetitorSex.MALE, CompetitorCategory.UNKNOWN)));
	}
	
	@Test
	public void testWithOneSexAndTwoCategory() {
		final CompetitionCategory categ = new CompetitionCategory(CompetitorSex.FEMALE, CompetitorCategory.JUNIOR, CompetitorCategory.SENIOR);
		Assert.assertTrue(categ.isCompetitorAccepted(createCompetitor(CompetitorSex.FEMALE, CompetitorCategory.JUNIOR)));
		Assert.assertTrue(categ.isCompetitorAccepted(createCompetitor(CompetitorSex.FEMALE, CompetitorCategory.SENIOR)));
		Assert.assertFalse(categ.isCompetitorAccepted(createCompetitor(CompetitorSex.MALE, CompetitorCategory.JUNIOR)));
		Assert.assertFalse(categ.isCompetitorAccepted(createCompetitor(CompetitorSex.MALE, CompetitorCategory.SENIOR)));
		Assert.assertFalse(categ.isCompetitorAccepted(createCompetitor(CompetitorSex.UNKNOWN, CompetitorCategory.JUNIOR)));
		Assert.assertFalse(categ.isCompetitorAccepted(createCompetitor(CompetitorSex.UNKNOWN, CompetitorCategory.SENIOR)));
		Assert.assertFalse(categ.isCompetitorAccepted(createCompetitor(CompetitorSex.FEMALE, CompetitorCategory.UNKNOWN)));
		Assert.assertFalse(categ.isCompetitorAccepted(createCompetitor(CompetitorSex.FEMALE, CompetitorCategory.UNKNOWN)));
	}

	private Competitor createCompetitor(final CompetitorSex sex, final CompetitorCategory category) {
		final Competitor comp = new Competitor();
		comp.setCategory(category);
		comp.setSex(sex);
		return comp;
	}
}
