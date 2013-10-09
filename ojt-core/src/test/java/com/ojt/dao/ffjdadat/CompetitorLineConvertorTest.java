package com.ojt.dao.ffjdadat;

import java.util.Calendar;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.ojt.Club;
import com.ojt.Competitor;
import com.ojt.CompetitorSex;
import com.ojt.GradeBelt;


public class CompetitorLineConvertorTest {

	private CompetitorLineConvertor convertor;
	
	@Before
	public void init() {
		convertor = new CompetitorLineConvertor();
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
		final FFJDADatColumnsIndexes indexes = new FFJDADatColumnsIndexes();
		indexes.licenseCodeIndex = 1;
		indexes.nameIndex = 2;
		indexes.firstNameIndex = 3;
		indexes.clubCodeIndex = 10;
		indexes.clubNameIndex = 12;
		indexes.birthDateIndex = 6;
		indexes.sexIndex = 4;
		indexes.gradeIndex = 13;
		Assert.assertEquals("\"\";\"LICENSE\";\"NAME\";\"FIRST NAME\";\"Masculin\";\"\";\"23/11/1980\";\"\";\"\";\"\";\"CLUB CODE\";\"\";\"Club Name\";\"2\";\"\";\"\";\"\";\"\";\"\";\"\";\"\";\"\";\"\";\"\";\"12.5\";", convertor.convertCompetitorInLine(competitor, indexes, 24));
	}
	
	@Test
	public void testConvertCompetitorInLineWithNullBirthDate() {
		final Competitor competitor = new Competitor("LICENSE");
		competitor.setName("NAME");
		competitor.setFirstName("FIRST NAME");
		competitor.setClub(new Club("CLUB CODE", "23", "Club Name"));
		competitor.setWeight(Float.valueOf(12.5f));
		competitor.setSex(CompetitorSex.FEMALE);
		competitor.setBirthDate(null);
		competitor.setGradeBelt(GradeBelt.WHITE_YELLOW);
		final FFJDADatColumnsIndexes indexes = new FFJDADatColumnsIndexes();
		indexes.licenseCodeIndex = 1;
		indexes.nameIndex = 2;
		indexes.firstNameIndex = 3;
		indexes.clubCodeIndex = 10;
		indexes.clubNameIndex = 12;
		indexes.birthDateIndex = 6;
		indexes.sexIndex = 4;
		indexes.gradeIndex = 13;
		Assert.assertEquals("\"\";\"LICENSE\";\"NAME\";\"FIRST NAME\";\"Féminin\";\"\";\"\";\"\";\"\";\"\";\"CLUB CODE\";\"\";\"Club Name\";\"BJ\";\"\";\"\";\"\";\"\";\"\";\"\";\"\";\"\";\"\";\"\";\"12.5\";", convertor.convertCompetitorInLine(competitor, indexes, 24));
	}
	
	@Test
	public void testConvertCompetitorWithNullWeightInLine() {
		final Competitor competitor = new Competitor("LICENSE");
		competitor.setName("NAME");
		competitor.setFirstName("FIRST NAME");
		competitor.setClub(new Club("CLUB CODE", "23", "Club Name"));
		competitor.setSex(CompetitorSex.UNKNOWN);
		competitor.setWeight(null);
		final FFJDADatColumnsIndexes indexes = new FFJDADatColumnsIndexes();
		indexes.licenseCodeIndex = 1;
		indexes.nameIndex = 2;
		indexes.firstNameIndex = 3;
		indexes.clubCodeIndex = 10;
		indexes.clubNameIndex = 12;
		indexes.birthDateIndex = 6;
		indexes.sexIndex = 4;
		indexes.gradeIndex = 13;
		Assert.assertEquals("\"\";\"LICENSE\";\"NAME\";\"FIRST NAME\";\"\";\"\";\"\";\"\";\"\";\"\";\"CLUB CODE\";\"\";\"Club Name\";\"\";\"\";\"\";\"\";\"\";\"\";\"\";\"\";\"\";\"\";\"\";\"\";", convertor.convertCompetitorInLine(competitor, indexes, 24));
	}
}
