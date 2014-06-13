package com.ojt.dao.ffjdadat;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.ojt.Competitor;
import com.ojt.CompetitorCategory;
import com.ojt.CompetitorSex;
import com.ojt.GradeBelt;

import java.util.Calendar;

public class CompetitorLineExtractorTest {

    private CompetitorLineExtractor extractor;

    @Before
    public void initTest() {
        extractor = new CompetitorLineExtractor();
    }

    @Test(expected = CompetitorLineExtractionException.class)
    public void testWithEmptyLine() {
        extractor.extractCompetitorFromLine(new String[0], new FFJDADatColumnsIndexes());
    }

    @Test(expected = CompetitorLineExtractionException.class)
    public void testWithInvalidLine() {
        final FFJDADatColumnsIndexes indexes = initColumnIndexes();
        extractor.extractCompetitorFromLine(
                "\"1\";skdjgvkjgfvkjsgvkshdkhsdkdvhksdh".split(FFJDADatConstants.FIELDS_SEPARATOR_PATTERN),
                indexes);
    }

    @Test
    public void testCompetitorExtraction() {
        final FFJDADatColumnsIndexes indexes = initColumnIndexes();
        final String line = "\"11\";\"M1LICENSEID\";\"TEST\";\"FIRST\";\"Masculin\";\"17/09/2004\";\"3 route du test\";\"Domaine du test\";\"05000\";\"GAP\";\"SEPA050030\";\"A.S.P.T.T.GAP\";\"A.S.P.T.T.GAP\";\"OV\";\"Non\";\"\";\"\";\"\";\"\";\"\";\"\";\"\";\"\";\"\";\"\";";
        final Competitor comp = extractor.extractCompetitorFromLine(line.split(";"), indexes);
        Assert.assertEquals("M1LICENSEID", comp.getLicenseCode());
        Assert.assertEquals("TEST", comp.getName());
        Assert.assertEquals("FIRST", comp.getFirstName());
        Assert.assertEquals("SEPA050030", comp.getClub().getClubCode());
        Assert.assertEquals("A.S.P.T.T.GAP", comp.getClub().getClubName());
        Assert.assertEquals("05", comp.getClub().getDepartment());
        Assert.assertEquals(CompetitorSex.MALE, comp.getSex());
        Assert.assertEquals(GradeBelt.ORANGE_GREEN, comp.getGradeBelt());
        final Calendar cal = Calendar.getInstance();
        cal.setTime(comp.getBirthDate());
        Assert.assertEquals(17, cal.get(Calendar.DAY_OF_MONTH));
        Assert.assertEquals(Calendar.SEPTEMBER, cal.get(Calendar.MONTH));
        Assert.assertEquals(2004, cal.get(Calendar.YEAR));
        Assert.assertNotSame(CompetitorCategory.UNKNOWN, comp.getCategory());
        Assert.assertNull(comp.getWeight());
    }

    @Test
    public void testCompetitorWithWeightExtractionWithPatternAtTheEnd() {
        final FFJDADatColumnsIndexes indexes = initColumnIndexes();
        final String line = "\"11\";\"M1LICENSEID\";\"TEST\";\"FIRST\";\"Féminin\";\"17/09/2004\";\"3 route du test\";\"Domaine du test\";\"05000\";\"GAP\";\"SEPA050030\";\"A.S.P.T.T.GAP\";\"A.S.P.T.T.GAP\";\"3\";\"Non\";\"\";\"\";\"\";\"\";\"\";\"\";\"\";\"\";\"\";\"\";\"25.36\";";
        final Competitor comp = extractor.extractCompetitorFromLine(line.split(";"), indexes);
        Assert.assertEquals(CompetitorSex.FEMALE, comp.getSex());
        Assert.assertEquals(Float.valueOf(25.36f), comp.getWeight());
        Assert.assertEquals(GradeBelt.THIRD_DAN, comp.getGradeBelt());
    }

    @Test
    public void testCompetitorWithWeightExtractionWithoutPatternAtTheEnd() {
        final FFJDADatColumnsIndexes indexes = initColumnIndexes();
        final String line = "\"11\";\"M1LICENSEID\";\"TEST\";\"FIRST\";\"Masculin\";\"17/09/2004\";\"3 route du test\";\"Domaine du test\";\"05000\";\"GAP\";\"SEPA050030\";\"A.S.P.T.T.GAP\";\"A.S.P.T.T.GAP\";\"\";\"Non\";\"\";\"\";\"\";\"\";\"\";\"\";\"\";\"\";\"\";\"\";\"25.36\"";
        final Competitor comp = extractor.extractCompetitorFromLine(line.split(";"), indexes);
        Assert.assertEquals(Float.valueOf(25.36f), comp.getWeight());
        Assert.assertNull(comp.getGradeBelt());
    }

