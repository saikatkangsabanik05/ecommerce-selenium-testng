package utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * WaitUtil - Explicit wait helper methods
 */
public class WaitUtil {

    private static final Logger log = LogManager.getLogger(WaitUtil.class);

    private static WebDriverWait getWait(WebDriver driver) {
        return new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getExplicitWait()));
    }

    public static WebElement waitForElementVisible(WebDriver driver, WebElement element) {
        log.debug("Waiting for element to be visible");
        return getWait(driver).until(ExpectedConditions.visibilityOf(element));
    }

    public static WebElement waitForElementClickable(WebDriver driver, WebElement element) {
        log.debug("Waiting for element to be clickable");
        return getWait(driver).until(ExpectedConditions.elementToBeClickable(element));
    }

    public static WebElement waitForElementVisible(WebDriver driver, By locator) {
        log.debug("Waiting for locator to be visible: {}", locator);
        return getWait(driver).until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static List<WebElement> waitForAllElementsVisible(WebDriver driver, By locator) {
        return getWait(driver).until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
    }

    public static boolean waitForTextInElement(WebDriver driver, WebElement element, String text) {
        return getWait(driver).until(ExpectedConditions.textToBePresentInElement(element, text));
    }

    public static boolean waitForUrlContains(WebDriver driver, String urlFragment) {
        return getWait(driver).until(ExpectedConditions.urlContains(urlFragment));
    }

    public static void hardWait(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
