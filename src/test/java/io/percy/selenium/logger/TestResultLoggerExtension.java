package io.percy.selenium.logger;

import io.percy.selenium.flow.BrowserFlow;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.extension.*;
import org.junit.jupiter.api.extension.ExtensionContext.Store;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


public class TestResultLoggerExtension implements TestWatcher, BeforeTestExecutionCallback, AfterTestExecutionCallback, AfterAllCallback {
    private static final Logger logger = LoggerFactory.getLogger(TestResultLoggerExtension.class);
    private final BrowserFlow browserFlow = new BrowserFlow();

    private final Map<String, TestResultStatus> testResultsStatus = new HashMap<>();
    private static final String START_TIME = "start time";

    @Override
    public void beforeTestExecution(ExtensionContext context) {
        getStore(context).put(START_TIME, System.currentTimeMillis());
        String testClass = this.getTestClassName(context).getSimpleName();
        String testMethod = this.getTestMethodName(context).getName();
        logger.info("I am in onTestStart method {}.{}", testClass, testMethod + " start");
    }

    @Override
    public void afterTestExecution(ExtensionContext context) {
        String testClass = this.getTestClassName(context).getSimpleName();
        String testMethod = this.getTestMethodName(context).getName();
        long startTime = getStore(context).remove(START_TIME, long.class);
        long duration = System.currentTimeMillis() - startTime;
        logger.info("I am in onTestFinish method {}.{}, duration time {} ms", testClass, testMethod, duration);
    }

    private enum TestResultStatus {
        SUCCESSFUL, ABORTED, FAILED, DISABLED
    }

    @Override
    public void afterAll(ExtensionContext context) {
        logger.info("Test result summary for {} ", testResultsStatus);
    }

    @Override
    @AfterEach
    public void testDisabled(ExtensionContext context, Optional<String> reason) {
        String testClass = this.getTestClassName(context).getSimpleName();
        String testMethod = this.getTestMethodName(context).getName();
        String testName = testClass + "." + testMethod + " " + context.getDisplayName();
        this.markTestStatus(TestResultStatus.DISABLED.name(), reason.orElse("Disabled"));
        logger.info("Test Disabled for {}: with reason :- {}", context.getDisplayName(), reason.orElse("Disabled"));
        testResultsStatus.put(testName, TestResultStatus.DISABLED);
        TestWatcher.super.testDisabled(context, reason);
    }

    @Override
    @AfterEach
    public void testSuccessful(ExtensionContext context) {
        String testClass = this.getTestClassName(context).getSimpleName();
        String testMethod = this.getTestMethodName(context).getName();
        String testName = testClass + "." + testMethod + " " + context.getDisplayName();
        this.markTestStatus(TestResultStatus.SUCCESSFUL.name(), TestResultStatus.SUCCESSFUL.name());
        logger.info("Test Successful for {}: ", context.getDisplayName());
        testResultsStatus.put(testName, TestResultStatus.SUCCESSFUL);
        TestWatcher.super.testSuccessful(context);
    }

    @Override
    @AfterEach
    public void testAborted(ExtensionContext context, Throwable cause) {
        String testClass = this.getTestClassName(context).getSimpleName();
        String testMethod = this.getTestMethodName(context).getName();
        String testName = testClass + "." + testMethod + " " + context.getDisplayName();
        this.markTestStatus(TestResultStatus.ABORTED.name(), cause.getMessage());
        logger.info("Test Aborted for {}: ", context.getDisplayName());
        testResultsStatus.put(testName, TestResultStatus.ABORTED);
        TestWatcher.super.testAborted(context, cause);
    }

    @Override
    @AfterEach
    public void testFailed(ExtensionContext context, Throwable cause) {
        String testClass = this.getTestClassName(context).getSimpleName();
        String testMethod = this.getTestMethodName(context).getName();
        String testName = testClass + "." + testMethod + " " + context.getDisplayName();
        this.markTestStatus(TestResultStatus.FAILED.name(), cause.getMessage());
        logger.info("Test Failed for {}: ", context.getDisplayName());
        testResultsStatus.put(testName, TestResultStatus.FAILED);
        TestWatcher.super.testFailed(context, cause);
    }

    private Method getTestMethodName(ExtensionContext context) {
        return context.getRequiredTestMethod();
    }

    private Class<?> getTestClassName(ExtensionContext context) {
        return context.getRequiredTestClass();
    }

    private Store getStore(ExtensionContext context) {
        return context.getStore(ExtensionContext.Namespace.create(getClass(), context.getRequiredTestMethod()));
    }

    private void markTestStatus(String status, String reason) {
        try {
            browserFlow.getJsExecutor().executeScript("browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\": \"" + status + "\", \"reason\": \"" + reason + "\"}}");
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}