    @Test
    public void testCompetitorWithWeightAndCommaExtractionWithoutPatternAtTheEnd() {
        final FFJDADatColumnsIndexes indexes = initColumnIndexes();
        final String line = "\"11\";\"M1LICENSEID\";\"TEST\";\"FIRST\";\"Masculin\";\"17/09/2004\";\"3 route du test\";\"Domaine du test\";\"05000\";\"GAP\";\"SEPA050030\";\"A.S.P.T.T.GAP\";\"A.S.P.T.T.GAP\";\"\";\"Non\";\"\";\"\";\"\";\"\";\"\";\"\";\"\";\"\";\"\";\"\";\"25,36\"";
        final Competitor comp = extractor.extractCompetitorFromLine(line.split(";"), indexes);
        Assert.assertEquals(Float.valueOf(25.36f), comp.getWeight());
    }

    @Test
    public void testCompetitorWithoutBirthDate() {
        final FFJDADatColumnsIndexes indexes = initColumnIndexes();
        final String line = "\"11\";\"M1LICENSEID\";\"TEST\";\"FIRST\";\"Masculin\";\"\";\"3 route du test\";\"Domaine du test\";\"05000\";\"GAP\";\"SEPA050030\";\"A.S.P.T.T.GAP\";\"A.S.P.T.T.GAP\";\"\";\"Non\";\"\";\"\";\"\";\"\";\"\";\"\";\"\";\"\";\"\";\"\";\"25,36\"";
        final Competitor comp = extractor.extractCompetitorFromLine(line.split(";"), indexes);
        Assert.assertNull(comp.getBirthDate());
        Assert.assertEquals(CompetitorCategory.UNKNOWN, comp.getCategory());
    }

    @Test
    @Ignore
    public void test_competitor_extraction_with_no_code_club_but_dep_code() {
        final FFJDADatColumnsIndexes indexes = initColumnIndexes();
        indexes.clubCodeIndex = -1;
        indexes.departmentIndex = 15;
        final String line1 = "\"11\";\"M1LICENSEID\";\"TEST\";\"FIRST\";\"Masculin\";\"17/09/2004\";\"3 route du test\";\"Domaine du test\";\"05000\";\"GAP\";\"\";\"A.S.P.T.T.GAP\";\"A.S.P.T.T.GAP\";\"OV\";\"Non\";\"05\";\"\";\"\";\"\";\"\";\"\";\"\";\"\";\"\";\"\";";
        final Competitor comp1 = extractor.extractCompetitorFromLine(line1.split(";"), indexes);
        Assert.assertEquals("SEPA050001", comp1.getClub().getClubCode());
        Assert.assertEquals("A.S.P.T.T.GAP", comp1.getClub().getClubName());
        Assert.assertEquals("05", comp1.getClub().getDepartment());

        final String line2 = "\"12\";\"M1LICENSEID\";\"TEST\";\"FIRST\";\"Masculin\";\"17/09/2004\";\"3 route du test\";\"Domaine du test\";\"05000\";\"GAP\";\"\";\"A.S.P.T.T.GAP\";\"A.S.P.T.T.GAP\";\"OV\";\"Non\";\"05\";\"\";\"\";\"\";\"\";\"\";\"\";\"\";\"\";\"\";";
        final Competitor comp2 = extractor.extractCompetitorFromLine(line2.split(";"), indexes);
        Assert.assertEquals("SEPA050001", comp2.getClub().getClubCode());
        Assert.assertEquals("A.S.P.T.T.GAP", comp2.getClub().getClubName());
        Assert.assertEquals("05", comp2.getClub().getDepartment());

        final String line3 = "\"12\";\"M1LICENSEID\";\"TEST\";\"FIRST\";\"Masculin\";\"17/09/2004\";\"3 route du test\";\"Domaine du test\";\"05000\";\"GAP\";\"\";\"Test du sud\";\"Test du sud\";\"OV\";\"Non\";\"13\";\"\";\"\";\"\";\"\";\"\";\"\";\"\";\"\";\"\";";
        final Competitor comp3 = extractor.extractCompetitorFromLine(line3.split(";"), indexes);
        Assert.assertEquals("SEPA130001", comp3.getClub().getClubCode());
        Assert.assertEquals("Test du sud", comp3.getClub().getClubName());
        Assert.assertEquals("13", comp3.getClub().getDepartment());
    }

    private FFJDADatColumnsIndexes initColumnIndexes() {
        final FFJDADatColumnsIndexes indexes = new FFJDADatColumnsIndexes();
        indexes.licenseCodeIndex = 1;
        indexes.nameIndex = 2;
        indexes.firstNameIndex = 3;
        indexes.clubCodeIndex = 10;
        indexes.clubNameIndex = 12;
        indexes.birthDateIndex = 5;
        indexes.sexIndex = 4;
        indexes.gradeIndex = 13;
        return indexes;
    }
}
