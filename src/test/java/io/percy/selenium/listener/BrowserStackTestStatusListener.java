package io.percy.selenium.listener;

import io.percy.selenium.testBase.TestBaseTestNg;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

@Slf4j
public class BrowserStackTestStatusListener extends TestListenerAdapter {

    @Override
    public void onTestSuccess(ITestResult result) {
        Object currentClass = result.getInstance();
        WebDriver driver = ((TestBaseTestNg) currentClass).getDriver();
        this.markTestStatus("passed", "", driver);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        Object currentClass = result.getInstance();
        WebDriver driver = ((TestBaseTestNg) currentClass).getDriver();
        String message = result.getThrowable().getMessage();
        String reason = (message != null && message.length() > 254) ? message.substring(0, 254) : message;
        this.markTestStatus("failed", reason.replaceAll("[^a-zA-Z0-9._-]", " "), driver);
    }

    private void markTestStatus(String status, String reason, WebDriver driver) {
        try {
            JavascriptExecutor jse = (JavascriptExecutor) driver;
            jse.executeScript("browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\": \"" + status + "\", \"reason\": \"" + reason + "\"}}");
        } catch (Exception e) {
            log.error("Error executing javascript {}", e.getMessage());
        }
    }
}
