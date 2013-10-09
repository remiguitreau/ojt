/**
 * 
 */
package com.ojt.dao.xls;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.ojt.Club;
import com.ojt.Competitor;
import com.ojt.dao.ffjdadat.FFJDADatConstants;

import java.io.File;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author cedric
 */
public class XlsCompetitorsDaoTest {
	List<Competitor> competitors = new ArrayList<Competitor>();

	XlsCompetitorsDao competitorsDao;

	List<Competitor> competitorsXls;

	/**
	 * Initialisation des tests
	 * @throws URISyntaxException
	 */
	@Before
	public void setUp() throws URISyntaxException {
		// competitorsDao = new XlsCompetitorsDao(new
		// File(getClass().getResource("reftest.xls").toURI()));
		//competitorsDao = new XlsCompetitorsDao(new File("reftest.xls"), false);
//		competitorsXls = new ArrayList<Competitor>(competitorsDao.retrieveCompetitors());
	}

	/**
	 * @throws ParseException 
	 * 
	 */
	@Test
	@Ignore
	public void instantiateCompetitorsTest() throws ParseException {
		competitors.add(new Competitor("TOTO 1", "titi 1", new Club("SEPA050030", "05", "A.S.P.T.T.GAP"), FFJDADatConstants.BIRTH_DATE_FORMAT.parse("23/11/1980"),
				35));
		competitors.add(new Competitor("TOTO 2", "titi 2",
				new Club("SEPA052260", "05", "J.C.OLYMPIEN EMBRUN"), 36));
		competitors.add(new Competitor("TOTO 3", "titi 3", new Club("SEPA051800", "05", "J.C.BRIANCONNAIS"), FFJDADatConstants.BIRTH_DATE_FORMAT.parse("12/02/2004"),
				37));
		competitors.add(new Competitor("TOTO 4", "titi 4", new Club("SEPA050140", "05",
				"JUJITSU CLUB GAPENCAIS"), 38));
		competitors.add(new Competitor("TOTO 5", "titi 5", new Club("SEPA050030", "05", "A.S.P.T.T.GAP"),
				39));
		competitors.add(new Competitor("TOTO 6", "titi 6", new Club("SEPA050180", "05", "KODOKAN JUDO GAP"),
				40));
		competitors.add(new Competitor("TOTO 7", "titi 7",
				new Club("SEPA052260", "05", "J.C.OLYMPIEN EMBRUN"), 41));
		competitors.add(new Competitor("TOTO 8", "titi 8", new Club("SEPA051800", "05", "J.C.BRIANCONNAIS"),
				42));
		competitors.add(new Competitor("TOTO 9", "titi 9", new Club("SEPA050170", "05",
				"JUDO CLUB DU GUILLESTROIS"), 43));
		competitors.add(new Competitor("TOTO 10", "titi 10", new Club("SEPA050170", "05",
				"JUDO CLUB DU GUILLESTROIS"), 44));
		competitors.add(new Competitor("TOTO 11", "titi 11", new Club("SEPA050030", "05", "A.S.P.T.T.GAP"),
				45));
		competitors.add(new Competitor("TOTO 12", "titi 12", new Club("SEPA050030", "05", "A.S.P.T.T.GAP"),
				46));
		competitors.add(new Competitor("TOTO 13", "titi 13",
				new Club("SEPA050180", "05", "KODOKAN JUDO GAP"), 47));
		competitors.add(new Competitor("TOTO 14", "titi 14",
				new Club("SEPA050180", "05", "KODOKAN JUDO GAP"), 48));
		competitors.add(new Competitor("TOTO 15", "titi 15", new Club("SEPA050030", "05", "A.S.P.T.T.GAP"),
				49));
		competitors.add(new Competitor("TOTO 16", "titi 16", new Club("SEPA130", "13", "Zip"), 39));
		System.out.println(competitors);
		System.out.println(competitorsXls);
		org.junit.Assert.assertArrayEquals(competitors.toArray(), competitorsXls.toArray());
	}

	@Test
	public void testUpdateCompetitor() {

	}
}
