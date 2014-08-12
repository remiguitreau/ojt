package com.ojt.dao.ffjdacsv;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import com.ojt.Competitor;
import com.ojt.CompetitorList;
import com.ojt.OjtConstants;
import com.ojt.dao.CompetitorCreationException;
import com.ojt.dao.CompetitorDeleteException;
import com.ojt.dao.CompetitorRetrieveException;
import com.ojt.dao.CompetitorUpdateException;
import com.ojt.dao.CompetitorsDao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class FFJDACSVCompetitorsDao implements CompetitorsDao {

    private final Logger logger = Logger.getLogger(FFJDACSVCompetitorsDao.class);

    private final File competitorsFile;

    private final CSVCompetitorLineExtractor competitorLineExtractor = new CSVCompetitorLineExtractor();

    private final CSVCompetitorLineConvertor competitorLineConvertor = new CSVCompetitorLineConvertor();

    private final FFJDACSVColumnIndexesExtractor columnIndexesExtractor = new FFJDACSVColumnIndexesExtractor();

    private final boolean retrieveOnlyCompetitorsWithWeight;

    public FFJDACSVCompetitorsDao(final File competitorsFile, final boolean retrieveOnlyCompetitorsWithWeight) {
        this.competitorsFile = competitorsFile;
        this.retrieveOnlyCompetitorsWithWeight = retrieveOnlyCompetitorsWithWeight;
    }

    @Override
    public void createCompetitor(final Competitor competitor) throws CompetitorCreationException {
        BufferedWriter writer = null;
        try {
            final List<String> lines = readLines();
            final String[] splittedHeaderLine = lines.get(0).split(FFJDACSVConstants.FIELDS_SEPARATOR_PATTERN);
            final FFJDACSVColumnsIndexes columnsIndexes = columnIndexesExtractor.extractColumnIndexesFromLine(splittedHeaderLine);
            writer = new BufferedWriter(new FileWriter(competitorsFile, true));
            writer.newLine();
            writer.append(competitorLineConvertor.convertCompetitorInLine(competitor, columnsIndexes,
                    splittedHeaderLine.length));
            writer.flush();
        } catch (final Exception ex) {
            throw new CompetitorCreationException(ex);
        } finally {
            IOUtils.closeQuietly(writer);
        }
    }

    @Override
    public void deleteCompetitor(final Competitor competitor) throws CompetitorDeleteException {
        try {
            final List<String> lines = readLines();
            final FFJDACSVColumnsIndexes columnsIndexes = columnIndexesExtractor.extractColumnIndexesFromLine(lines.get(
                    0).split(FFJDACSVConstants.FIELDS_SEPARATOR_PATTERN));
            boolean competitorDeleted = false;
            for (final Iterator<String> it = lines.iterator(); it.hasNext() && !competitorDeleted;) {
                try {
                    final Competitor extractedCompetitor = competitorLineExtractor.extractCompetitorFromLine(
                            it.next().split(FFJDACSVConstants.FIELDS_SEPARATOR_PATTERN), columnsIndexes);
                    if (competitorAreEquals(competitor, extractedCompetitor)) {
                        it.remove();
                        competitorDeleted = true;
                    }
                } catch (final CSVCompetitorLineExtractionException ex) {
                    logger.info(ex.getMessage());
                }
            }
            if (competitorDeleted) {
                FileUtils.writeLines(competitorsFile, lines);
            }
        } catch (final Exception ex) {
            throw new CompetitorDeleteException(ex);
        }
    }

    @Override
    public void deleteCompetitors(final List<Competitor> competitors) throws CompetitorDeleteException {
        try {
            final List<String> lines = readLines();
            final FFJDACSVColumnsIndexes columnsIndexes = columnIndexesExtractor.extractColumnIndexesFromLine(lines.get(
                    0).split(FFJDACSVConstants.FIELDS_SEPARATOR_PATTERN));
            boolean competitorDeleted = false;
            for (final Iterator<String> it = lines.iterator(); it.hasNext();) {
                try {
                    final Competitor extractedCompetitor = competitorLineExtractor.extractCompetitorFromLine(
                            it.next().split(FFJDACSVConstants.FIELDS_SEPARATOR_PATTERN), columnsIndexes);
                    for (final Competitor competitor : competitors) {
                        if (competitorAreEquals(competitor, extractedCompetitor)) {
                            it.remove();
                            competitorDeleted = true;
                        }
                    }
                } catch (final CSVCompetitorLineExtractionException ex) {
                    logger.info(ex.getMessage());
                }
            }
            if (competitorDeleted) {
                FileUtils.writeLines(competitorsFile, lines);
            }
        } catch (final Exception ex) {
            throw new CompetitorDeleteException(ex);
        }
    }

    private List readLines() throws IOException {
        return FileUtils.readLines(competitorsFile, OjtConstants.ENCODING);
    }

    @Override
    public CompetitorList retrieveCompetitors() throws CompetitorRetrieveException {
        final CompetitorList competitorList = new CompetitorList();
        final BufferedReader reader = null;
        try {
            final List<String> lines = readLines();
            final FFJDACSVColumnsIndexes columnsIndexes = columnIndexesExtractor.extractColumnIndexesFromLine(lines.get(
                    0).split(FFJDACSVConstants.FIELDS_SEPARATOR_PATTERN));
            for (int i = 1; i < lines.size(); i++) {
                try {
                    final Competitor comp = competitorLineExtractor.extractCompetitorFromLine(
                            lines.get(i).split(FFJDACSVConstants.FIELDS_SEPARATOR_PATTERN), columnsIndexes);
                    if (!retrieveOnlyCompetitorsWithWeight || comp.getWeight() != null) {
                        competitorList.add(comp);
                    }
                } catch (final CSVCompetitorLineExtractionException ex) {
                    logger.error("LINE SKIPPED ! Unable to extract competitor from line : " + lines.get(i),
                            ex);
                }
            }
        } catch (final Exception ex) {
            throw new CompetitorRetrieveException(ex);
        } finally {
            IOUtils.closeQuietly(reader);
        }

        return competitorList;
    }

    @Override
    public void updateCompetitor(final Competitor competitor) throws CompetitorUpdateException {
        try {
            final List<String> lines = readLines();
            final String[] splittedHeaderLine = lines.get(0).split(FFJDACSVConstants.FIELDS_SEPARATOR_PATTERN);
            final FFJDACSVColumnsIndexes columnsIndexes = columnIndexesExtractor.extractColumnIndexesFromLine(splittedHeaderLine);
            boolean competitorUpdated = false;
            for (int i = 0; i < lines.size() && !competitorUpdated; i++) {
                try {
                    final Competitor extractedCompetitor = competitorLineExtractor.extractCompetitorFromLine(
                            lines.get(i).split(FFJDACSVConstants.FIELDS_SEPARATOR_PATTERN), columnsIndexes);
                    if (competitorAreEquals(competitor, extractedCompetitor)) {
                        lines.remove(i);
                        lines.add(i, competitorLineConvertor.convertCompetitorInLine(competitor,
                                columnsIndexes, splittedHeaderLine.length));
                        competitorUpdated = true;
                    }
                } catch (final CSVCompetitorLineExtractionException ex) {
                    logger.info(ex.getMessage());
                }
            }
            if (competitorUpdated) {
                FileUtils.writeLines(competitorsFile, lines);
            }
        } catch (final Exception ex) {
            throw new CompetitorDeleteException(ex);
        }
    }

    private boolean competitorAreEquals(final Competitor competitor1, final Competitor competitor2) {
        return competitor1.getLicenseCode() != null
                && competitor1.getLicenseCode().equals(competitor2.getLicenseCode())
                || competitor1.getLicenseCode() == null && competitor1.equals(competitor2);
    }
}
