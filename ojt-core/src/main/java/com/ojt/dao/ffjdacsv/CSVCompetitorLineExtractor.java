package com.ojt.dao.ffjdacsv;

import org.apache.log4j.Logger;

import com.ojt.Competitor;
import com.ojt.dao.ffjdacsv.FFJDACSVConstants.FFJDACSVDatas;

class CSVCompetitorLineExtractor {

    private final Logger logger = Logger.getLogger(getClass());

    public Competitor extractCompetitorFromLine(final String[] datas,
            final FFJDACSVColumnsIndexes columnIndexes) {
        checkEmptyDatas(datas);
        checkValidDatas(datas, columnIndexes);
        final Competitor competitor = new Competitor();
        for (final FFJDACSVDatas data : FFJDACSVDatas.values()) {
            data.completeCompetitorWithFFJDADatasLine(competitor, datas, columnIndexes);
        }
        return competitor;
    }

    private void checkValidDatas(final String[] datas, final FFJDACSVColumnsIndexes columnIndexes) {
        if (columnIndexes.clubDepartmentIndex >= datas.length || columnIndexes.clubNameIndex >= datas.length
                || columnIndexes.licenseCodeIndex >= datas.length
                || columnIndexes.weightIndex >= datas.length || columnIndexes.fullNameIndex >= datas.length) {
            throw new CSVCompetitorLineExtractionException("Competitor line has an invalid format");
        }
    }

    private void checkEmptyDatas(final String[] datas) {
        if (datas == null || datas.length == 0) {
            throw new CSVCompetitorLineExtractionException("Competitor line cannot be null or empty");
        }
    }

}
