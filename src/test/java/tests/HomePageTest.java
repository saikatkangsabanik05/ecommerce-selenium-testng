package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.LoginPage;
import utils.ConfigReader;

import java.util.List;

/**
 * HomePageTest - Covers product listing and homepage scenarios
 *
 * Test Cases:
 *  TC_HOME_001 - Verify products are displayed after login
 *  TC_HOME_002 - Verify product count (SauceDemo has 6 products)
 *  TC_HOME_003 - Verify all product names are non-empty
 *  TC_HOME_004 - Verify all product prices are positive
 *  TC_HOME_005 - Verify cart count starts at zero
 *  TC_HOME_006 - Verify cart count increments when adding product
 */
public class HomePageTest extends BaseTest {

    private HomePage loginAndGetHomePage() {
        return new LoginPage(getDriver()).login(
            ConfigReader.getStandardUser(),
            ConfigReader.getPassword()
        );
    }

    // -------------------------------------------------------
    // TC_HOME_001
    // -------------------------------------------------------
    @Test(groups = {"smoke", "regression"},
          description = "TC_HOME_001 - Verify Products page is displayed after login")
    public void testHomePageDisplayedAfterLogin() {
        log.info("TC_HOME_001: Home page display after login");
        HomePage homePage = loginAndGetHomePage();

        Assert.assertTrue(homePage.isHomePageDisplayed(),
            "Home page with 'Products' title should be displayed");

        log.info("TC_HOME_001 PASSED");
    }

    // -------------------------------------------------------
    // TC_HOME_002
    // -------------------------------------------------------
    @Test(groups = {"smoke", "regression"},
          description = "TC_HOME_002 - Verify 6 products are displayed on the home page")
    public void testProductCount() {
        log.info("TC_HOME_002: Product count verification");
        HomePage homePage = loginAndGetHomePage();

        int productCount = homePage.getProductCount();
        log.info("Product count found: {}", productCount);

        Assert.assertEquals(productCount, 6,
            "Expected 6 products on the home page");

        log.info("TC_HOME_002 PASSED");
    }

    // -------------------------------------------------------
    // TC_HOME_003
    // -------------------------------------------------------
    @Test(groups = {"regression"},
          description = "TC_HOME_003 - Verify all product names are not empty")
    public void testAllProductNamesNonEmpty() {
        log.info("TC_HOME_003: Product names non-empty check");
        HomePage homePage = loginAndGetHomePage();

        List<String> names = homePage.getAllProductNames();
        Assert.assertFalse(names.isEmpty(), "Product name list should not be empty");

        for (String name : names) {
            Assert.assertFalse(name.trim().isEmpty(),
                "Product name should not be blank, found: '" + name + "'");
        }

        log.info("TC_HOME_003 PASSED: All {} product names are non-empty", names.size());
    }

    // -------------------------------------------------------
    // TC_HOME_004
    // -------------------------------------------------------
    @Test(groups = {"regression"},
          description = "TC_HOME_004 - Verify all product prices are positive")
    public void testAllProductPricesPositive() {
        log.info("TC_HOME_004: Product prices positive check");
        HomePage homePage = loginAndGetHomePage();

        List<Double> prices = homePage.getAllProductPrices();
        Assert.assertFalse(prices.isEmpty(), "Price list should not be empty");

        for (Double price : prices) {
            Assert.assertTrue(price > 0,
                "Product price should be positive, found: " + price);
        }

        log.info("TC_HOME_004 PASSED: All {} prices are positive", prices.size());
    }

    // -------------------------------------------------------
    // TC_HOME_005
    // -------------------------------------------------------
    @Test(groups = {"smoke", "regression"},
          description = "TC_HOME_005 - Verify cart count is 0 before adding any product")
    public void testCartStartsEmpty() {
        log.info("TC_HOME_005: Cart starts empty");
        HomePage homePage = loginAndGetHomePage();

        int cartCount = homePage.getCartItemCount();
        Assert.assertEquals(cartCount, 0,
            "Cart badge count should be 0 before adding any product");

        log.info("TC_HOME_005 PASSED");
    }

    // -------------------------------------------------------
    // TC_HOME_006
    // -------------------------------------------------------
    @Test(groups = {"smoke", "regression"},
          description = "TC_HOME_006 - Verify cart count increments when adding a product")
    public void testCartCountIncrementsOnAddToCart() {
        log.info("TC_HOME_006: Cart count increment test");
        HomePage homePage = loginAndGetHomePage();

        Assert.assertEquals(homePage.getCartItemCount(), 0, "Cart should start at 0");

        homePage.addProductToCartByIndex(0);
        Assert.assertEquals(homePage.getCartItemCount(), 1, "Cart count should be 1 after adding 1 product");

        homePage.addProductToCartByIndex(1);
        Assert.assertEquals(homePage.getCartItemCount(), 2, "Cart count should be 2 after adding 2 products");

        log.info("TC_HOME_006 PASSED");
    }
}
