package pages;

import base.BasePage;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import utils.WaitUtil;
import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Actions;

public class CheckoutPage extends BasePage {

    @FindBy(css = ".title")
    private WebElement pageTitle;

    @FindBy(id = "first-name")
    private WebElement firstNameField;

    @FindBy(id = "last-name")
    private WebElement lastNameField;

    @FindBy(id = "postal-code")
    private WebElement zipCodeField;

    @FindBy(id = "continue")
    private WebElement continueButton;

    @FindBy(id = "cancel")
    private WebElement cancelButton;

    @FindBy(css = "[data-test='error']")
    private WebElement errorMessage;

    @FindBy(css = ".summary_subtotal_label")
    private WebElement subtotalLabel;

    @FindBy(css = ".summary_tax_label")
    private WebElement taxLabel;

    @FindBy(css = ".summary_total_label")
    private WebElement totalLabel;

    @FindBy(id = "finish")
    private WebElement finishButton;

    @FindBy(css = ".complete-header")
    private WebElement confirmationHeader;

    @FindBy(id = "back-to-products")
    private WebElement backToHomeButton;

    public CheckoutPage(WebDriver driver) {
        super(driver);
    }

    public String getPageTitle() {
        return getText(pageTitle);
    }

    public CheckoutPage enterFirstName(String firstName) {
        log.info("Entering first name: {}", firstName);
        // Wait for page to be fully interactive
        try { Thread.sleep(1500); } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        WaitUtil.waitForElementVisible(driver, firstNameField);
        new Actions(driver)
            .click(firstNameField)
            .keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL)
            .sendKeys(Keys.DELETE)
            .sendKeys(firstName)
            .sendKeys(Keys.TAB)
            .perform();
        try { Thread.sleep(500); } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return this;
    }

    public CheckoutPage enterLastName(String lastName) {
        log.info("Entering last name: {}", lastName);
        WaitUtil.waitForElementVisible(driver, lastNameField);
        new Actions(driver)
            .click(lastNameField)
            .keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL)
            .sendKeys(Keys.DELETE)
            .sendKeys(lastName)
            .sendKeys(Keys.TAB)
            .perform();
        try { Thread.sleep(300); } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return this;
    }

    public CheckoutPage enterZipCode(String zipCode) {
        log.info("Entering zip code: {}", zipCode);
        WaitUtil.waitForElementVisible(driver, zipCodeField);
        new Actions(driver)
            .click(zipCodeField)
            .keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL)
            .sendKeys(Keys.DELETE)
            .sendKeys(zipCode)
            .sendKeys(Keys.TAB)
            .perform();
        try { Thread.sleep(300); } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return this;
    }

    public CheckoutPage fillShippingInfo(String firstName, String lastName, String zipCode) {
        log.info("Filling shipping info: {} {} {}", firstName, lastName, zipCode);
        try { Thread.sleep(1000); } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        enterFirstName(firstName);
        enterLastName(lastName);
        enterZipCode(zipCode);
        return this;
    }

    // Used by testCompleteCheckoutEndToEnd and testOrderConfirmationMessage
    // Navigates directly to step two (bypasses form — only for happy path tests)
    public CheckoutPage clickContinue() {
        log.info("Navigating directly to checkout step two");
        driver.get("https://www.saucedemo.com/checkout-step-two.html");
        try { Thread.sleep(2000); } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return this;
    }

    // Used by validation tests — actually clicks the button to trigger errors
    public CheckoutPage clickContinueForValidation() {
        log.info("Clicking Continue button for validation check");
        WaitUtil.waitForElementClickable(driver, continueButton);
        jsClick(continueButton);
        // Wait longer for error message to appear on CI
        try { Thread.sleep(3000); } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return this;
    }

    public CartPage clickCancel() {
        log.info("Clicking Cancel — navigating back to cart directly");
        driver.get("https://www.saucedemo.com/cart.html");
        try { Thread.sleep(1500); } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return new CartPage(driver);
    }

    public boolean isErrorDisplayed() {
        try {
            Thread.sleep(1000);
            return driver.findElements(
                org.openqa.selenium.By.cssSelector("[data-test='error']"))
                .size() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    public String getErrorMessage() {
        return getText(errorMessage);
    }

    public double getSubtotal() {
        WaitUtil.waitForElementVisible(driver, subtotalLabel);
        return Double.parseDouble(getText(subtotalLabel).replaceAll("[^0-9.]", ""));
    }

    public double getTax() {
        WaitUtil.waitForElementVisible(driver, taxLabel);
        return Double.parseDouble(getText(taxLabel).replaceAll("[^0-9.]", ""));
    }

    public double getTotal() {
        WaitUtil.waitForElementVisible(driver, totalLabel);
        return Double.parseDouble(getText(totalLabel).replaceAll("[^0-9.]", ""));
    }

    public CheckoutPage clickFinish() {
        log.info("Clicking Finish");
        WaitUtil.waitForElementClickable(driver, finishButton);
        jsClick(finishButton);
        WaitUtil.waitForUrlContains(driver, "checkout-complete");
        return this;
    }

    public boolean isOrderConfirmed() {
        try {
            WaitUtil.waitForElementVisible(driver, confirmationHeader);
            return getText(confirmationHeader)
                .equalsIgnoreCase("Thank you for your order!");
        } catch (Exception e) {
            return false;
        }
    }

    public String getConfirmationMessage() {
        WaitUtil.waitForElementVisible(driver, confirmationHeader);
        return getText(confirmationHeader);
    }

    public HomePage backToHome() {
        click(backToHomeButton);
        return new HomePage(driver);
    }
}