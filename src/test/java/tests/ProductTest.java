package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.LoginPage;
import pages.ProductDetailPage;
import utils.ConfigReader;

/**
 * ProductTest - Covers product detail page scenarios
 *
 * Test Cases:
 *  TC_PROD_001 - Verify product detail page opens correctly
 *  TC_PROD_002 - Verify product name on detail page matches listing
 *  TC_PROD_003 - Verify product price on detail page is positive
 *  TC_PROD_004 - Verify product description is not empty
 *  TC_PROD_005 - Verify Add to Cart button on product detail page
 *  TC_PROD_006 - Verify Remove button appears after adding to cart
 *  TC_PROD_007 - Verify Back to Products navigation
 */
public class ProductTest extends BaseTest {

    private static final String PRODUCT_NAME = "Sauce Labs Backpack";

    private HomePage loginAndGetHomePage() {
        return new LoginPage(getDriver()).login(
            ConfigReader.getStandardUser(),
            ConfigReader.getPassword()
        );
    }

    // -------------------------------------------------------
    // TC_PROD_001
    // -------------------------------------------------------
    @Test(groups = {"smoke", "regression"},
          description = "TC_PROD_001 - Verify product detail page opens correctly")
    public void testProductDetailPageOpens() {
        log.info("TC_PROD_001: Product detail page opens");
        HomePage homePage = loginAndGetHomePage();

        ProductDetailPage detailPage = homePage.clickOnProductByName(PRODUCT_NAME);

        Assert.assertTrue(getDriver().getCurrentUrl().contains("inventory-item"),
            "URL should contain 'inventory-item' for product detail page");

        log.info("TC_PROD_001 PASSED");
    }

    // -------------------------------------------------------
    // TC_PROD_002
    // -------------------------------------------------------
    @Test(groups = {"regression"},
          description = "TC_PROD_002 - Verify product name matches between listing and detail page")
    public void testProductNameMatchesOnDetailPage() {
        log.info("TC_PROD_002: Product name match test");
        HomePage homePage = loginAndGetHomePage();

        ProductDetailPage detailPage = homePage.clickOnProductByName(PRODUCT_NAME);

        String detailName = detailPage.getProductName();
        Assert.assertEquals(detailName, PRODUCT_NAME,
            "Product name on detail page should match listing page name");

        log.info("TC_PROD_002 PASSED: Product name matches - '{}'", detailName);
    }

    // -------------------------------------------------------
    // TC_PROD_003
    // -------------------------------------------------------
    @Test(groups = {"regression"},
          description = "TC_PROD_003 - Verify product price is positive on detail page")
    public void testProductPriceIsPositive() {
        log.info("TC_PROD_003: Product price positive check");
        HomePage homePage = loginAndGetHomePage();

        ProductDetailPage detailPage = homePage.clickOnProductByName(PRODUCT_NAME);

        double price = detailPage.getProductPrice();
        Assert.assertTrue(price > 0, "Product price should be positive, found: " + price);

        log.info("TC_PROD_003 PASSED: Product price = ${}", price);
    }

    // -------------------------------------------------------
    // TC_PROD_004
    // -------------------------------------------------------
    @Test(groups = {"regression"},
          description = "TC_PROD_004 - Verify product description is not empty on detail page")
    public void testProductDescriptionNotEmpty() {
        log.info("TC_PROD_004: Product description not empty");
        HomePage homePage = loginAndGetHomePage();

        ProductDetailPage detailPage = homePage.clickOnProductByName(PRODUCT_NAME);

        String description = detailPage.getProductDescription();
        Assert.assertFalse(description.isEmpty(),
            "Product description should not be empty");

        log.info("TC_PROD_004 PASSED: Description = '{}'", description);
    }

    // -------------------------------------------------------
    // TC_PROD_005
    // -------------------------------------------------------
    @Test(groups = {"smoke", "regression"},
          description = "TC_PROD_005 - Verify Add to Cart button is present on detail page")
    public void testAddToCartButtonDisplayed() {
        log.info("TC_PROD_005: Add to Cart button displayed");
        HomePage homePage = loginAndGetHomePage();

        ProductDetailPage detailPage = homePage.clickOnProductByName(PRODUCT_NAME);

        Assert.assertTrue(detailPage.isAddToCartButtonDisplayed(),
            "'Add to cart' button should be visible on product detail page");

        log.info("TC_PROD_005 PASSED");
    }

    // -------------------------------------------------------
    // TC_PROD_006
    // -------------------------------------------------------
    @Test(groups = {"regression"},
          description = "TC_PROD_006 - Verify Remove button appears after adding to cart from detail page")
    public void testRemoveButtonAppearsAfterAddToCart() {
        log.info("TC_PROD_006: Remove button appears after add to cart");
        HomePage homePage = loginAndGetHomePage();

        ProductDetailPage detailPage = homePage.clickOnProductByName(PRODUCT_NAME);

        Assert.assertTrue(detailPage.isAddToCartButtonDisplayed(),
            "Add to Cart should be visible before adding");

        detailPage.addToCart();

        Assert.assertTrue(detailPage.isRemoveButtonDisplayed(),
            "Remove button should appear after adding to cart");
        Assert.assertFalse(detailPage.isAddToCartButtonDisplayed(),
            "Add to Cart should disappear after adding");

        log.info("TC_PROD_006 PASSED");
    }

    // -------------------------------------------------------
    // TC_PROD_007
    // -------------------------------------------------------
    @Test(groups = {"regression"},
          description = "TC_PROD_007 - Verify Back to Products button navigates back to home")
    public void testBackToProductsNavigation() {
        log.info("TC_PROD_007: Back to products navigation");
        HomePage homePage = loginAndGetHomePage();

        ProductDetailPage detailPage = homePage.clickOnProductByName(PRODUCT_NAME);
        HomePage returnedHomePage = detailPage.goBackToProducts();

        Assert.assertTrue(returnedHomePage.isHomePageDisplayed(),
            "Should return to Products listing page");

        log.info("TC_PROD_007 PASSED");
    }
}
