package base;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;
import utils.ConfigReader;
import utils.DriverManager;
import utils.ScreenshotUtil;

/**
 * BaseTest - Parent class for all test classes.
 * Handles driver lifecycle, navigation, and screenshot on failure.
 */
public class BaseTest {

    protected static final Logger log = LogManager.getLogger(BaseTest.class);

    @BeforeSuite(alwaysRun = true)
    public void beforeSuite() {
        log.info("========== TEST SUITE STARTED ==========");
        log.info("Base URL      : {}", ConfigReader.getBaseUrl());
        log.info("Browser       : {}", ConfigReader.getBrowser());
        log.info("Headless Mode : {}", ConfigReader.isHeadless());
    }

    @BeforeMethod(alwaysRun = true)
    public void setUp(ITestResult result) {
        log.info("------ Starting Test: {} ------", result.getMethod().getMethodName());
        DriverManager.initDriver();
        getDriver().get(ConfigReader.getBaseUrl());
        log.info("Navigated to: {}", ConfigReader.getBaseUrl());
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        if (result.getStatus() == ITestResult.FAILURE) {
            log.error("TEST FAILED: {}", testName);
            if (ConfigReader.isScreenshotOnFailure()) {
                String path = ScreenshotUtil.captureScreenshot(getDriver(), testName);
                log.info("Failure screenshot: {}", path);
            }
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            log.info("TEST PASSED: {}", testName);
        } else {
            log.warn("TEST SKIPPED: {}", testName);
        }
        DriverManager.quitDriver();
    }

    @AfterSuite(alwaysRun = true)
    public void afterSuite() {
        log.info("========== TEST SUITE COMPLETED ==========");
    }

    protected WebDriver getDriver() {
        return DriverManager.getDriver();
    }
}
