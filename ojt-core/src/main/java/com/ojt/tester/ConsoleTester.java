package com.ojt.tester;

import org.apache.log4j.Logger;

import com.ojt.CompetitorGroup;
import com.ojt.CompetitorList;
import com.ojt.algo.Algorithme;
import com.ojt.dao.CompetitorsDao;
import com.ojt.dao.CompetitorsDaoFactory;

import java.io.File;
import java.util.List;

public class ConsoleTester {

	public static void main(final String[] args) {
		final Logger logger = Logger.getLogger(ConsoleTester.class);
		if ((args == null) || (args.length < 1)) {
			logger.info("Must spécify a input file!");
			logger.info("Usage : ");
			logger.info("  testConsole input_file");
			return;
		}

		final String inputFile = args[0];
		try {
			final CompetitorsDao dao = CompetitorsDaoFactory.createCompetitorsDao(new File(inputFile), false);
			final CompetitorList comps = dao.retrieveCompetitors();
			final Algorithme algo = new Algorithme();

			final List<CompetitorGroup> groups = comps.createGroups(algo);

			logger.info("Liste des groupes");
			for (final CompetitorGroup group : groups) {
				logger.info(group);
				logger.info("---------------------------");
			}

		} catch (final Exception e) {
			logger.error("", e);
		}
	}
}
