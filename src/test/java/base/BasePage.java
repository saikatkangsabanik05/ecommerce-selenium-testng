package base;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import utils.WaitUtil;

/**
 * BasePage - Parent class for all Page Object classes.
 * Contains reusable Selenium interaction methods.
 */
public class BasePage {

    protected WebDriver driver;
    protected static final Logger log = LogManager.getLogger(BasePage.class);

    public BasePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    protected void click(WebElement element) {
        WaitUtil.waitForElementClickable(driver, element);
        element.click();
        log.debug("Clicked: {}", element);
    }

    protected void type(WebElement element, String text) {
        WaitUtil.waitForElementVisible(driver, element);
        element.clear();
        element.sendKeys(text);
        log.debug("Typed '{}' into: {}", text, element);
    }

    protected String getText(WebElement element) {
        WaitUtil.waitForElementVisible(driver, element);
        return element.getText().trim();
    }

    protected boolean isDisplayed(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    protected void scrollToElement(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    protected void jsClick(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    protected String getPageTitle() {
        return driver.getTitle();
    }

    protected String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
}
