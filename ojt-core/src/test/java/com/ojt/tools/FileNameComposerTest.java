package com.ojt.tools;

import junit.framework.Assert;

import org.junit.Test;

import com.ojt.CompetitionCategory;
import com.ojt.CompetitionDescriptor;
import com.ojt.CompetitorCategory;
import com.ojt.CompetitorSex;
import com.ojt.OJTCompetitionCategories;


public class FileNameComposerTest {

	@Test
	public void testComposeDirectoryNameWithUnknownCategory() {
		final CompetitionDescriptor competitionDescriptor = new CompetitionDescriptor();
		competitionDescriptor.setDate("12 03 2005");
		competitionDescriptor.setLocation("Veynes");
		competitionDescriptor.setName("Comp�tition de test");
		competitionDescriptor.setCategory(CompetitionCategory.UNKNOWN_CATEGORY);
		Assert.assertEquals("Comp�tition_de_test-Veynes-12_03_2005", FileNameComposer.composeDirectoryName(competitionDescriptor));
	}
	
	@Test
	public void testComposeDirectoryNameWithCategory() {
		final CompetitionDescriptor competitionDescriptor = new CompetitionDescriptor();
		competitionDescriptor.setDate("12 03 2005");
		competitionDescriptor.setLocation("Veynes");
		competitionDescriptor.setName("Comp�tition de test");
		final CompetitionCategory categ = new CompetitionCategory("cat�gorie de test", CompetitorSex.FEMALE, CompetitorCategory.BENJAMIN);
		OJTCompetitionCategories.addNewCompetitionCategory(categ);
		competitionDescriptor.setCategory(categ);
		Assert.assertEquals("cat�gorie_de_test-Comp�tition_de_test-Veynes-12_03_2005", FileNameComposer.composeDirectoryName(competitionDescriptor));
	}
	
	@Test
	public void testDecomposeDirectoryNameWithCategory() {
		final CompetitionCategory categ = new CompetitionCategory("cat�gorie de test", CompetitorSex.FEMALE, CompetitorCategory.BENJAMIN);
		OJTCompetitionCategories.addNewCompetitionCategory(categ);
		final CompetitionDescriptor competitionDescriptor = FileNameComposer.decomposeFileName("cat�gorie_de_test-Comp�tition_de_test-Veynes-12_03_2005-hjhkj.dat");
		Assert.assertEquals("12_03_2005", competitionDescriptor.getDate());
		Assert.assertEquals("Veynes", competitionDescriptor.getLocation());
		Assert.assertEquals("Comp�tition_de_test", competitionDescriptor.getCompetitionName());
		Assert.assertEquals("cat�gorie de test", competitionDescriptor.getCategory().getHumanName());
	}
}
