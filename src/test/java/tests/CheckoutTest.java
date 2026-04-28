package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.CartPage;
import pages.CheckoutPage;
import pages.HomePage;
import pages.LoginPage;
import utils.ConfigReader;

/**
 * CheckoutTest - Covers all Checkout scenarios (End-to-End order flow)
 *
 * Test Cases:
 *  TC_CHK_001 - Complete checkout end-to-end successfully
 *  TC_CHK_002 - Verify checkout page title (Step One)
 *  TC_CHK_003 - Verify error when first name is missing
 *  TC_CHK_004 - Verify error when last name is missing
 *  TC_CHK_005 - Verify error when zip code is missing
 *  TC_CHK_006 - Verify order summary total = subtotal + tax
 *  TC_CHK_007 - Verify order confirmation message
 *  TC_CHK_008 - Verify Cancel button on checkout returns to cart
 *  TC_CHK_009 - Verify checkout with multiple items
 */
public class CheckoutTest extends BaseTest {

    private HomePage loginAndGetHomePage() {
        return new LoginPage(getDriver()).login(
            ConfigReader.getStandardUser(),
            ConfigReader.getPassword()
        );
    }

    private CartPage addItemAndGoToCart() {
        HomePage homePage = loginAndGetHomePage();
        // Wait for page to fully load before adding to cart
        try { Thread.sleep(2000); } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        homePage.addProductToCartByIndex(0);
        // Wait for cart to update before navigating
        try { Thread.sleep(2000); } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return homePage.goToCart();
    }

    // -------------------------------------------------------
    // TC_CHK_001
    // -------------------------------------------------------
    @Test(groups = {"smoke", "regression"},
          description = "TC_CHK_001 - Complete end-to-end checkout flow")
    public void testCompleteCheckoutEndToEnd() {
        log.info("TC_CHK_001: End-to-end checkout");

        CartPage cartPage = addItemAndGoToCart();
        Assert.assertEquals(cartPage.getCartItemCount(), 1, "Cart should have 1 item");

        CheckoutPage checkoutPage = cartPage.proceedToCheckout();
        checkoutPage.fillShippingInfo(
            ConfigReader.getTestFirstName(),
            ConfigReader.getTestLastName(),
            ConfigReader.getTestZipCode()
        ).clickContinue();

        // Step Two - Order Overview
        Assert.assertTrue(checkoutPage.getSubtotal() > 0, "Subtotal should be positive");
        Assert.assertTrue(checkoutPage.getTax() > 0, "Tax should be positive");
        Assert.assertTrue(checkoutPage.getTotal() > 0, "Total should be positive");

        checkoutPage.clickFinish();

        Assert.assertTrue(checkoutPage.isOrderConfirmed(),
            "Order confirmation should be displayed");

        log.info("TC_CHK_001 PASSED: Order placed successfully");
    }

    // -------------------------------------------------------
    // TC_CHK_002
    // -------------------------------------------------------
    @Test(groups = {"regression"},
          description = "TC_CHK_002 - Verify checkout page title on Step One")
    public void testCheckoutStepOneTitle() {
        log.info("TC_CHK_002: Checkout step one title");

        CartPage cartPage = addItemAndGoToCart();
        CheckoutPage checkoutPage = cartPage.proceedToCheckout();

        Assert.assertEquals(checkoutPage.getPageTitle(), "Checkout: Your Information",
            "Checkout Step 1 title should be 'Checkout: Your Information'");

        log.info("TC_CHK_002 PASSED");
    }

    // -------------------------------------------------------
    // TC_CHK_003
    // -------------------------------------------------------
    @Test(groups = {"regression"},
    	      description = "TC_CHK_003 - Verify error when first name is missing")
    	public void testErrorWhenFirstNameMissing() {
    	    log.info("TC_CHK_003: Error for missing first name");
    	    CartPage cartPage = addItemAndGoToCart();
    	    CheckoutPage checkoutPage = cartPage.proceedToCheckout();

    	    // Only fill last name and zip — skip first name
    	    checkoutPage.enterLastName("Doe")
    	                .enterZipCode("10001")
    	                .clickContinueForValidation();

    	    Assert.assertTrue(checkoutPage.isErrorDisplayed(),
    	        "Error should be shown when first name is missing");
    	    Assert.assertTrue(checkoutPage.getErrorMessage().contains("First Name is required"),
    	        "Error should mention First Name, got: " + checkoutPage.getErrorMessage());
    	    log.info("TC_CHK_003 PASSED");
    	}

    	@Test(groups = {"regression"},
    	      description = "TC_CHK_004 - Verify error when last name is missing")
    	public void testErrorWhenLastNameMissing() {
    	    log.info("TC_CHK_004: Error for missing last name");
    	    CartPage cartPage = addItemAndGoToCart();
    	    CheckoutPage checkoutPage = cartPage.proceedToCheckout();

    	    // Fill first name and zip — skip last name
    	    checkoutPage.enterFirstName("John")
    	                .enterZipCode("10001")
    	                .clickContinueForValidation();

    	    Assert.assertTrue(checkoutPage.isErrorDisplayed(),
    	        "Error should be shown when last name is missing");
    	    Assert.assertTrue(checkoutPage.getErrorMessage().contains("Last Name is required"),
    	        "Error should mention Last Name, got: " + checkoutPage.getErrorMessage());
    	    log.info("TC_CHK_004 PASSED");
    	}

