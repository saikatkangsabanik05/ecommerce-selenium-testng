package listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import utils.ConfigReader;
import utils.DriverManager;
import utils.ScreenshotUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

/**
 * ExtentReportListener - Generates HTML Extent Reports
 */
public class ExtentReportListener implements ITestListener {

    private static ExtentReports extent;
    private static final ThreadLocal<ExtentTest> testThreadLocal = new ThreadLocal<>();

    @Override
    public void onStart(ITestContext context) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String reportPath = ConfigReader.getReportPath() + ConfigReader.getReportName() + "_" + timestamp + ".html";

        ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
        spark.config().setTheme(Theme.DARK);
        spark.config().setDocumentTitle("eCommerce Automation Report");
        spark.config().setReportName("eCommerce Test Execution Report");
        spark.config().setEncoding("utf-8");

        extent = new ExtentReports();
        extent.attachReporter(spark);
        extent.setSystemInfo("Application", "eCommerce App (SauceDemo)");
        extent.setSystemInfo("Browser", ConfigReader.getBrowser());
        extent.setSystemInfo("Environment", "QA");
        extent.setSystemInfo("Tester", System.getProperty("user.name"));
        extent.setSystemInfo("OS", System.getProperty("os.name"));
    }

    @Override
    public void onTestStart(ITestResult result) {
        ExtentTest test = extent.createTest(
            result.getMethod().getMethodName(),
            result.getMethod().getDescription()
        );
        test.assignCategory(result.getMethod().getGroups());
        testThreadLocal.set(test);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        getTest().pass("✅ Test Passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        getTest().fail(result.getThrowable());
        try {
            byte[] screenshot = ScreenshotUtil.captureScreenshotAsBytes(DriverManager.getDriver());
            String base64 = Base64.getEncoder().encodeToString(screenshot);
            getTest().fail("Screenshot on Failure:",
                MediaEntityBuilder.createScreenCaptureFromBase64String(base64).build());
        } catch (Exception e) {
            getTest().warning("Could not capture screenshot: " + e.getMessage());
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        getTest().skip(result.getThrowable());
    }

    @Override
    public void onFinish(ITestContext context) {
        if (extent != null) {
            extent.flush();
        }
    }

    private static ExtentTest getTest() {
        return testThreadLocal.get();
    }
}
