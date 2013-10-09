package com.ojt.dao;

import com.ojt.Competitor;
import com.ojt.CompetitorList;

import java.util.List;

public class MockCompetitorsDao implements CompetitorsDao {

    @Override
    public void createCompetitor(final Competitor competitor) throws CompetitorCreationException {
        /* xxx */System.out.println("Competiteur crée : " + competitor.getDisplayName());
    }

    @Override
    public void deleteCompetitor(final Competitor competitor) throws CompetitorDeleteException {
        /* xxx */System.out.println("Competiteur supprimé : " + competitor.getDisplayName());
    }

    @Override
    public CompetitorList retrieveCompetitors() throws CompetitorRetrieveException {
        return new CompetitorList();
    }

    @Override
    public void updateCompetitor(final Competitor competitor) throws CompetitorUpdateException {
        /* xxx */System.out.println("Competiteur mis à jour : " + competitor.getDisplayName());
    }

    @Override
    public void deleteCompetitors(final List<Competitor> competitors) throws CompetitorDeleteException {
        if (competitors != null) {
            /* xxx */System.out.println("Suppression de " + String.valueOf(competitors.size())
                    + " competiteurs.");
        }
    }

}
