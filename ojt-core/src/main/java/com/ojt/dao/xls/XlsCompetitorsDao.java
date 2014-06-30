package com.ojt.dao.xls;

import org.apache.log4j.Logger;

import com.ojt.Club;
import com.ojt.Competitor;
import com.ojt.CompetitorCategory;
import com.ojt.CompetitorList;
import com.ojt.GradeBelt;
import com.ojt.OJTUtils;
import com.ojt.dao.CompetitorCreationException;
import com.ojt.dao.CompetitorDeleteException;
import com.ojt.dao.CompetitorRetrieveException;
import com.ojt.dao.CompetitorUpdateException;
import com.ojt.dao.CompetitorsDao;
import com.ojt.dao.ffjdadat.FFJDADatConstants;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

/**
 * Permet d'importer un fichier xls
 * @author cedric
 * @since 27/03/08 : cration
 */

public class XlsCompetitorsDao implements CompetitorsDao {

    // ------------------------------------------------
    // Attributs
    // ------------------------------------------------

    private File xlsFile;

    private final Logger logger = Logger.getLogger(getClass());

    private final boolean onlyWithWeight;

    // ------------------------------------------------
    // Constructeur
    // ------------------------------------------------

    /**
     * @param xlsFile
     */
    public XlsCompetitorsDao(final File xlsFile, final boolean onlyWithWeight) {
        super();
        this.xlsFile = xlsFile;
        this.onlyWithWeight = onlyWithWeight;
    }

    // ------------------------------------------------
    // Accesseurs
    // ------------------------------------------------

    public File getXlsFile() {
        return xlsFile;
    }

    public void setXlsFile(final File xlsFile) {
        this.xlsFile = xlsFile;
    }

    @Override
    public CompetitorList retrieveCompetitors() throws CompetitorRetrieveException {
        try {
            final CompetitorList competitors = new CompetitorList();
            final Workbook workbook = Workbook.getWorkbook(xlsFile);
            final Sheet sheet = workbook.getSheet(0);
            final Map<String, Integer> columnIndex = new HashMap<String, Integer>();
            resolvIndexs(sheet, columnIndex);
            checkIndex(columnIndex);
            for (int i = 2; i < sheet.getRows(); i++) { // on commence a 1 car
                // l'entete + code de colonne
                // des colonnes se trouve en 0
                final String name = sheet.getCell(columnIndex.get(COLUMN_ID_NAME), i).getContents();
                final String firstName = sheet.getCell(columnIndex.get(COLUMN_ID_SUBNAME), i).getContents();
                if (isValidName(name, firstName)) {
                    final CompetitorCategory category = CompetitorCategory.UNKNOWN;
                    final String clubId = sheet.getCell(columnIndex.get(COLUMN_ID_CLUB_ID), i).getContents();
                    final Club club = new Club(clubId, OJTUtils.extractDepartmentFromClubId(clubId),
                            sheet.getCell(columnIndex.get(COLUMN_ID_CLUB_NAME), i).getContents());
                    final String licenseCode = sheet.getCell(columnIndex.get(COLUMN_ID_LICENSE), i).getContents();
                    final Competitor competitor = new Competitor(licenseCode);
                    competitor.setName(name);
                    competitor.setFirstName(firstName);
                    competitor.setClub(club);
                    try {
                        competitor.setBirthDate(FFJDADatConstants.BIRTH_DATE_FORMAT.parse(sheet.getCell(
                                columnIndex.get(COLUMN_ID_BIRTH_DATE), i).getContents()));
                    } catch (final Exception ex) {
                        competitor.setBirthDate(null);
                    }
                    try {
                        competitor.setGradeBelt(GradeBelt.retrieveGradeFromString(sheet.getCell(
                                columnIndex.get(COLUMN_ID_GRADE), i).getContents()));
                    } catch (final Exception ex) {
                        competitor.setGradeBelt(null);
                    }
                    OJTUtils.fillCompetitorWithCategory(competitor, new Date());
                    try {
                        testValidWeight(sheet, i, columnIndex);
                        final Float weight = getWeight(sheet, i, columnIndex);
                        competitor.setWeight(weight);
                    } catch (final Exception ex) {
                        // On n'affecte pas de poids au compétiteur
                        competitor.setWeight(null);
                    }
                    if (onlyWithWeight) {
                        if (competitor.getWeight() != null && competitor.getWeightAsFloat() > 0) {
                            competitors.add(competitor);
                        }
                    } else {
                        competitors.add(competitor);
                    }
                }
            }
            return competitors;
        } catch (final Exception ex) {
            throw new CompetitorRetrieveException(ex);
        }
    }

