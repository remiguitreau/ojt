package com.ojt.dao.ffjdacsv;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.ojt.Club;
import com.ojt.Competitor;
import com.ojt.CompetitorSex;
import com.ojt.GradeBelt;

import java.util.Calendar;

public class CSVCompetitorLineConvertorTest {

    private CSVCompetitorLineConvertor convertor;

    @Before
    public void init() {
        convertor = new CSVCompetitorLineConvertor();
    }

    @Test
    public void testConvertCompetitorInLine() {
        final Competitor competitor = new Competitor("LICENSE");
        competitor.setName("NAME");
        competitor.setFirstName("FIRST NAME");
        competitor.setClub(new Club("CLUB CODE", "23", "Club Name"));
        competitor.setSex(CompetitorSex.MALE);
        competitor.setWeight(Float.valueOf(12.5f));
        competitor.setGradeBelt(GradeBelt.SECOND_DAN);
        final Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 23);
        cal.set(Calendar.MONTH, Calendar.NOVEMBER);
        cal.set(Calendar.YEAR, 1980);
        competitor.setBirthDate(cal.getTime());
        final FFJDACSVColumnsIndexes indexes = new FFJDACSVColumnsIndexes();
        indexes.licenseCodeIndex = 1;
        indexes.fullNameIndex = 2;
        indexes.clubDepartmentIndex = 10;
        indexes.clubNameIndex = 12;
        indexes.birthDateIndex = 6;
        indexes.sexIndex = 4;
        indexes.gradeIndex = 13;
        indexes.weightIndex = 7;
        Assert.assertEquals(
                ";LICENSE;NAME FIRST NAME;;M;;23/11/1980;12.5;;;23;;Club Name;2eme Dan;;;;;;;;;;;;",
                convertor.convertCompetitorInLine(competitor, indexes, 24));

        competitor.setWeight(null);
        Assert.assertEquals(";LICENSE;NAME FIRST NAME;;M;;23/11/1980;;;;23;;Club Name;2eme Dan;;;;;;;;;;;;",
                convertor.convertCompetitorInLine(competitor, indexes, 24));
    }
}
