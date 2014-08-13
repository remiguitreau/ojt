package com.ojt.dao.ffjdacsv;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.ojt.Competitor;
import com.ojt.CompetitorCategory;
import com.ojt.CompetitorSex;
import com.ojt.GradeBelt;

import java.util.Calendar;

public class CSVCompetitorLineExtractorTest {

    private CSVCompetitorLineExtractor extractor;

    @Before
    public void initTest() {
        FFJDACSVConstants.clubs.clear();
        extractor = new CSVCompetitorLineExtractor();
    }

    @Test(expected = CSVCompetitorLineExtractionException.class)
    public void testWithEmptyLine() {
        extractor.extractCompetitorFromLine(new String[0], new FFJDACSVColumnsIndexes());
    }

    @Test(expected = CSVCompetitorLineExtractionException.class)
    public void testWithInvalidLine() {
        extractor.extractCompetitorFromLine(
                "1;skdjgvkjgfvkjsgvkshdkhsdkdvhksdh".split(FFJDACSVConstants.FIELDS_SEPARATOR_PATTERN),
                initColumnIndexes());
    }

    @Test
    public void testCompetitorExtraction() {
        final String line = "M222222222222222;15/10/2013 14:36:20;Q;NOM2 PRENOM2;M;13/10/2001;TOULON JUDO;83;Verte;;13;;syldial@sfr.fr;;TOULON JUDO;;;83150;BANDOL";
        final Competitor comp = extractor.extractCompetitorFromLine(line.split(";"), initColumnIndexes());
        Assert.assertEquals("M222222222222222", comp.getLicenseCode());
        Assert.assertEquals("NOM2", comp.getName());
        Assert.assertEquals("PRENOM2", comp.getFirstName());
        Assert.assertEquals("SEPA830000", comp.getClub().getClubCode());
        Assert.assertEquals("TOULON JUDO", comp.getClub().getClubName());
        Assert.assertEquals("83", comp.getClub().getDepartment());
        Assert.assertEquals(CompetitorSex.MALE, comp.getSex());
        Assert.assertEquals(GradeBelt.GREEN, comp.getGradeBelt());
        final Calendar cal = Calendar.getInstance();
        cal.setTime(comp.getBirthDate());
        Assert.assertEquals(13, cal.get(Calendar.DAY_OF_MONTH));
        Assert.assertEquals(Calendar.OCTOBER, cal.get(Calendar.MONTH));
        Assert.assertEquals(2001, cal.get(Calendar.YEAR));
        Assert.assertNotSame(CompetitorCategory.UNKNOWN, comp.getCategory());
        Assert.assertEquals(13f, comp.getWeight(), 0);
    }

    private FFJDACSVColumnsIndexes initColumnIndexes() {
        final FFJDACSVColumnsIndexes indexes = new FFJDACSVColumnsIndexes();
        indexes.licenseCodeIndex = 0;
        indexes.fullNameIndex = 3;
        indexes.sexIndex = 4;
        indexes.birthDateIndex = 5;
        indexes.clubNameIndex = 6;
        indexes.clubDepartmentIndex = 7;
        indexes.gradeIndex = 8;
        indexes.weightIndex = 10;
        return indexes;
    }

}
