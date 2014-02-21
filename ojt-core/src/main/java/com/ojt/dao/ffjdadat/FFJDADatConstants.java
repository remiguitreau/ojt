package com.ojt.dao.ffjdadat;

import com.ojt.Club;
import com.ojt.Competitor;
import com.ojt.CompetitorSex;
import com.ojt.GradeBelt;
import com.ojt.OJTUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public interface FFJDADatConstants {

    static final String FIELDS_SEPARATOR_PATTERN = ";";

    final static String LICENSE_CODE_IDENTIFIER = "\"Code Identifiant\"";

    final static String NAME_IDENTIFIER = "\"Nom\"";

    final static String FIRST_NAME_IDENTIFIER = "\"Prénom\"";

    final static String FIRST_NAME2_IDENTIFIER = "\"Prémon\"";

    final static String CLUB_CODE_IDENTIFIER = "\"Code Club\"";

    final static String CLUB_NAME_IDENTIFIER = "\"Nom_club_complet\"";

    final static String BIRTH_DATE_IDENTIFIER = "\"Date de naissance\"";

    final static String SEX_IDENTIFIER = "\"Sexe\"";

    final static String GRADE_IDENTIFIER = "\"Dan actuel\"";

    static final String DEPARTMENT = "\"Département\"";

    public final DateFormat BIRTH_DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");

    public enum FFJDADatas {
        LICENSE_CODE {
            @Override
            public void completeCompetitorWithFFJDADatasLine(final Competitor competitor,
                    final String[] datas, final FFJDADatColumnsIndexes columnIndexes) {
                competitor.setLicenseCode(FFJDADatUtils.extractStringValue(datas[columnIndexes.licenseCodeIndex]));
            }
        },
        NAME {
            @Override
            public void completeCompetitorWithFFJDADatasLine(final Competitor competitor,
                    final String[] datas, final FFJDADatColumnsIndexes columnIndexes) {
                competitor.setName(FFJDADatUtils.extractStringValue(datas[columnIndexes.nameIndex]));
            }
        },
        FIRST_NAME {
            @Override
            public void completeCompetitorWithFFJDADatasLine(final Competitor competitor,
                    final String[] datas, final FFJDADatColumnsIndexes columnIndexes) {
                competitor.setFirstName(FFJDADatUtils.extractStringValue(datas[columnIndexes.firstNameIndex]));
            }
        },
        CLUB {
            @Override
            public void completeCompetitorWithFFJDADatasLine(final Competitor competitor,
                    final String[] datas, final FFJDADatColumnsIndexes columnIndexes) {
                final String clubCode = FFJDADatUtils.extractStringValue(datas[columnIndexes.clubCodeIndex]);
                competitor.setClub(new Club(clubCode, OJTUtils.extractDepartmentFromClubId(clubCode),
                        FFJDADatUtils.extractStringValue(datas[columnIndexes.clubNameIndex])));
            }
        },
        BIRTH_DATE {
            @Override
            public void completeCompetitorWithFFJDADatasLine(final Competitor competitor,
                    final String[] datas, final FFJDADatColumnsIndexes columnIndexes) {
                try {
                    competitor.setBirthDate(BIRTH_DATE_FORMAT.parse(FFJDADatUtils.extractStringValue(datas[columnIndexes.birthDateIndex])));
                } catch (final Exception ex) {
                    competitor.setBirthDate(null);
                }
                OJTUtils.fillCompetitorWithCategory(competitor, new Date());
            }
        },
        SEX {
            @Override
            public void completeCompetitorWithFFJDADatasLine(final Competitor competitor,
                    final String[] datas, final FFJDADatColumnsIndexes columnIndexes) {
                competitor.setSex(CompetitorSex.retrieveSexFromString(FFJDADatUtils.extractStringValue(datas[columnIndexes.sexIndex])));
            }
        },
        GRADE {
            @Override
            public void completeCompetitorWithFFJDADatasLine(final Competitor competitor,
                    final String[] datas, final FFJDADatColumnsIndexes columnIndexes) {
                competitor.setGradeBelt(GradeBelt.retrieveGradeFromString(FFJDADatUtils.extractStringValue(datas[columnIndexes.gradeIndex])));
            }
        };

        public abstract void completeCompetitorWithFFJDADatasLine(final Competitor competitor,
                final String[] datas, final FFJDADatColumnsIndexes columnIndexes);
    }
}
