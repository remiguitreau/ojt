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
		competitionDescriptor.setName("Compétition de test");
		competitionDescriptor.setCategory(CompetitionCategory.UNKNOWN_CATEGORY);
		Assert.assertEquals("Compétition_de_test-Veynes-12_03_2005", FileNameComposer.composeDirectoryName(competitionDescriptor));
	}
	
	@Test
	public void testComposeDirectoryNameWithCategory() {
		final CompetitionDescriptor competitionDescriptor = new CompetitionDescriptor();
		competitionDescriptor.setDate("12 03 2005");
		competitionDescriptor.setLocation("Veynes");
		competitionDescriptor.setName("Compétition de test");
		final CompetitionCategory categ = new CompetitionCategory("catégorie de test", CompetitorSex.FEMALE, CompetitorCategory.BENJAMIN);
		OJTCompetitionCategories.addNewCompetitionCategory(categ);
		competitionDescriptor.setCategory(categ);
		Assert.assertEquals("catégorie_de_test-Compétition_de_test-Veynes-12_03_2005", FileNameComposer.composeDirectoryName(competitionDescriptor));
	}
	
	@Test
	public void testDecomposeDirectoryNameWithCategory() {
		final CompetitionCategory categ = new CompetitionCategory("catégorie de test", CompetitorSex.FEMALE, CompetitorCategory.BENJAMIN);
		OJTCompetitionCategories.addNewCompetitionCategory(categ);
		final CompetitionDescriptor competitionDescriptor = FileNameComposer.decomposeFileName("catégorie_de_test-Compétition_de_test-Veynes-12_03_2005-hjhkj.dat");
		Assert.assertEquals("12_03_2005", competitionDescriptor.getDate());
		Assert.assertEquals("Veynes", competitionDescriptor.getLocation());
		Assert.assertEquals("Compétition_de_test", competitionDescriptor.getCompetitionName());
		Assert.assertEquals("catégorie de test", competitionDescriptor.getCategory().getHumanName());
	}
}
