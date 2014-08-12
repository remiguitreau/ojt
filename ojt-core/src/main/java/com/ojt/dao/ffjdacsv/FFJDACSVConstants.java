package com.ojt.dao.ffjdacsv;

import com.ojt.Club;
import com.ojt.Competitor;
import com.ojt.CompetitorSex;
import com.ojt.GradeBelt;
import com.ojt.OJTUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public interface FFJDACSVConstants {

    final static Map<String, String> clubs = new HashMap<String, String>();

    static final String FIELDS_SEPARATOR_PATTERN = ";";

    final static String LICENSE_CODE_IDENTIFIER = "Licence";

    final static String FULL_NAME_IDENTIFIER = "Nom prémon";

    final static String CLUB_DEPARTMENT_IDENTIFIER = "Comité";

    final static String CLUB_NAME_IDENTIFIER = "Club";

    final static String BIRTH_DATE_IDENTIFIER = "Date de naissance";

    final static String SEX_IDENTIFIER = "Sexe";

    final static String GRADE_IDENTIFIER = "Grade actuel";

    static final String WEIGHT_IDENTIFIER = "Catégorie";

    public final DateFormat BIRTH_DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");

    public enum FFJDACSVDatas {
        LICENSE_CODE {
            @Override
            public void completeCompetitorWithFFJDADatasLine(final Competitor competitor,
                    final String[] datas, final FFJDACSVColumnsIndexes columnIndexes) {
                competitor.setLicenseCode(FFJDACSVUtils.extractStringValue(datas[columnIndexes.licenseCodeIndex]));
            }
        },
        NAME {
            @Override
            public void completeCompetitorWithFFJDADatasLine(final Competitor competitor,
                    final String[] datas, final FFJDACSVColumnsIndexes columnIndexes) {
                competitor.setName(FFJDACSVUtils.extractStringValue(datas[columnIndexes.fullNameIndex]).split(
                        " ")[0].trim());
            }
        },
        FIRST_NAME {
            @Override
            public void completeCompetitorWithFFJDADatasLine(final Competitor competitor,
                    final String[] datas, final FFJDACSVColumnsIndexes columnIndexes) {
                final String value = FFJDACSVUtils.extractStringValue(datas[columnIndexes.fullNameIndex]);
                competitor.setFirstName(value.substring(value.indexOf(" ") + 1).trim());
            }
        },
        CLUB {
            @Override
            public void completeCompetitorWithFFJDADatasLine(final Competitor competitor,
                    final String[] datas, final FFJDACSVColumnsIndexes columnIndexes) {
                String clubDepartmentCode = FFJDACSVUtils.extractStringValue(datas[columnIndexes.clubDepartmentIndex]);
                if (clubDepartmentCode.length() == 1) {
                    clubDepartmentCode = "0" + clubDepartmentCode;
                }
                final String clubName = FFJDACSVUtils.extractStringValue(datas[columnIndexes.clubNameIndex]);
                String clubCode = clubs.get(clubDepartmentCode + clubName);
                if (clubCode == null || clubCode.isEmpty()) {
                    clubCode = OJTUtils.buildClubCodeFromDepartment(clubDepartmentCode, clubs.size());
                    clubs.put(clubDepartmentCode + clubName, clubCode);
                }
                competitor.setClub(new Club(clubCode, clubDepartmentCode, clubName));
            }
        },
        BIRTH_DATE {
            @Override
            public void completeCompetitorWithFFJDADatasLine(final Competitor competitor,
                    final String[] datas, final FFJDACSVColumnsIndexes columnIndexes) {
                try {
                    competitor.setBirthDate(BIRTH_DATE_FORMAT.parse(FFJDACSVUtils.extractStringValue(datas[columnIndexes.birthDateIndex])));
                } catch (final Exception ex) {
                    competitor.setBirthDate(null);
                }
                OJTUtils.fillCompetitorWithCategory(competitor, new Date());
            }
        },
        SEX {
            @Override
            public void completeCompetitorWithFFJDADatasLine(final Competitor competitor,
                    final String[] datas, final FFJDACSVColumnsIndexes columnIndexes) {
                competitor.setSex(CompetitorSex.retrieveSexFromString(FFJDACSVUtils.extractStringValue(datas[columnIndexes.sexIndex])));
            }
        },
        GRADE {
            @Override
            public void completeCompetitorWithFFJDADatasLine(final Competitor competitor,
                    final String[] datas, final FFJDACSVColumnsIndexes columnIndexes) {
                competitor.setGradeBelt(GradeBelt.retrieveGradeFromString(FFJDACSVUtils.extractStringValue(datas[columnIndexes.gradeIndex])));
            }
        },
        WEIGHT {
            @Override
            public void completeCompetitorWithFFJDADatasLine(final Competitor competitor,
                    final String[] datas, final FFJDACSVColumnsIndexes columnIndexes) {
                final String weightAsString = datas[columnIndexes.weightIndex].replaceAll(",", ".").trim();
                if (weightAsString.isEmpty() || weightAsString.equals("+0")) {
                    competitor.setWeight(null);
                } else {
                    competitor.setWeight(Float.parseFloat(FFJDACSVUtils.extractStringValue(weightAsString)));
                }
            }
        };

        public abstract void completeCompetitorWithFFJDADatasLine(final Competitor competitor,
                final String[] datas, final FFJDACSVColumnsIndexes columnIndexes);
    }
}
