package com.ojt.dao.xls;

import java.util.ArrayList;
import java.util.List;

import com.ojt.Categorie;
import com.ojt.Club;
import com.ojt.Competitor;
import com.ojt.CompetitorCategory;

/**
 * Simulacre pour la classe XlsCompetitorsDao
 * @author cedric
 * @since 01/04/08 : creation
 */

public class MockXlsCompetitorsDao {

	public MockXlsCompetitorsDao() {
		
	}
	
	public List<Competitor> retrieveCompetitors() {
		List<Competitor> competitors = new ArrayList<Competitor>();
		competitors.add(new Competitor("TOTO", "toto", new Club("SEPA050030", "05", "gap"), CompetitorCategory.MINIME, 35));
		competitors.add(new Competitor("TITI", "titi", new Club("SEPA050030", "05", "gap"), CompetitorCategory.MINIME, 35));
		competitors.add(new Competitor("TUTU", "tutu", new Club("SEPA130001", "13", "marseille"), CompetitorCategory.MINIME, 35));
		competitors.add(new Competitor("TATA", "tata", new Club("SEPA050050", "05", "veynes"), CompetitorCategory.MINIME, 35));
		competitors.add(new Competitor("TATA", "tata", new Club("SEPA050020", "05", "aye"), CompetitorCategory.MINIME, 35));
	
		return competitors ;
	}
}
