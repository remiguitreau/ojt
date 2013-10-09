package com.ojt.dao.ffjdadat;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;


public class FFJDADatColumnIndexesExtractorTest {

	private FFJDADatColumnIndexesExtractor extractor;
	
	@Before
	public void initTest() {
		extractor = new FFJDADatColumnIndexesExtractor();
	}
	
	@Test(expected=FFJDADatBadFormatException.class)
	public void testBadFormat() {
		extractor.extractColumnIndexesFromLine(new String[12]);
	}
	
	@Test
	public void testFFJDADatColumnIndexesExtraction() {
		final FFJDADatColumnsIndexes indexes = extractor.extractColumnIndexesFromLine("\"Numéro d'ordre\";\"Code Identifiant\";\"Nom\";\"Prénom\";\"Sexe\";\"Date de naissance\";\"Adresse 1/2\";\"Adresse 2/2\";\"Code Postal\";\"Ville\";\"Code Club\";\"Désignation du Club\";\"Nom_club_complet\";\"Dan actuel\";\"Ceinture Noire\";\"DAN N°1\";\"DAN N°2\";\"DAN N°3\";\"DAN N°4\";\"DAN N°5\";\"DAN N°6\";\"DAN N°7\";\"DAN N°8\";\"DAN N°9\";\"DAN N°10\";".split(FFJDADatConstants.FIELDS_SEPARATOR_PATTERN));
		Assert.assertEquals(1, indexes.licenseCodeIndex);
		Assert.assertEquals(2, indexes.nameIndex);
		Assert.assertEquals(3, indexes.firstNameIndex);
		Assert.assertEquals(10, indexes.clubCodeIndex);
		Assert.assertEquals(12, indexes.clubNameIndex);
		Assert.assertEquals(5, indexes.birthDateIndex);
	}
	
	@Test
	public void testFFJDADatColumnIndexesExtractionWithAnotherOrganisation() {
		final FFJDADatColumnsIndexes indexes = extractor.extractColumnIndexesFromLine("\"Nom\";\"Sexe\";\"Date de naissance\";\"Code Identifiant\";\"Adresse 1/2\";\"Adresse 2/2\";\"Code Postal\";\"Ville\";\"Désignation du Club\";\"Nom_club_complet\";\"Prénom\";\"Ceinture Noire\";\"DAN N°1\";\"DAN N°2\";\"Code Club\";\"DAN N°9\";\"DAN N°10\";".split(FFJDADatConstants.FIELDS_SEPARATOR_PATTERN));
		Assert.assertEquals(3, indexes.licenseCodeIndex);
		Assert.assertEquals(0, indexes.nameIndex);
		Assert.assertEquals(10, indexes.firstNameIndex);
		Assert.assertEquals(14, indexes.clubCodeIndex);
		Assert.assertEquals(9, indexes.clubNameIndex);
		Assert.assertEquals(2, indexes.birthDateIndex);
	}
}
