package com.ojt.dao.ffjdacsv;

import com.ojt.OJTException;

public class CSVCompetitorLineExtractionException extends OJTException {

    CSVCompetitorLineExtractionException(final String message) {
        super(message);
    }

    CSVCompetitorLineExtractionException(final Exception cause) {
        super(cause);
    }

    public CSVCompetitorLineExtractionException(final String message, final Exception cause) {
        super(message, cause);
    }
}