    	@Test(groups = {"regression"},
    	      description = "TC_CHK_005 - Verify error when zip code is missing")
    	public void testErrorWhenZipCodeMissing() {
    	    log.info("TC_CHK_005: Error for missing zip code");
    	    CartPage cartPage = addItemAndGoToCart();
    	    CheckoutPage checkoutPage = cartPage.proceedToCheckout();

    	    // Fill first name and last name — skip zip
    	    checkoutPage.enterFirstName("John")
    	                .enterLastName("Doe")
    	                .clickContinueForValidation();

    	    Assert.assertTrue(checkoutPage.isErrorDisplayed(),
    	        "Error should be shown when zip code is missing");
    	    Assert.assertTrue(checkoutPage.getErrorMessage().contains("Postal Code is required"),
    	        "Error should mention Postal Code, got: " + checkoutPage.getErrorMessage());
    	    log.info("TC_CHK_005 PASSED");
    	}

    // -------------------------------------------------------
    // TC_CHK_006
    // -------------------------------------------------------
    @Test(groups = {"regression"},
          description = "TC_CHK_006 - Verify order total = subtotal + tax")
    public void testOrderTotalEqualsSubtotalPlusTax() {
        log.info("TC_CHK_006: Total = Subtotal + Tax");

        CartPage cartPage = addItemAndGoToCart();
        CheckoutPage checkoutPage = cartPage.proceedToCheckout();

        checkoutPage.fillShippingInfo(
            ConfigReader.getTestFirstName(),
            ConfigReader.getTestLastName(),
            ConfigReader.getTestZipCode()
        ).clickContinue();

        double subtotal = checkoutPage.getSubtotal();
        double tax      = checkoutPage.getTax();
        double total    = checkoutPage.getTotal();

        double expectedTotal = Math.round((subtotal + tax) * 100.0) / 100.0;
        double actualTotal   = Math.round(total * 100.0) / 100.0;

        log.info("Subtotal=${}, Tax=${}, Total=${}, Expected=${}", subtotal, tax, total, expectedTotal);

        Assert.assertEquals(actualTotal, expectedTotal,
            "Total should equal subtotal + tax");

        log.info("TC_CHK_006 PASSED");
    }

    // -------------------------------------------------------
    // TC_CHK_007
    // -------------------------------------------------------
    @Test(groups = {"smoke", "regression"},
          description = "TC_CHK_007 - Verify order confirmation message after placing order")
    public void testOrderConfirmationMessage() {
        log.info("TC_CHK_007: Order confirmation message");

        CartPage cartPage = addItemAndGoToCart();
        CheckoutPage checkoutPage = cartPage.proceedToCheckout();

        checkoutPage.fillShippingInfo(
            ConfigReader.getTestFirstName(),
            ConfigReader.getTestLastName(),
            ConfigReader.getTestZipCode()
        ).clickContinue().clickFinish();

        String confirmationMsg = checkoutPage.getConfirmationMessage();
        Assert.assertEquals(confirmationMsg, "Thank you for your order!",
            "Confirmation message should be 'Thank you for your order!'");

        log.info("TC_CHK_007 PASSED: Confirmation - '{}'", confirmationMsg);
    }

    // -------------------------------------------------------
    // TC_CHK_008
    // -------------------------------------------------------
    @Test(groups = {"regression"},
          description = "TC_CHK_008 - Verify Cancel button returns user to cart")
    public void testCancelCheckoutReturnsToCart() {
        log.info("TC_CHK_008: Cancel checkout returns to cart");

        CartPage cartPage = addItemAndGoToCart();
        CheckoutPage checkoutPage = cartPage.proceedToCheckout();

        CartPage returnedCart = checkoutPage.clickCancel();

        Assert.assertTrue(returnedCart.isCartPageDisplayed(),
            "Should return to cart page after cancelling checkout");

        log.info("TC_CHK_008 PASSED");
    }

    // -------------------------------------------------------
    // TC_CHK_009
    // -------------------------------------------------------
    @Test(groups = {"regression"},
          description = "TC_CHK_009 - Verify checkout works correctly with multiple items")
    public void testCheckoutWithMultipleItems() {
        log.info("TC_CHK_009: Checkout with multiple items");

        HomePage homePage = loginAndGetHomePage();
        homePage.addProductToCartByIndex(0);
        homePage.addProductToCartByIndex(1);
        homePage.addProductToCartByIndex(2);

        CartPage cartPage = homePage.goToCart();
        Assert.assertEquals(cartPage.getCartItemCount(), 3, "Cart should have 3 items");

        CheckoutPage checkoutPage = cartPage.proceedToCheckout();
        checkoutPage.fillShippingInfo(
            ConfigReader.getTestFirstName(),
            ConfigReader.getTestLastName(),
            ConfigReader.getTestZipCode()
        ).clickContinue();

        double subtotal = checkoutPage.getSubtotal();
        double tax      = checkoutPage.getTax();
        double total    = checkoutPage.getTotal();

        Assert.assertTrue(subtotal > 0, "Subtotal should be positive with 3 items");
        Assert.assertEquals(
            Math.round(total * 100.0) / 100.0,
            Math.round((subtotal + tax) * 100.0) / 100.0,
            "Total should match subtotal + tax for 3 items"
        );

        checkoutPage.clickFinish();

        Assert.assertTrue(checkoutPage.isOrderConfirmed(),
            "Order should be confirmed with multiple items");

        log.info("TC_CHK_009 PASSED: Multi-item checkout successful");
    }
}
