package com.ojt.dao.ffjdadat;

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

public class FFJDADatCompetitorsDaoTest {

    private FFJDADatCompetitorsDao dao;

    private File testFile;

    @Before
    public void initTest() throws IOException {
        testFile = new File(System.getProperty("java.io.tmpdir"), "test.dat");
        FileUtils.copyURLToFile(FFJDADatCompetitorsDaoTest.class.getResource("sepa05-test.dat"), testFile);
        dao = new FFJDADatCompetitorsDao(testFile, false);
    }

    @After
    public void cleanTest() {
        testFile.delete();
    }

    @Test
    public void testRetrieveCompetitors() throws URISyntaxException {
        final CompetitorList list = dao.retrieveCompetitors();
        Assert.assertEquals(4, list.size());
        Assert.assertEquals("LICENSE 1", list.get(0).getLicenseCode());
        Assert.assertEquals("TEST1", list.get(0).getName());
        Assert.assertEquals("01", list.get(0).getClub().getDepartment());
        final Calendar cal = Calendar.getInstance();
        cal.setTime(list.get(0).getBirthDate());
        Assert.assertEquals(10, cal.get(Calendar.DAY_OF_MONTH));
        Assert.assertEquals(Calendar.JULY, cal.get(Calendar.MONTH));
        Assert.assertEquals(2001, cal.get(Calendar.YEAR));

        Assert.assertEquals("LICENSE 4", list.get(3).getLicenseCode());
        Assert.assertEquals("TEST 4", list.get(3).getName());
        Assert.assertEquals("14", list.get(3).getClub().getDepartment());
        cal.setTime(list.get(3).getBirthDate());
        Assert.assertEquals(6, cal.get(Calendar.DAY_OF_MONTH));
        Assert.assertEquals(Calendar.OCTOBER, cal.get(Calendar.MONTH));
        Assert.assertEquals(2002, cal.get(Calendar.YEAR));
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
        Assert.assertEquals(
                "\"\";\"LICENSE\";\"NAME\";\"FIRST NAME\";\"\";\"\";\"\";\"\";\"\";\"\";\"CLUB CODE\";\"\";\"Club Name\";\"\";\"\";\"\";\"\";\"\";\"\";\"\";\"\";\"\";\"\";\"\";\"\";\"\";",
                lines.get(5));
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
        Assert.assertEquals(
                "\"\";\"LICENSE\";\"NAME\";\"FIRST NAME\";\"\";\"\";\"\";\"\";\"\";\"\";\"CLUB CODE\";\"\";\"Club Name\";\"\";\"\";\"\";\"\";\"\";\"\";\"\";\"\";\"\";\"\";\"\";\"\";\"23.1\";",
                lines.get(5));
    }

    @Test
    public void testDeleteCompetitorWithLicense() throws URISyntaxException, IOException {
        final Competitor competitor = new Competitor("LICENSE 2");
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
        competitor.setName("TEST3");
        competitor.setFirstName("THIRD");
        competitor.setClub(new Club("CLUB CODE", "23", "Club Name"));
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
        final Competitor competitor = new Competitor("LICENSE 2");
        competitor.setName("NAME");
        competitor.setFirstName("FIRST NAME");
        competitor.setClub(new Club("CLUB CODE", "23", "Club Name"));
        competitor.setWeight(Float.valueOf(23.1f));
        dao.updateCompetitor(competitor);
        Assert.assertTrue(((String) FileUtils.readLines(testFile).get(2)).endsWith("\"23.1\";"));
    }

    @Test
    public void testUpdateCompetitorWithoutLicense() throws URISyntaxException, IOException {
        final Competitor competitor = new Competitor();
        competitor.setName("TEST 4");
        competitor.setFirstName("FOURTH");
        competitor.setClub(new Club("CLUB CODE", "23", "Club Name"));
        competitor.setWeight(Float.valueOf(23.1f));
        dao.updateCompetitor(competitor);
        Assert.assertTrue(((String) FileUtils.readLines(testFile).get(4)).endsWith("\"23.1\";"));
    }
}
