package com.ojt;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.ojt.dao.ffjdadat.FFJDADatConstants;

import java.text.ParseException;
import java.util.Calendar;

public class CompetitorCategoryTest {

    private static int YEAR_OF_TEST = 2014;

    private Calendar referenceCalendar;

    @Before
    public void initTest() {
        referenceCalendar = Calendar.getInstance();
        referenceCalendar.set(Calendar.YEAR, YEAR_OF_TEST);
    }

    @Test
    public void testWithPREPOUSSIN() throws ParseException {
        referenceCalendar.set(Calendar.MONTH, Calendar.MARCH);
        Assert.assertEquals(CompetitorCategory.PREPOUSSIN,
                CompetitorCategory.retrieveCategoryFromBirthDateAndCompetitionDate(
                        FFJDADatConstants.BIRTH_DATE_FORMAT.parse("23/02/2006"), referenceCalendar.getTime()));
        Assert.assertEquals(CompetitorCategory.PREPOUSSIN,
                CompetitorCategory.retrieveCategoryFromBirthDateAndCompetitionDate(
                        FFJDADatConstants.BIRTH_DATE_FORMAT.parse("23/02/2007"), referenceCalendar.getTime()));
        referenceCalendar.set(Calendar.MONTH, Calendar.OCTOBER);
        Assert.assertNotSame(CompetitorCategory.PREPOUSSIN,
                CompetitorCategory.retrieveCategoryFromBirthDateAndCompetitionDate(
                        FFJDADatConstants.BIRTH_DATE_FORMAT.parse("23/02/2006"), referenceCalendar.getTime()));
        Assert.assertEquals(CompetitorCategory.PREPOUSSIN,
                CompetitorCategory.retrieveCategoryFromBirthDateAndCompetitionDate(
                        FFJDADatConstants.BIRTH_DATE_FORMAT.parse("23/02/2007"), referenceCalendar.getTime()));
        Assert.assertEquals(CompetitorCategory.PREPOUSSIN,
                CompetitorCategory.retrieveCategoryFromBirthDateAndCompetitionDate(
                        FFJDADatConstants.BIRTH_DATE_FORMAT.parse("23/02/2008"), referenceCalendar.getTime()));
    }

    @Test
    public void testWithPOUSSIN() throws ParseException {
        referenceCalendar.set(Calendar.MONTH, Calendar.MARCH);
        Assert.assertEquals(CompetitorCategory.POUSSIN,
                CompetitorCategory.retrieveCategoryFromBirthDateAndCompetitionDate(
                        FFJDADatConstants.BIRTH_DATE_FORMAT.parse("23/02/2004"), referenceCalendar.getTime()));
        Assert.assertEquals(CompetitorCategory.POUSSIN,
                CompetitorCategory.retrieveCategoryFromBirthDateAndCompetitionDate(
                        FFJDADatConstants.BIRTH_DATE_FORMAT.parse("23/02/2005"), referenceCalendar.getTime()));
        referenceCalendar.set(Calendar.MONTH, Calendar.OCTOBER);
        Assert.assertNotSame(CompetitorCategory.POUSSIN,
                CompetitorCategory.retrieveCategoryFromBirthDateAndCompetitionDate(
                        FFJDADatConstants.BIRTH_DATE_FORMAT.parse("23/02/2004"), referenceCalendar.getTime()));
        Assert.assertEquals(CompetitorCategory.POUSSIN,
                CompetitorCategory.retrieveCategoryFromBirthDateAndCompetitionDate(
                        FFJDADatConstants.BIRTH_DATE_FORMAT.parse("23/02/2005"), referenceCalendar.getTime()));
        Assert.assertEquals(CompetitorCategory.POUSSIN,
                CompetitorCategory.retrieveCategoryFromBirthDateAndCompetitionDate(
                        FFJDADatConstants.BIRTH_DATE_FORMAT.parse("23/02/2006"), referenceCalendar.getTime()));
    }

    @Test
    public void testWithBENJAMIN() throws ParseException {
        referenceCalendar.set(Calendar.MONTH, Calendar.MARCH);
        Assert.assertEquals(CompetitorCategory.BENJAMIN,
                CompetitorCategory.retrieveCategoryFromBirthDateAndCompetitionDate(
                        FFJDADatConstants.BIRTH_DATE_FORMAT.parse("23/02/2002"), referenceCalendar.getTime()));
        Assert.assertEquals(CompetitorCategory.BENJAMIN,
                CompetitorCategory.retrieveCategoryFromBirthDateAndCompetitionDate(
                        FFJDADatConstants.BIRTH_DATE_FORMAT.parse("23/02/2003"), referenceCalendar.getTime()));
        referenceCalendar.set(Calendar.MONTH, Calendar.OCTOBER);
        Assert.assertNotSame(CompetitorCategory.BENJAMIN,
                CompetitorCategory.retrieveCategoryFromBirthDateAndCompetitionDate(
                        FFJDADatConstants.BIRTH_DATE_FORMAT.parse("23/02/2002"), referenceCalendar.getTime()));
        Assert.assertEquals(CompetitorCategory.BENJAMIN,
                CompetitorCategory.retrieveCategoryFromBirthDateAndCompetitionDate(
                        FFJDADatConstants.BIRTH_DATE_FORMAT.parse("23/02/2003"), referenceCalendar.getTime()));
        Assert.assertEquals(CompetitorCategory.BENJAMIN,
                CompetitorCategory.retrieveCategoryFromBirthDateAndCompetitionDate(
                        FFJDADatConstants.BIRTH_DATE_FORMAT.parse("23/02/2004"), referenceCalendar.getTime()));
    }

