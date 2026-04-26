package listeners;

import org.testng.ITestListener;
import org.testng.ITestResult;
import utils.ConfigReader;
import utils.DriverManager;
import utils.ScreenshotUtil;

/**
 * ScreenshotListener - Captures screenshot automatically on test failure
 */
public class ScreenshotListener implements ITestListener {

    @Override
    public void onTestFailure(ITestResult result) {
        if (ConfigReader.isScreenshotOnFailure()) {
            try {
                String testName = result.getMethod().getMethodName();
                String path = ScreenshotUtil.captureScreenshot(DriverManager.getDriver(), testName);
                System.out.println("[ScreenshotListener] Screenshot saved: " + path);
            } catch (Exception e) {
                System.err.println("[ScreenshotListener] Failed to capture screenshot: " + e.getMessage());
            }
        }
    }
}
