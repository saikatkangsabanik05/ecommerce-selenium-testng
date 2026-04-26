package pages;

import base.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * CheckoutPage - Covers both Checkout Step One (info) and Step Two (overview)
 */
public class CheckoutPage extends BasePage {

    // --- Step One (Shipping Info) ---
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

    // --- Step Two (Order Overview) ---
    @FindBy(css = ".summary_subtotal_label")
    private WebElement subtotalLabel;

    @FindBy(css = ".summary_tax_label")
    private WebElement taxLabel;

    @FindBy(css = ".summary_total_label")
    private WebElement totalLabel;

    @FindBy(id = "finish")
    private WebElement finishButton;

    // --- Confirmation ---
    @FindBy(css = ".complete-header")
    private WebElement confirmationHeader;

    @FindBy(css = ".complete-text")
    private WebElement confirmationText;

    @FindBy(id = "back-to-products")
    private WebElement backToHomeButton;

    public CheckoutPage(WebDriver driver) {
        super(driver);
    }

    // ---- Step One Actions ----
    public String getPageTitle() {
        return getText(pageTitle);
    }

    public CheckoutPage enterFirstName(String firstName) {
        type(firstNameField, firstName);
        return this;
    }

    public CheckoutPage enterLastName(String lastName) {
        type(lastNameField, lastName);
        return this;
    }

    public CheckoutPage enterZipCode(String zipCode) {
        type(zipCodeField, zipCode);
        return this;
    }

    public CheckoutPage fillShippingInfo(String firstName, String lastName, String zipCode) {
        log.info("Filling shipping info: {} {} {}", firstName, lastName, zipCode);
        enterFirstName(firstName);
        enterLastName(lastName);
        enterZipCode(zipCode);
        return this;
    }

    public CheckoutPage clickContinue() {
        log.info("Clicking Continue");
        click(continueButton);
        return this;
    }

    public CartPage clickCancel() {
        log.info("Clicking Cancel");
        click(cancelButton);
        return new CartPage(driver);
    }

    public boolean isErrorDisplayed() {
        return isDisplayed(errorMessage);
    }

    public String getErrorMessage() {
        return getText(errorMessage);
    }

    // ---- Step Two Actions ----
    public double getSubtotal() {
        String text = getText(subtotalLabel);
        return Double.parseDouble(text.replaceAll("[^0-9.]", ""));
    }

    public double getTax() {
        String text = getText(taxLabel);
        return Double.parseDouble(text.replaceAll("[^0-9.]", ""));
    }

    public double getTotal() {
        String text = getText(totalLabel);
        return Double.parseDouble(text.replaceAll("[^0-9.]", ""));
    }

    public CheckoutPage clickFinish() {
        log.info("Clicking Finish");
        click(finishButton);
        return this;
    }

    // ---- Confirmation Actions ----
    public boolean isOrderConfirmed() {
        return isDisplayed(confirmationHeader) &&
               getText(confirmationHeader).equalsIgnoreCase("Thank you for your order!");
    }

    public String getConfirmationMessage() {
        return getText(confirmationHeader);
    }

    public HomePage backToHome() {
        click(backToHomeButton);
        return new HomePage(driver);
    }
}
