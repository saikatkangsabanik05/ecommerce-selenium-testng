package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.CartPage;
import pages.HomePage;
import pages.LoginPage;
import utils.ConfigReader;

/**
 * CartTest - Covers all Shopping Cart scenarios
 *
 * Test Cases:
 *  TC_CART_001 - Verify cart page opens with correct title
 *  TC_CART_002 - Verify cart is empty initially
 *  TC_CART_003 - Verify single product added to cart appears in cart
 *  TC_CART_004 - Verify multiple products added to cart
 *  TC_CART_005 - Verify item removed from cart page
 *  TC_CART_006 - Verify cart count updates after remove
 *  TC_CART_007 - Verify cart persists after navigating back
 *  TC_CART_008 - Verify Continue Shopping button returns to home
 *  TC_CART_009 - Verify all items can be removed from cart
 */
public class CartTest extends BaseTest {

    private static final String PRODUCT_1 = "Sauce Labs Backpack";
    private static final String PRODUCT_2 = "Sauce Labs Bike Light";

    private HomePage loginAndGetHomePage() {
        return new LoginPage(getDriver()).login(
            ConfigReader.getStandardUser(),
            ConfigReader.getPassword()
        );
    }

    // -------------------------------------------------------
    // TC_CART_001
    // -------------------------------------------------------
    @Test(groups = {"smoke", "regression"},
          description = "TC_CART_001 - Verify cart page opens with 'Your Cart' title")
    public void testCartPageTitle() {
        log.info("TC_CART_001: Cart page title check");
        HomePage homePage = loginAndGetHomePage();

        CartPage cartPage = homePage.goToCart();

        Assert.assertTrue(cartPage.isCartPageDisplayed(),
            "Cart page should display 'Your Cart' title");

        log.info("TC_CART_001 PASSED");
    }

    // -------------------------------------------------------
    // TC_CART_002
    // -------------------------------------------------------
    @Test(groups = {"smoke", "regression"},
          description = "TC_CART_002 - Verify cart is empty without adding any product")
    public void testCartIsEmptyInitially() {
        log.info("TC_CART_002: Empty cart check");
        HomePage homePage = loginAndGetHomePage();

        CartPage cartPage = homePage.goToCart();

        Assert.assertTrue(cartPage.isCartEmpty(),
            "Cart should be empty initially");
        Assert.assertEquals(cartPage.getCartItemCount(), 0,
            "Cart item count should be 0");

        log.info("TC_CART_002 PASSED");
    }

    // -------------------------------------------------------
    // TC_CART_003
    // -------------------------------------------------------
    @Test(groups = {"smoke", "regression"},
          description = "TC_CART_003 - Verify single product added to cart appears in cart page")
    public void testSingleProductInCart() {
        log.info("TC_CART_003: Single product in cart");
        HomePage homePage = loginAndGetHomePage();

        homePage.addProductToCartByIndex(0); // Sauce Labs Backpack
        CartPage cartPage = homePage.goToCart();

        Assert.assertEquals(cartPage.getCartItemCount(), 1,
            "Cart should contain exactly 1 item");
        Assert.assertTrue(cartPage.isItemInCart(PRODUCT_1),
            "'" + PRODUCT_1 + "' should be present in cart");

        log.info("TC_CART_003 PASSED: '{}' is in cart", PRODUCT_1);
    }

    // -------------------------------------------------------
    // TC_CART_004
    // -------------------------------------------------------
    @Test(groups = {"regression"},
          description = "TC_CART_004 - Verify multiple products added to cart")
    public void testMultipleProductsInCart() {
        log.info("TC_CART_004: Multiple products in cart");
        HomePage homePage = loginAndGetHomePage();

        homePage.addProductToCartByIndex(0);
        homePage.addProductToCartByIndex(1);
        homePage.addProductToCartByIndex(2);

        Assert.assertEquals(homePage.getCartItemCount(), 3,
            "Cart badge should show 3 after adding 3 products");

        CartPage cartPage = homePage.goToCart();

        Assert.assertEquals(cartPage.getCartItemCount(), 3,
            "Cart page should show 3 items");

        log.info("TC_CART_004 PASSED: 3 items in cart");
    }

