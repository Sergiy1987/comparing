package io.percy.selenium.logger;

import org.junit.jupiter.api.extension.*;
import org.junit.jupiter.api.extension.ExtensionContext.Store;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;


public class TestResultLoggerExtension implements TestWatcher, BeforeTestExecutionCallback, AfterTestExecutionCallback, AfterAllCallback {
    private static final Logger logger = LoggerFactory.getLogger(TestResultLoggerExtension.class);

    private final List<TestResultStatus> testResultsStatus = new ArrayList<>();
    private static final String START_TIME = "start time";

    @Override
    public void beforeTestExecution(ExtensionContext context) {
        getStore(context).put(START_TIME, System.currentTimeMillis());
        String testClass = getTestClassName(context).getSimpleName();
        String testMethod = getTestMethodName(context).getName();
        logger.info("I am in onTestStart method {}.{}", testClass, testMethod + " start");
    }

    @Override
    public void afterTestExecution(ExtensionContext context) {
        String testClass = getTestClassName(context).getSimpleName();
        String testMethod = getTestMethodName(context).getName();
        long startTime = getStore(context).remove(START_TIME, long.class);
        long duration = System.currentTimeMillis() - startTime;
        logger.info("I am in onTestFinish method {}.{}, duration time {} ms", testClass, testMethod, duration);
    }

    private enum TestResultStatus {
        SUCCESSFUL, ABORTED, FAILED, DISABLED
    }

    @Override
    public void afterAll(ExtensionContext context) {
        Map<TestResultStatus, Long> summary = testResultsStatus.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        logger.info("Test result summary for {} {}", context.getDisplayName(), summary.toString());
    }

    @Override
    public void testDisabled(ExtensionContext context, Optional<String> reason) {
        logger.info("Test Disabled for {}: with reason :- {}", context.getDisplayName(), reason.orElse("Disabled"));
        testResultsStatus.add(TestResultStatus.DISABLED);
        TestWatcher.super.testDisabled(context, reason);
    }

    @Override
    public void testSuccessful(ExtensionContext context) {
        logger.info("Test Successful for {}: ", context.getDisplayName());
        testResultsStatus.add(TestResultStatus.SUCCESSFUL);
        TestWatcher.super.testSuccessful(context);
    }

    @Override
    public void testAborted(ExtensionContext context, Throwable cause) {
        logger.info("Test Aborted for {}: ", context.getDisplayName());
        testResultsStatus.add(TestResultStatus.ABORTED);
        TestWatcher.super.testAborted(context, cause);
    }

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        logger.info("Test Failed for {}: ", context.getDisplayName());
        testResultsStatus.add(TestResultStatus.FAILED);
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
}
