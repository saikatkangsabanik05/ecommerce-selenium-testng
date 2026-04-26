package listeners;

import org.testng.IAnnotationTransformer;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;
import org.testng.annotations.ITestAnnotation;
import utils.ConfigReader;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * RetryAnalyzer - Retries failed tests up to configured count
 */
class RetryAnalyzer implements IRetryAnalyzer {

    private int retryCount = 0;

    @Override
    public boolean retry(ITestResult result) {
        int maxRetry = ConfigReader.getRetryCount();
        if (retryCount < maxRetry) {
            retryCount++;
            System.out.println("Retrying test [" + result.getName() + "] - attempt " + retryCount + " of " + maxRetry);
            return true;
        }
        return false;
    }
}

/**
 * RetryListener - Attaches RetryAnalyzer to all tests automatically
 */
public class RetryListener implements IAnnotationTransformer {

    @Override
    public void transform(ITestAnnotation annotation, Class testClass,
                          Constructor testConstructor, Method testMethod) {
        annotation.setRetryAnalyzer(RetryAnalyzer.class);
    }
}