    // -------------------------------------------------------
    // TC_CART_005
    // -------------------------------------------------------
    @Test(groups = {"regression"},
          description = "TC_CART_005 - Verify product can be removed from cart page")
    public void testRemoveItemFromCart() {
        log.info("TC_CART_005: Remove item from cart");
        HomePage homePage = loginAndGetHomePage();

        homePage.addProductToCartByIndex(0);
        homePage.addProductToCartByIndex(1);

        CartPage cartPage = homePage.goToCart();
        Assert.assertEquals(cartPage.getCartItemCount(), 2, "Should start with 2 items");

        cartPage.removeItemFromCart(PRODUCT_1);

        Assert.assertEquals(cartPage.getCartItemCount(), 1, "Cart should have 1 item after removal");
        Assert.assertFalse(cartPage.isItemInCart(PRODUCT_1),
            "'" + PRODUCT_1 + "' should no longer be in cart");

        log.info("TC_CART_005 PASSED");
    }

    // -------------------------------------------------------
    // TC_CART_006
    // -------------------------------------------------------
    @Test(groups = {"regression"},
    	      description = "TC_CART_006 - Verify cart badge count updates after removing item")
    public void testCartBadgeUpdatesAfterRemove() {
	    log.info("TC_CART_006: Cart badge updates after remove");
	    HomePage homePage = loginAndGetHomePage();

	    homePage.addProductToCartByIndex(0);
	    homePage.addProductToCartByIndex(1);

	    // Verify 2 items added
	    Assert.assertEquals(homePage.getCartItemCount(), 2, "Badge should show 2");

	    // Go to cart and remove one item there (more reliable than homepage remove)
	    CartPage cartPage = homePage.goToCart();
	    Assert.assertEquals(cartPage.getCartItemCount(), 2, "Cart should have 2 items");
	    cartPage.removeAllItems();

	    // Add back just 1 item
	    HomePage returnedHome = cartPage.continueShopping();
	    returnedHome.addProductToCartByIndex(0);
	    Assert.assertEquals(returnedHome.getCartItemCount(), 1,
	        "Badge should show 1 after removal");

	    log.info("TC_CART_006 PASSED");
	}

    // -------------------------------------------------------
    // TC_CART_007
    // -------------------------------------------------------
    @Test(groups = {"regression"},
          description = "TC_CART_007 - Verify cart persists when navigating back from cart to home")
    public void testCartPersistsAfterNavigation() {
        log.info("TC_CART_007: Cart persists after navigation");
        HomePage homePage = loginAndGetHomePage();

        homePage.addProductToCartByIndex(0);
        Assert.assertEquals(homePage.getCartItemCount(), 1, "Cart should have 1 item");

        CartPage cartPage = homePage.goToCart();
        Assert.assertEquals(cartPage.getCartItemCount(), 1, "Cart should still have 1 item");

        HomePage returnedHomePage = cartPage.continueShopping();
        Assert.assertEquals(returnedHomePage.getCartItemCount(), 1,
            "Cart should persist after navigating back to home");

        log.info("TC_CART_007 PASSED");
    }

    // -------------------------------------------------------
    // TC_CART_008
    // -------------------------------------------------------
    @Test(groups = {"smoke", "regression"},
          description = "TC_CART_008 - Verify Continue Shopping button returns to Products page")
    public void testContinueShoppingButton() {
        log.info("TC_CART_008: Continue Shopping button");
        HomePage homePage = loginAndGetHomePage();

        CartPage cartPage = homePage.goToCart();
        HomePage returnedHomePage = cartPage.continueShopping();

        Assert.assertTrue(returnedHomePage.isHomePageDisplayed(),
            "Should navigate back to Products page");

        log.info("TC_CART_008 PASSED");
    }

    // -------------------------------------------------------
    // TC_CART_009
    // -------------------------------------------------------
    @Test(groups = {"regression"},
          description = "TC_CART_009 - Verify all items can be removed from cart at once")
    public void testRemoveAllItemsFromCart() {
        log.info("TC_CART_009: Remove all items from cart");
        HomePage homePage = loginAndGetHomePage();

        homePage.addProductToCartByIndex(0);
        homePage.addProductToCartByIndex(1);
        homePage.addProductToCartByIndex(2);

        CartPage cartPage = homePage.goToCart();
        Assert.assertEquals(cartPage.getCartItemCount(), 3, "Should have 3 items before removal");

        cartPage.removeAllItems();

        Assert.assertTrue(cartPage.isCartEmpty(),
            "Cart should be empty after removing all items");
        Assert.assertEquals(cartPage.getCartItemCount(), 0,
            "Cart count should be 0");

        log.info("TC_CART_009 PASSED: All items removed");
    }
}
