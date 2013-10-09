package com.ojt.dao;

import com.ojt.Competitor;
import com.ojt.CompetitorList;

import java.util.List;

/**
 * Interface permettant de s'affranchir du type de fichie a importer (xls, xlsx,
 * ods)
 * @author cedric
 * @since 27/03/08 : cr�ation
 */

public interface CompetitorsDao {
	String COLUMN_ID_NAME = "OJT_NAME";

	String COLUMN_ID_LICENSE = "OJT_LICENSE";
	
	String COLUMN_ID_BIRTH_DATE = "OJT_BIRTH_DATE";

	String COLUMN_ID_SUBNAME = "OJT_SUBNAME";

	String COLUMN_ID_CLUB_ID = "OJT_CLUB_ID";

	String COLUMN_ID_CLUB_NAME = "OJT_CLUB_NAME";

	String COLUMN_ID_WEIGHT = "OJT_WEIGHT";
	
	String COLUMN_ID_GRADE = "OJT_GRADE";

	CompetitorList retrieveCompetitors() throws CompetitorRetrieveException;

	void updateCompetitor(final Competitor competitor) throws CompetitorUpdateException;

	void createCompetitor(final Competitor competitor) throws CompetitorCreationException;

	void deleteCompetitor(final Competitor competitor) throws CompetitorDeleteException;

	void deleteCompetitors(final List<Competitor> competitors) throws CompetitorDeleteException;
}
