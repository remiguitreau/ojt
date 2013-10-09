package com.ojt.dao.ffjdadat;

import org.apache.log4j.Logger;

import com.ojt.Competitor;
import com.ojt.dao.ffjdadat.FFJDADatConstants.FFJDADatas;

class CompetitorLineExtractor {

	private final Logger logger = Logger.getLogger(getClass());
	
	public Competitor extractCompetitorFromLine(final String[] datas, final FFJDADatColumnsIndexes columnIndexes) {
		checkEmptyDatas(datas);
		checkValidDatas(datas, columnIndexes);
		final Competitor competitor = new Competitor();
		for(final FFJDADatas data : FFJDADatas.values()) {
			data.completeCompetitorWithFFJDADatasLine(competitor, datas, columnIndexes);
		}
		competitor.setWeight(extractWeightFromField(datas[datas.length-1] ));
		return competitor;
	}

	private void checkValidDatas(final String[] datas, final FFJDADatColumnsIndexes columnIndexes) {
		if(columnIndexes.clubCodeIndex >= datas.length || columnIndexes.clubNameIndex >= datas.length || columnIndexes.licenseCodeIndex >= datas.length || columnIndexes.nameIndex >= datas.length || columnIndexes.firstNameIndex >= datas.length) {
			throw new CompetitorLineExtractionException("Competitor line has an invalid format");
		}
	}

	private Float extractWeightFromField(final String stringOfWeight) {
		try {
			return Float.valueOf(FFJDADatUtils.extractStringValue(stringOfWeight).replaceAll(",", "."));
		}
		catch(final Exception ex) {
			logger.debug("Impossible to extract weight from string : "+stringOfWeight);
		}
		return null;
	}

	private void checkEmptyDatas(final String[] datas) {
		if(datas == null || datas.length == 0) {
			throw new CompetitorLineExtractionException("Competitor line cannot be null or empty");
		}
	}

}