    @Test
    public void testWithMINIME() throws ParseException {
        referenceCalendar.set(Calendar.MONTH, Calendar.MARCH);
        Assert.assertEquals(CompetitorCategory.MINIME,
                CompetitorCategory.retrieveCategoryFromBirthDateAndCompetitionDate(
                        FFJDADatConstants.BIRTH_DATE_FORMAT.parse("23/02/2000"), referenceCalendar.getTime()));
        Assert.assertEquals(CompetitorCategory.MINIME,
                CompetitorCategory.retrieveCategoryFromBirthDateAndCompetitionDate(
                        FFJDADatConstants.BIRTH_DATE_FORMAT.parse("23/02/2001"), referenceCalendar.getTime()));
        referenceCalendar.set(Calendar.MONTH, Calendar.OCTOBER);
        Assert.assertNotSame(CompetitorCategory.MINIME,
                CompetitorCategory.retrieveCategoryFromBirthDateAndCompetitionDate(
                        FFJDADatConstants.BIRTH_DATE_FORMAT.parse("23/02/2000"), referenceCalendar.getTime()));
        Assert.assertEquals(CompetitorCategory.MINIME,
                CompetitorCategory.retrieveCategoryFromBirthDateAndCompetitionDate(
                        FFJDADatConstants.BIRTH_DATE_FORMAT.parse("23/02/2001"), referenceCalendar.getTime()));
        Assert.assertEquals(CompetitorCategory.MINIME,
                CompetitorCategory.retrieveCategoryFromBirthDateAndCompetitionDate(
                        FFJDADatConstants.BIRTH_DATE_FORMAT.parse("23/02/2002"), referenceCalendar.getTime()));
    }

    @Test
    public void testWithCADET() throws ParseException {
        referenceCalendar.set(Calendar.MONTH, Calendar.MARCH);
        Assert.assertNotSame(CompetitorCategory.CADET,
                CompetitorCategory.retrieveCategoryFromBirthDateAndCompetitionDate(
                        FFJDADatConstants.BIRTH_DATE_FORMAT.parse("23/02/1996"), referenceCalendar.getTime()));
        Assert.assertEquals(CompetitorCategory.CADET,
                CompetitorCategory.retrieveCategoryFromBirthDateAndCompetitionDate(
                        FFJDADatConstants.BIRTH_DATE_FORMAT.parse("23/02/1997"), referenceCalendar.getTime()));
        Assert.assertEquals(CompetitorCategory.CADET,
                CompetitorCategory.retrieveCategoryFromBirthDateAndCompetitionDate(
                        FFJDADatConstants.BIRTH_DATE_FORMAT.parse("23/02/1998"), referenceCalendar.getTime()));
        Assert.assertEquals(CompetitorCategory.CADET,
                CompetitorCategory.retrieveCategoryFromBirthDateAndCompetitionDate(
                        FFJDADatConstants.BIRTH_DATE_FORMAT.parse("23/02/1999"), referenceCalendar.getTime()));
        Assert.assertNotSame(CompetitorCategory.CADET,
                CompetitorCategory.retrieveCategoryFromBirthDateAndCompetitionDate(
                        FFJDADatConstants.BIRTH_DATE_FORMAT.parse("23/02/2000"), referenceCalendar.getTime()));
        referenceCalendar.set(Calendar.MONTH, Calendar.OCTOBER);
        Assert.assertNotSame(CompetitorCategory.CADET,
                CompetitorCategory.retrieveCategoryFromBirthDateAndCompetitionDate(
                        FFJDADatConstants.BIRTH_DATE_FORMAT.parse("23/02/1997"), referenceCalendar.getTime()));
        Assert.assertEquals(CompetitorCategory.CADET,
                CompetitorCategory.retrieveCategoryFromBirthDateAndCompetitionDate(
                        FFJDADatConstants.BIRTH_DATE_FORMAT.parse("23/02/2000"), referenceCalendar.getTime()));
    }