    @Override
    public void updateCompetitor(final Competitor competitor) {
        WritableWorkbook workbook = null;
        try {
            workbook = Workbook.createWorkbook(xlsFile, Workbook.getWorkbook(xlsFile));
            final WritableSheet sheet = workbook.getSheet(0);
            final Map<String, Integer> columnIndex = new HashMap<String, Integer>();
            resolvIndexs(sheet, columnIndex);
            checkIndex(columnIndex);
            for (int i = 2; i < sheet.getRows(); i++) { // on commence a 1 car
                // l'entete + code de colonne
                // des colonnes se trouve en 0
                if (isCompetitorRow(competitor, sheet, i, columnIndex)) {
                    sheet.addCell(new Label(columnIndex.get(COLUMN_ID_WEIGHT), i,
                            competitor.getWeight() == null ? ""
                                    : String.valueOf(competitor.getWeight().floatValue()), sheet.getCell(
                                    columnIndex.get(COLUMN_ID_WEIGHT), i).getCellFormat()));
                    sheet.addCell(new Label(columnIndex.get(COLUMN_ID_GRADE), i,
                            competitor.getGradeBelt() == null ? "" : competitor.getGradeBelt().code,
                            new WritableCellFormat()));
                    logger.info("Compétiteur " + competitor + " mis à jour.");
                    workbook.write();
                    return;
                }
            }
            logger.info("Impossible de trouver " + competitor + " dans le fichier.");
        } catch (final Exception ex) {
            throw new CompetitorUpdateException(ex);
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (final Exception ex) {
                    logger.error("Impossible de clore le fichier", ex);
                }
            }
        }
    }

    @Override
    public void deleteCompetitors(final List<Competitor> competitors) throws CompetitorDeleteException {
        WritableWorkbook workbook = null;
        try {
            workbook = Workbook.createWorkbook(xlsFile, Workbook.getWorkbook(xlsFile));
            final WritableSheet sheet = workbook.getSheet(0);
            final Map<String, Integer> columnIndex = new HashMap<String, Integer>();
            resolvIndexs(sheet, columnIndex);
            checkIndex(columnIndex);
            for (final Competitor competitor : competitors) {
                for (int i = 2; i < sheet.getRows(); i++) { // on commence a 1
                    // car
                    // l'entete + code de colonne
                    // des colonnes se trouve en 0
                    if (isCompetitorRow(competitor, sheet, i, columnIndex)) {
                        sheet.removeRow(i);
                        logger.info("Compétiteur " + competitor + " supprimé.");
                        break;
                    }
                }
            }
            workbook.write();
        } catch (final Exception ex) {
            throw new CompetitorDeleteException(ex);
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (final Exception ex) {
                    logger.error("Impossible de clore le fichier.", ex);
                }
            }
        }
    }

    @Override
    public void deleteCompetitor(final Competitor competitor) throws CompetitorDeleteException {
        WritableWorkbook workbook = null;
        try {
            workbook = Workbook.createWorkbook(xlsFile, Workbook.getWorkbook(xlsFile));
            final WritableSheet sheet = workbook.getSheet(0);
            final Map<String, Integer> columnIndex = new HashMap<String, Integer>();
            resolvIndexs(sheet, columnIndex);
            checkIndex(columnIndex);
            for (int i = 2; i < sheet.getRows(); i++) { // on commence a 1 car
                // l'entete + code de colonne
                // des colonnes se trouve en 0
                if (isCompetitorRow(competitor, sheet, i, columnIndex)) {
                    sheet.removeRow(i);
                    logger.info("Compétiteur " + competitor + " supprimé.");
                    workbook.write();
                    return;
                }
            }
            logger.info("Impossible de trouver " + competitor + " dans le fichier.");
        } catch (final Exception ex) {
            throw new CompetitorDeleteException(ex);
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (final Exception ex) {
                    logger.error("Impossible de clore le fichier.", ex);
                }
            }
        }
    }

    @Override
    public void createCompetitor(final Competitor competitor) throws CompetitorCreationException {
        WritableWorkbook workbook = null;
        try {
            workbook = Workbook.createWorkbook(xlsFile, Workbook.getWorkbook(xlsFile));
            final WritableSheet sheet = workbook.getSheet(0);
            final Map<String, Integer> columnIndex = new HashMap<String, Integer>();
            resolvIndexs(sheet, columnIndex);
            checkIndex(columnIndex);
            final int newRowIdx = sheet.getRows();
            sheet.addCell(new Label(columnIndex.get(COLUMN_ID_NAME), newRowIdx, competitor.getName(),
                    new WritableCellFormat()));
            sheet.addCell(new Label(columnIndex.get(COLUMN_ID_SUBNAME), newRowIdx, competitor.getFirstName(),
                    new WritableCellFormat()));
            sheet.addCell(new Label(columnIndex.get(COLUMN_ID_GRADE), newRowIdx,
                    competitor.getGradeBelt() == null ? "" : competitor.getGradeBelt().code,
                    new WritableCellFormat()));
            sheet.addCell(new Label(columnIndex.get(COLUMN_ID_LICENSE), newRowIdx,
                    competitor.getLicenseCode() == null ? "" : competitor.getLicenseCode(),
                    new WritableCellFormat()));
            logger.info("competitor: " + competitor + " sheet = " + sheet + " ; columnIndex=" + columnIndex
                    + " ; birthdate = " + competitor.getBirthDate());
            sheet.addCell(new Label(columnIndex.get(COLUMN_ID_BIRTH_DATE), newRowIdx,
                    competitor.getBirthDate() == null ? ""
                            : FFJDADatConstants.BIRTH_DATE_FORMAT.format(competitor.getBirthDate()),
                    new WritableCellFormat()));
            if (competitor.getClub() != null) {
                sheet.addCell(new Label(columnIndex.get(COLUMN_ID_CLUB_ID), newRowIdx,
                        competitor.getClub().getClubCode() == null ? "" : competitor.getClub().getClubCode(),
                        new WritableCellFormat()));
                sheet.addCell(new Label(columnIndex.get(COLUMN_ID_CLUB_NAME), newRowIdx,
                        competitor.getClub().getClubName() == null ? "" : competitor.getClub().getClubName(),
                        new WritableCellFormat()));
            }
            sheet.addCell(new Label(
                    columnIndex.get(COLUMN_ID_WEIGHT),
                    newRowIdx,
                    competitor.getWeight() == null ? "" : String.valueOf(competitor.getWeight().floatValue()),
                    new WritableCellFormat()));
            workbook.write();
            logger.info("Competiteur " + competitor + " ajouté");
        } catch (final Exception ex) {
            throw new CompetitorCreationException(ex);
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (final Exception ex) {
                    logger.error("Impossible de clore le fichier.", ex);
                }
            }
        }
    }

    // -------------------------------------------------------------------------
    // Methode prive
    // -------------------------------------------------------------------------

    private boolean isCompetitorRow(final Competitor competitor, final Sheet sheet, final int rowIndex,
            final Map<String, Integer> columnIndex) {
        if (competitor.getLicenseCode() != null && !competitor.getLicenseCode().trim().isEmpty()) {
            return competitor.getLicenseCode().equalsIgnoreCase(
                    sheet.getCell(columnIndex.get(COLUMN_ID_LICENSE), rowIndex).getContents());
        }
        return competitor.getName().equalsIgnoreCase(
                sheet.getCell(columnIndex.get(COLUMN_ID_NAME), rowIndex).getContents())
                && competitor.getFirstName().equalsIgnoreCase(
                        sheet.getCell(columnIndex.get(COLUMN_ID_SUBNAME), rowIndex).getContents());
    }

    private void checkIndex(final Map<String, Integer> columnIndex) {
        final StringBuffer missingColumnIds = new StringBuffer(50);
        if (!columnIndex.containsKey(COLUMN_ID_NAME)) {
            missingColumnIds.append("'");
            missingColumnIds.append(COLUMN_ID_NAME);
            missingColumnIds.append("', ");
        }
        if (!columnIndex.containsKey(COLUMN_ID_SUBNAME)) {
            missingColumnIds.append("'");
            missingColumnIds.append(COLUMN_ID_SUBNAME);
            missingColumnIds.append("', ");
        }
        if (!columnIndex.containsKey(COLUMN_ID_CLUB_ID)) {
            missingColumnIds.append("'");
            missingColumnIds.append(COLUMN_ID_CLUB_ID);
            missingColumnIds.append("', ");
        }
        if (!columnIndex.containsKey(COLUMN_ID_CLUB_NAME)) {
            missingColumnIds.append("'");
            missingColumnIds.append(COLUMN_ID_CLUB_NAME);
            missingColumnIds.append("', ");
        }
        if (!columnIndex.containsKey(COLUMN_ID_LICENSE)) {
            missingColumnIds.append("'");
            missingColumnIds.append(COLUMN_ID_LICENSE);
            missingColumnIds.append("', ");
        }
        if (!columnIndex.containsKey(COLUMN_ID_GRADE)) {
            missingColumnIds.append("'");
            missingColumnIds.append(COLUMN_ID_GRADE);
            missingColumnIds.append("', ");
        }
        if (!columnIndex.containsKey(COLUMN_ID_BIRTH_DATE)) {
            missingColumnIds.append("'");
            missingColumnIds.append(COLUMN_ID_BIRTH_DATE);
            missingColumnIds.append("', ");
        }
        if (missingColumnIds.length() > 0) {
            throw new XlsBadFormatException("Colonnes manquantes : " + missingColumnIds.toString());
        }

    }

    private void resolvIndexs(final Sheet sheet, final Map<String, Integer> columnIndex) {
        for (int i = 0; i < sheet.getColumns(); i++) {
            final String val = sheet.getCell(i, 0).getContents();
            if (COLUMN_ID_NAME.equals(val)) {
                columnIndex.put(COLUMN_ID_NAME, i);
            } else if (COLUMN_ID_SUBNAME.equals(val)) {
                columnIndex.put(COLUMN_ID_SUBNAME, i);
            } else if (COLUMN_ID_CLUB_ID.equals(val)) {
                columnIndex.put(COLUMN_ID_CLUB_ID, i);
            } else if (COLUMN_ID_CLUB_NAME.equals(val)) {
                columnIndex.put(COLUMN_ID_CLUB_NAME, i);
            } else if (COLUMN_ID_WEIGHT.equals(val)) {
                columnIndex.put(COLUMN_ID_WEIGHT, i);
            } else if (COLUMN_ID_LICENSE.equals(val)) {
                columnIndex.put(COLUMN_ID_LICENSE, i);
            } else if (COLUMN_ID_BIRTH_DATE.equals(val)) {
                columnIndex.put(COLUMN_ID_BIRTH_DATE, i);
            } else if (COLUMN_ID_GRADE.equals(val)) {
                columnIndex.put(COLUMN_ID_GRADE, i);
            }
        }

    }

    /**
     * @param sheet
     * @param i
     * @return Le poids du compétiteur, null si aucun poids n'a été saisie.
     */
    private Float getWeight(final Sheet sheet, final int i, final Map<String, Integer> columnIndex) {
        float weight = -1;
        try {
            String w = sheet.getCell(columnIndex.get(COLUMN_ID_WEIGHT), i).getContents();
            w = w.replaceAll(",", ".");
            if (w == null || w.trim().length() == 0) {
                return null;
            }
            weight = Float.valueOf(w);
        } catch (final NumberFormatException ex) {
            logger.error("Bad weight format", ex);
        }
        return weight;
    }

    private boolean isValidName(final String name, final String firstName) {
        if (name == null || firstName == null) {
            return false;
        }
        if (name.trim().equals("") && firstName.trim().equals("")) {
            return false;
        }
        return true;
    }

    private void testValidWeight(final Sheet sheet, final int i, final Map<String, Integer> columnIndex)
            throws IllegalStateException, IllegalArgumentException {
        float weight = -1;
        String w = sheet.getCell(columnIndex.get(COLUMN_ID_WEIGHT), i).getContents();
        w = w.replaceAll(",", ".");
        if (w == null || w.trim().length() == 0) {
            return;
        }
        try {
            weight = Float.valueOf(w);
            if (weight < 0) {
                throw new IllegalArgumentException("value < 0 :" + w);
            }
        } catch (final NumberFormatException e) {
            throw new IllegalArgumentException("value is not a number : " + w);
        }
    }

    public static void main(final String[] args) {
        final Competitor competitor = new Competitor();
        competitor.setName("ANDRE");
        competitor.setFirstName("etienne");
        competitor.setWeight(Float.valueOf(23));
        competitor.setLicenseCode("COUCOU23");
        competitor.setClub(new Club("SEPA05400", "05", "Best cliub"));
        new XlsCompetitorsDao(new File("D:\\ref2.xls"), false).createCompetitor(competitor);
    }
}
