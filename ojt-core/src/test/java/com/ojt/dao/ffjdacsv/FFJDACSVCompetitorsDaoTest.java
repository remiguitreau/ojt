package com.ojt.dao.ffjdacsv;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.ojt.Club;
import com.ojt.Competitor;
import com.ojt.CompetitorList;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Calendar;
import java.util.List;

public class FFJDACSVCompetitorsDaoTest {
    private FFJDACSVCompetitorsDao dao;

    private File testFile;

    @Before
    public void initTest() throws IOException {
        testFile = new File(System.getProperty("java.io.tmpdir"), "test.csv");
        FileUtils.copyURLToFile(FFJDACSVCompetitorsDaoTest.class.getResource("sepa05-test.csv"), testFile);
        dao = new FFJDACSVCompetitorsDao(testFile, false);
    }

    @After
    public void cleanTest() {
        testFile.delete();
    }

    @Test
    public void testRetrieveCompetitors() throws URISyntaxException {
        final CompetitorList list = dao.retrieveCompetitors();
        Assert.assertEquals(4, list.size());
        Assert.assertEquals("M111111111111111", list.get(0).getLicenseCode());
        Assert.assertEquals("NOM1", list.get(0).getName());
        Assert.assertEquals("PRENOM1", list.get(0).getFirstName());
        Assert.assertEquals("05", list.get(0).getClub().getDepartment());
        final Calendar cal = Calendar.getInstance();
        cal.setTime(list.get(0).getBirthDate());
        Assert.assertEquals(06, cal.get(Calendar.DAY_OF_MONTH));
        Assert.assertEquals(Calendar.NOVEMBER, cal.get(Calendar.MONTH));
        Assert.assertEquals(2000, cal.get(Calendar.YEAR));
        Assert.assertEquals(12.56f, list.get(0).getWeightAsFloat(), 0);

        Assert.assertEquals("F444444444444444", list.get(3).getLicenseCode());
        Assert.assertEquals("NOM4", list.get(3).getName());
        Assert.assertEquals("PRENOM4 COMP4", list.get(3).getFirstName());
        Assert.assertEquals("83", list.get(3).getClub().getDepartment());
        cal.setTime(list.get(3).getBirthDate());
        Assert.assertEquals(19, cal.get(Calendar.DAY_OF_MONTH));
        Assert.assertEquals(Calendar.SEPTEMBER, cal.get(Calendar.MONTH));
        Assert.assertEquals(2001, cal.get(Calendar.YEAR));
        Assert.assertEquals(16f, list.get(3).getWeightAsFloat(), 0);
    }

    @Test
    public void testCreateCompetitorWithoutWeight() throws URISyntaxException, IOException {
        final Competitor competitor = new Competitor("LICENSE");
        competitor.setName("NAME");
        competitor.setFirstName("FIRST NAME");
        competitor.setClub(new Club("CLUB CODE", "23", "Club Name"));
        competitor.setWeight(null);
        dao.createCompetitor(competitor);
        final List<String> lines = FileUtils.readLines(testFile);
        Assert.assertEquals(6, lines.size());
        Assert.assertEquals("LICENSE;;;NAME FIRST NAME;;;Club Name;23;;;;;;;;;;;;;", lines.get(5));
    }

    @Test
    public void testCreateCompetitorWithWeight() throws URISyntaxException, IOException {
        final Competitor competitor = new Competitor("LICENSE");
        competitor.setName("NAME");
        competitor.setFirstName("FIRST NAME");
        competitor.setClub(new Club("CLUB CODE", "23", "Club Name"));
        competitor.setWeight(Float.valueOf(23.1f));
        dao.createCompetitor(competitor);
        final List<String> lines = FileUtils.readLines(testFile);
        Assert.assertEquals(6, lines.size());
        Assert.assertEquals("LICENSE;;;NAME FIRST NAME;;;Club Name;23;;;23.1;;;;;;;;;;", lines.get(5));
    }

    @Test
    public void testDeleteCompetitorWithLicense() throws URISyntaxException, IOException {
        final Competitor competitor = new Competitor("M222222222222222");
        competitor.setName("NAME");
        competitor.setFirstName("FIRST NAME");
        competitor.setClub(new Club("CLUB CODE", "23", "Club Name"));
        competitor.setWeight(Float.valueOf(23.1f));
        dao.deleteCompetitor(competitor);
        final List<String> lines = FileUtils.readLines(testFile);
        Assert.assertEquals(4, lines.size());
    }

    @Test
    public void testDeleteCompetitorWithoutLicense() throws URISyntaxException, IOException {
        final Competitor competitor = new Competitor();
        competitor.setName("NOM3");
        competitor.setFirstName("PRENOM3");
        competitor.setClub(new Club("SEPA130000", "13", "CS MONTOLIVET BOIS-LUZY"));
        dao.deleteCompetitor(competitor);
        final List<String> lines = FileUtils.readLines(testFile);
        Assert.assertEquals(4, lines.size());
    }

    @Test
    public void testDeleteUnexistedCompetitorWithoutLicense() throws URISyntaxException, IOException {
        final Competitor competitor = new Competitor();
        competitor.setName("TEST3");
        competitor.setFirstName("Coucou");
        competitor.setClub(new Club("CLUB CODE", "23", "Club Name"));
        dao.deleteCompetitor(competitor);
        final List<String> lines = FileUtils.readLines(testFile);
        Assert.assertEquals(5, lines.size());
    }

    @Test
    public void testDeleteUnexistedCompetitorWithLicense() throws URISyntaxException, IOException {
        final Competitor competitor = new Competitor("BAD");
        competitor.setName("TEST3");
        competitor.setFirstName("THIRD");
        competitor.setClub(new Club("CLUB CODE", "23", "Club Name"));
        dao.deleteCompetitor(competitor);
        final List<String> lines = FileUtils.readLines(testFile);
        Assert.assertEquals(5, lines.size());
    }

    @Test
    public void testUpdateCompetitorWithLicense() throws URISyntaxException, IOException {
        final Competitor competitor = new Competitor("M222222222222222");
        competitor.setName("NAME");
        competitor.setFirstName("FIRST NAME");
        competitor.setClub(new Club("CLUB CODE", "35", "Club Name"));
        competitor.setWeight(Float.valueOf(23.1f));
        dao.updateCompetitor(competitor);
        Assert.assertEquals(23.1f, dao.retrieveCompetitors().get(1).getWeightAsFloat(), 0);
        Assert.assertEquals("NAME", dao.retrieveCompetitors().get(1).getName());
        Assert.assertEquals("FIRST NAME", dao.retrieveCompetitors().get(1).getFirstName());
        Assert.assertNull(dao.retrieveCompetitors().get(1).getGradeBelt());
        Assert.assertEquals("Club Name", dao.retrieveCompetitors().get(1).getClub().getClubName());
        Assert.assertEquals("35", dao.retrieveCompetitors().get(1).getClub().getDepartment());
    }

    @Test
    public void testUpdateCompetitorWithoutLicense() throws URISyntaxException, IOException {
        final Competitor competitor = dao.retrieveCompetitors().get(3);
        competitor.setWeight(Float.valueOf(56.38f));
        dao.updateCompetitor(competitor);
        Assert.assertEquals(56.38f, dao.retrieveCompetitors().get(3).getWeightAsFloat(), 0);
    }
}
