package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.LoginPage;
import utils.ConfigReader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * SortingTest - Covers product sorting functionality
 *
 * Test Cases:
 *  TC_SORT_001 - Sort products A to Z
 *  TC_SORT_002 - Sort products Z to A
 *  TC_SORT_003 - Sort products by Price Low to High
 *  TC_SORT_004 - Sort products by Price High to Low
 */
public class SortingTest extends BaseTest {

    private HomePage loginAndGetHomePage() {
        return new LoginPage(getDriver()).login(
            ConfigReader.getStandardUser(),
            ConfigReader.getPassword()
        );
    }

    // -------------------------------------------------------
    // TC_SORT_001
    // -------------------------------------------------------
    @Test(groups = {"regression"},
          description = "TC_SORT_001 - Verify products are sorted A to Z correctly")
    public void testSortProductsAtoZ() {
        log.info("TC_SORT_001: Sort A to Z");
        HomePage homePage = loginAndGetHomePage();

        homePage.sortBy("Name (A to Z)");

        List<String> actualNames = homePage.getAllProductNames();
        List<String> sortedNames = new ArrayList<>(actualNames);
        Collections.sort(sortedNames);

        Assert.assertEquals(actualNames, sortedNames,
            "Products should be sorted A to Z");

        log.info("TC_SORT_001 PASSED: Products sorted A-Z: {}", actualNames);
    }

    // -------------------------------------------------------
    // TC_SORT_002
    // -------------------------------------------------------
    @Test(groups = {"regression"},
          description = "TC_SORT_002 - Verify products are sorted Z to A correctly")
    public void testSortProductsZtoA() {
        log.info("TC_SORT_002: Sort Z to A");
        HomePage homePage = loginAndGetHomePage();

        homePage.sortBy("Name (Z to A)");

        List<String> actualNames = homePage.getAllProductNames();
        List<String> sortedNames = new ArrayList<>(actualNames);
        sortedNames.sort(Collections.reverseOrder());

        Assert.assertEquals(actualNames, sortedNames,
            "Products should be sorted Z to A");

        log.info("TC_SORT_002 PASSED: Products sorted Z-A: {}", actualNames);
    }

    // -------------------------------------------------------
    // TC_SORT_003
    // -------------------------------------------------------
    @Test(groups = {"regression"},
          description = "TC_SORT_003 - Verify products sorted by Price Low to High")
    public void testSortByPriceLowToHigh() {
        log.info("TC_SORT_003: Sort Price Low to High");
        HomePage homePage = loginAndGetHomePage();

        homePage.sortBy("Price (low to high)");

        List<Double> actualPrices = homePage.getAllProductPrices();
        List<Double> sortedPrices = new ArrayList<>(actualPrices);
        Collections.sort(sortedPrices);

        Assert.assertEquals(actualPrices, sortedPrices,
            "Products should be sorted by price low to high");

        log.info("TC_SORT_003 PASSED: Prices low-high: {}", actualPrices);
    }

    // -------------------------------------------------------
    // TC_SORT_004
    // -------------------------------------------------------
    @Test(groups = {"regression"},
          description = "TC_SORT_004 - Verify products sorted by Price High to Low")
    public void testSortByPriceHighToLow() {
        log.info("TC_SORT_004: Sort Price High to Low");
        HomePage homePage = loginAndGetHomePage();

        homePage.sortBy("Price (high to low)");

        List<Double> actualPrices = homePage.getAllProductPrices();
        List<Double> sortedPrices = new ArrayList<>(actualPrices);
        sortedPrices.sort(Collections.reverseOrder());

        Assert.assertEquals(actualPrices, sortedPrices,
            "Products should be sorted by price high to low");

        log.info("TC_SORT_004 PASSED: Prices high-low: {}", actualPrices);
    }
}
