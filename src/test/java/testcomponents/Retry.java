package testcomponents;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class Retry implements IRetryAnalyzer {

    int count = 1;
    int maxRetry = 1;

    @Override
    public boolean retry(ITestResult iTestResult) {

        if (count <= maxRetry) {
            count++;
            return true;
        }

        return false;
    }
}
