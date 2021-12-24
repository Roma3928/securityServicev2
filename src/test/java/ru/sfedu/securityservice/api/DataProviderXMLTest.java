package ru.sfedu.securityservice.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.sfedu.securityservice.models.SecurityPaper;
import ru.sfedu.securityservice.utils.DataGeneratorForCSVProvider;

class DataProviderXMLTest {

    public static DataProvider instance = new DataProviderXML();
    public static final Logger log = LogManager.getLogger(DataProviderCsvTest.class);

    @BeforeAll
    static void setCSVEnv() {
        DataGeneratorForCSVProvider.addRecord(instance);
    }

    @Test
    void addToFavoritesComplete() {
        SecurityPaper securityPaper = instance.getSecurityPaperById(1).get();
        Assertions.assertEquals(true, instance.addToFavorites(securityPaper, 1).getData());
    }

    @Test
    void addToFavoritesFail() {
        SecurityPaper securityPaper = instance.getSecurityPaperById(1).get();
        Assertions.assertEquals(false, instance.addToFavorites(securityPaper, 8).getData());
    }

    @Test
    void deleteFromFavoritesComplete() {
        SecurityPaper securityPaper = instance.getSecurityPaperById(1).get();
        Assertions.assertEquals(true, instance.deleteFromFavorites(securityPaper, 1).getData());
    }

    @Test
    void deleteFromFavoritesFail() {
        SecurityPaper securityPaper = instance.getSecurityPaperById(1).get();
        Assertions.assertEquals(false, instance.addToFavorites(securityPaper, 8).getData());
    }

    @Test
    void findSecurityByNameComplete() {
        Assertions.assertEquals(true, instance.securityController("Eugen", "ADDFAV", 1).getData());
    }

    @Test
    void findSecurityByNameFail() {
        Assertions.assertEquals(false, instance.securityController("Bob", "ADDFAV", 1).getData());
    }

    @Test
    void calculateAllPaperCostComplete() {
        Assertions.assertEquals(72900, instance.calculateAllPaperCost("").getData());
    }

    @Test
    void calculateAllPaperCostFail() {
        Assertions.assertNotEquals(0, instance.calculateAllPaperCost("").getData());
    }

    @Test
    void calculateBondComplete() {
        Assertions.assertEquals(24300, instance.calculateBond().getData());
    }

    @Test
    void calculateBondFail() {
        Assertions.assertNotEquals(0, instance.calculateBond().getData());
    }

    @Test
    void calculateStockComplete() {
        Assertions.assertEquals(24300, instance.calculateStock().getData());
    }

    @Test
    void calculateStockFail() {
        Assertions.assertNotEquals(0, instance.calculateStock().getData());
    }

    @Test
    void calculateETFComplete() {
        Assertions.assertEquals(24300, instance.calculateETF().getData());
    }

    @Test
    void calculateETFFail() {
        Assertions.assertNotEquals(0, instance.calculateETF().getData());
    }

}