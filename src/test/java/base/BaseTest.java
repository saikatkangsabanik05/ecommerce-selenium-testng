package base;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;
import utils.ConfigReader;
import utils.DriverManager;
import utils.ScreenshotUtil;

public class BaseTest {

    protected static final Logger log = LogManager.getLogger(BaseTest.class);

    @BeforeSuite(alwaysRun = true)
    public void beforeSuite() {
        log.info("========== TEST SUITE STARTED ==========");
        log.info("Base URL : {}", ConfigReader.getBaseUrl());
        log.info("Browser  : {}", ConfigReader.getBrowser());
    }

    @BeforeMethod(alwaysRun = true)
    public void setUp(ITestResult result) {
        log.info("------ Starting: {} ------",
            result.getMethod().getMethodName());

        // Fresh browser for every test — no shared state between retries
        DriverManager.initDriver();

        // Navigate to base URL
        getDriver().get(ConfigReader.getBaseUrl());

        // Extra wait for CI environments where page load is slow
        try { Thread.sleep(1500); } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        log.info("Navigated to: {}", ConfigReader.getBaseUrl());
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        String testName = result.getMethod().getMethodName();

        if (result.getStatus() == ITestResult.FAILURE) {
            log.error("FAILED: {}", testName);
            if (ConfigReader.isScreenshotOnFailure()) {
                try {
                    String path = ScreenshotUtil.captureScreenshot(
                        getDriver(), testName);
                    log.info("Screenshot: {}", path);
                } catch (Exception e) {
                    log.warn("Could not capture screenshot: {}", e.getMessage());
                }
            }
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            log.info("PASSED: {}", testName);
        } else {
            log.warn("SKIPPED: {}", testName);
        }

        // Always quit driver — ensures clean state for next test/retry
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