    @Test
    public void testWithJUNIOR() throws ParseException {
        referenceCalendar.set(Calendar.MONTH, Calendar.MARCH);
        Assert.assertNotSame(CompetitorCategory.JUNIOR,
                CompetitorCategory.retrieveCategoryFromBirthDateAndCompetitionDate(
                        FFJDADatConstants.BIRTH_DATE_FORMAT.parse("23/02/1997"), referenceCalendar.getTime()));
        Assert.assertEquals(CompetitorCategory.JUNIOR,
                CompetitorCategory.retrieveCategoryFromBirthDateAndCompetitionDate(
                        FFJDADatConstants.BIRTH_DATE_FORMAT.parse("23/02/1996"), referenceCalendar.getTime()));
        Assert.assertEquals(CompetitorCategory.JUNIOR,
                CompetitorCategory.retrieveCategoryFromBirthDateAndCompetitionDate(
                        FFJDADatConstants.BIRTH_DATE_FORMAT.parse("23/02/1995"), referenceCalendar.getTime()));
        Assert.assertEquals(CompetitorCategory.JUNIOR,
                CompetitorCategory.retrieveCategoryFromBirthDateAndCompetitionDate(
                        FFJDADatConstants.BIRTH_DATE_FORMAT.parse("23/02/1994"), referenceCalendar.getTime()));
        Assert.assertNotSame(CompetitorCategory.JUNIOR,
                CompetitorCategory.retrieveCategoryFromBirthDateAndCompetitionDate(
                        FFJDADatConstants.BIRTH_DATE_FORMAT.parse("23/02/1993"), referenceCalendar.getTime()));
        referenceCalendar.set(Calendar.MONTH, Calendar.OCTOBER);
        Assert.assertNotSame(CompetitorCategory.JUNIOR,
                CompetitorCategory.retrieveCategoryFromBirthDateAndCompetitionDate(
                        FFJDADatConstants.BIRTH_DATE_FORMAT.parse("23/02/1991"), referenceCalendar.getTime()));
        Assert.assertNotSame(CompetitorCategory.JUNIOR,
                CompetitorCategory.retrieveCategoryFromBirthDateAndCompetitionDate(
                        FFJDADatConstants.BIRTH_DATE_FORMAT.parse("23/02/1994"), referenceCalendar.getTime()));
        Assert.assertEquals(CompetitorCategory.JUNIOR,
                CompetitorCategory.retrieveCategoryFromBirthDateAndCompetitionDate(
                        FFJDADatConstants.BIRTH_DATE_FORMAT.parse("23/02/1997"), referenceCalendar.getTime()));
    }

    @Test
    public void testWithSENIOR() throws ParseException {
        referenceCalendar.set(Calendar.MONTH, Calendar.MARCH);
        Assert.assertEquals(CompetitorCategory.SENIOR,
                CompetitorCategory.retrieveCategoryFromBirthDateAndCompetitionDate(
                        FFJDADatConstants.BIRTH_DATE_FORMAT.parse("23/02/1988"), referenceCalendar.getTime()));
        Assert.assertEquals(CompetitorCategory.SENIOR,
                CompetitorCategory.retrieveCategoryFromBirthDateAndCompetitionDate(
                        FFJDADatConstants.BIRTH_DATE_FORMAT.parse("23/02/1989"), referenceCalendar.getTime()));
        Assert.assertEquals(CompetitorCategory.SENIOR,
                CompetitorCategory.retrieveCategoryFromBirthDateAndCompetitionDate(
                        FFJDADatConstants.BIRTH_DATE_FORMAT.parse("23/02/1990"), referenceCalendar.getTime()));
        Assert.assertEquals(CompetitorCategory.SENIOR,
                CompetitorCategory.retrieveCategoryFromBirthDateAndCompetitionDate(
                        FFJDADatConstants.BIRTH_DATE_FORMAT.parse("23/02/1993"), referenceCalendar.getTime()));
        Assert.assertNotSame(CompetitorCategory.SENIOR,
                CompetitorCategory.retrieveCategoryFromBirthDateAndCompetitionDate(
                        FFJDADatConstants.BIRTH_DATE_FORMAT.parse("23/02/1994"), referenceCalendar.getTime()));
        referenceCalendar.set(Calendar.MONTH, Calendar.OCTOBER);
        Assert.assertEquals(CompetitorCategory.SENIOR,
                CompetitorCategory.retrieveCategoryFromBirthDateAndCompetitionDate(
                        FFJDADatConstants.BIRTH_DATE_FORMAT.parse("23/02/1994"), referenceCalendar.getTime()));
        Assert.assertEquals(CompetitorCategory.SENIOR,
                CompetitorCategory.retrieveCategoryFromBirthDateAndCompetitionDate(
                        FFJDADatConstants.BIRTH_DATE_FORMAT.parse("23/02/1992"), referenceCalendar.getTime()));
        Assert.assertEquals(CompetitorCategory.SENIOR,
                CompetitorCategory.retrieveCategoryFromBirthDateAndCompetitionDate(
                        FFJDADatConstants.BIRTH_DATE_FORMAT.parse("23/02/1991"), referenceCalendar.getTime()));
        Assert.assertEquals(CompetitorCategory.SENIOR,
                CompetitorCategory.retrieveCategoryFromBirthDateAndCompetitionDate(
                        FFJDADatConstants.BIRTH_DATE_FORMAT.parse("23/02/1980"), referenceCalendar.getTime()));
        Assert.assertEquals(CompetitorCategory.SENIOR,
                CompetitorCategory.retrieveCategoryFromBirthDateAndCompetitionDate(
                        FFJDADatConstants.BIRTH_DATE_FORMAT.parse("23/02/1975"), referenceCalendar.getTime()));
    }
}
