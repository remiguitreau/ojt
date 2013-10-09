package com.ojt.dao.ffjdadat;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import com.ojt.Competitor;
import com.ojt.CompetitorList;
import com.ojt.dao.CompetitorCreationException;
import com.ojt.dao.CompetitorDeleteException;
import com.ojt.dao.CompetitorRetrieveException;
import com.ojt.dao.CompetitorUpdateException;
import com.ojt.dao.CompetitorsDao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Iterator;
import java.util.List;

public class FFJDADatCompetitorsDao implements CompetitorsDao {

	private final Logger logger = Logger.getLogger(FFJDADatCompetitorsDao.class);

	private final File competitorsFile;

	private final CompetitorLineExtractor competitorLineExtractor = new CompetitorLineExtractor();

	private final CompetitorLineConvertor competitorLineConvertor = new CompetitorLineConvertor();

	private final FFJDADatColumnIndexesExtractor columnIndexesExtractor = new FFJDADatColumnIndexesExtractor();

	private final boolean retrieveOnlyCompetitorsWithWeight;

	public FFJDADatCompetitorsDao(final File competitorsFile, final boolean retrieveOnlyCompetitorsWithWeight) {
		this.competitorsFile = competitorsFile;
		this.retrieveOnlyCompetitorsWithWeight = retrieveOnlyCompetitorsWithWeight;
	}

	@Override
	public void createCompetitor(final Competitor competitor) throws CompetitorCreationException {
		BufferedWriter writer = null;
		try {
			final List<String> lines = FileUtils.readLines(competitorsFile);
			final String[] splittedHeaderLine = lines.get(0).split(FFJDADatConstants.FIELDS_SEPARATOR_PATTERN);
			final FFJDADatColumnsIndexes columnsIndexes = columnIndexesExtractor.extractColumnIndexesFromLine(splittedHeaderLine);
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
			final List<String> lines = FileUtils.readLines(competitorsFile);
			final FFJDADatColumnsIndexes columnsIndexes = columnIndexesExtractor.extractColumnIndexesFromLine(lines.get(
					0).split(FFJDADatConstants.FIELDS_SEPARATOR_PATTERN));
			boolean competitorDeleted = false;
			for (final Iterator<String> it = lines.iterator(); it.hasNext() && !competitorDeleted;) {
				try {
					final Competitor extractedCompetitor = competitorLineExtractor.extractCompetitorFromLine(
							it.next().split(FFJDADatConstants.FIELDS_SEPARATOR_PATTERN), columnsIndexes);
					if (competitorAreEquals(competitor, extractedCompetitor)) {
						it.remove();
						competitorDeleted = true;
					}
				} catch (final CompetitorLineExtractionException ex) {
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
			final List<String> lines = FileUtils.readLines(competitorsFile);
			final FFJDADatColumnsIndexes columnsIndexes = columnIndexesExtractor.extractColumnIndexesFromLine(lines.get(
					0).split(FFJDADatConstants.FIELDS_SEPARATOR_PATTERN));
			boolean competitorDeleted = false;
			for (final Iterator<String> it = lines.iterator(); it.hasNext();) {
				try {
					final Competitor extractedCompetitor = competitorLineExtractor.extractCompetitorFromLine(
							it.next().split(FFJDADatConstants.FIELDS_SEPARATOR_PATTERN), columnsIndexes);
					for (final Competitor competitor : competitors) {
						if (competitorAreEquals(competitor, extractedCompetitor)) {
							it.remove();
							competitorDeleted = true;
						}
					}
				} catch (final CompetitorLineExtractionException ex) {
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
	public CompetitorList retrieveCompetitors() throws CompetitorRetrieveException {
		final CompetitorList competitorList = new CompetitorList();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(competitorsFile));
			String line = reader.readLine();
			final FFJDADatColumnsIndexes columnsIndexes = columnIndexesExtractor.extractColumnIndexesFromLine(line.split(FFJDADatConstants.FIELDS_SEPARATOR_PATTERN));
			while ((line = reader.readLine()) != null) {
				try {
					final Competitor comp = competitorLineExtractor.extractCompetitorFromLine(
							line.split(FFJDADatConstants.FIELDS_SEPARATOR_PATTERN), columnsIndexes);
					if (!retrieveOnlyCompetitorsWithWeight || (comp.getWeight() != null)) {
						competitorList.add(comp);
					}
				} catch (final CompetitorLineExtractionException ex) {
					logger.error("LINE SKIPPED ! Unable to extract competitor from line : " + line, ex);
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
			final List<String> lines = FileUtils.readLines(competitorsFile);
			final String[] splittedHeaderLine = lines.get(0).split(FFJDADatConstants.FIELDS_SEPARATOR_PATTERN);
			final FFJDADatColumnsIndexes columnsIndexes = columnIndexesExtractor.extractColumnIndexesFromLine(splittedHeaderLine);
			boolean competitorUpdated = false;
			for (int i = 0; (i < lines.size()) && !competitorUpdated; i++) {
				try {
					final Competitor extractedCompetitor = competitorLineExtractor.extractCompetitorFromLine(
							lines.get(i).split(FFJDADatConstants.FIELDS_SEPARATOR_PATTERN), columnsIndexes);
					if (competitorAreEquals(competitor, extractedCompetitor)) {
						lines.remove(i);
						lines.add(i, competitorLineConvertor.convertCompetitorInLine(competitor,
								columnsIndexes, splittedHeaderLine.length));
						competitorUpdated = true;
					}
				} catch (final CompetitorLineExtractionException ex) {
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
		return ((competitor1.getLicenseCode() != null) && competitor1.getLicenseCode().equals(
				competitor2.getLicenseCode()))
				|| ((competitor1.getLicenseCode() == null) && competitor1.equals(competitor2));
	}
}
