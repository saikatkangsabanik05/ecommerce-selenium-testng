package listeners;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;
import utils.ConfigReader;

public class RetryAnalyzer implements IRetryAnalyzer {

    private int retryCount = 0;

    @Override
    public boolean retry(ITestResult result) {
        int maxRetry = ConfigReader.getRetryCount();
        if (retryCount < maxRetry) {
            retryCount++;
            System.out.println("Retrying test ["
                + result.getName() + "] attempt "
                + retryCount + " of " + maxRetry);
            return true;
        }
        return false;
    }
}