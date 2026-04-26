package pages;

import base.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * LoginPage - Page Object for the Login page
 */
public class LoginPage extends BasePage {

    // ---- Locators ----
    @FindBy(id = "user-name")
    private WebElement usernameField;

    @FindBy(id = "password")
    private WebElement passwordField;

    @FindBy(id = "login-button")
    private WebElement loginButton;

    @FindBy(css = "[data-test='error']")
    private WebElement errorMessage;

    @FindBy(css = ".login_logo")
    private WebElement loginLogo;

    // ---- Constructor ----
    public LoginPage(WebDriver driver) {
        super(driver);
    }

    // ---- Actions ----
    public LoginPage enterUsername(String username) {
        log.info("Entering username: {}", username);
        type(usernameField, username);
        return this;
    }

    public LoginPage enterPassword(String password) {
        log.info("Entering password");
        type(passwordField, password);
        return this;
    }

    public HomePage clickLoginButton() {
        log.info("Clicking Login button");
        click(loginButton);
        return new HomePage(driver);
    }

    public LoginPage clickLoginExpectingFailure() {
        log.info("Clicking Login button (expecting failure)");
        click(loginButton);
        return this;
    }

    /**
     * Full login flow — returns HomePage on success
     */
    public HomePage login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        return clickLoginButton();
    }

    // ---- Assertions / Getters ----
    public boolean isLoginPageDisplayed() {
        return isDisplayed(loginLogo);
    }

    public String getErrorMessage() {
        return getText(errorMessage);
    }

    public boolean isErrorDisplayed() {
        return isDisplayed(errorMessage);
    }
}
