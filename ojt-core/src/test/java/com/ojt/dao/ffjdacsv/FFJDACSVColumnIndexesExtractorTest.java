package com.ojt.dao.ffjdacsv;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class FFJDACSVColumnIndexesExtractorTest {
    private FFJDACSVColumnIndexesExtractor extractor;

    @Before
    public void initTest() {
        extractor = new FFJDACSVColumnIndexesExtractor();
    }

    @Test(expected = FFJDACSVBadFormatException.class)
    public void testBadFormat() {
        extractor.extractColumnIndexesFromLine(new String[12]);
    }

    @Test
    public void testFFJDACSVColumnIndexesExtraction() {
        final FFJDACSVColumnsIndexes indexes = extractor.extractColumnIndexesFromLine("Licence;Date inscription;Statut;Nom prémon;Sexe;Date de naissance;Club;Comité;Grade actuel;Date grade actuel;Catégorie;Mail perso;Mail club;Mail inscription;Inscrit par;Adresse1;Adresse2;CP;Ville".split(FFJDACSVConstants.FIELDS_SEPARATOR_PATTERN));
        Assert.assertEquals(0, indexes.licenseCodeIndex);
        Assert.assertEquals(3, indexes.fullNameIndex);
        Assert.assertEquals(4, indexes.sexIndex);
        Assert.assertEquals(5, indexes.birthDateIndex);
        Assert.assertEquals(6, indexes.clubNameIndex);
        Assert.assertEquals(7, indexes.clubDepartmentIndex);
        Assert.assertEquals(8, indexes.gradeIndex);
        Assert.assertEquals(10, indexes.weightIndex);
    }

}
