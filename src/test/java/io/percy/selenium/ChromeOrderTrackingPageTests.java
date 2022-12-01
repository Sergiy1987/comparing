package io.percy.selenium;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import io.percy.selenium.data.BrowserName;
import io.percy.selenium.testBase.TestBase;
import io.qameta.allure.Step;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.JavascriptExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;

//@ExtendWith({TestResultLoggerExtension.class})
public class ChromeOrderTrackingPageTests extends TestBase implements TestWatcher {
    private TestInfo testInfo;
    private static final Logger logger = LoggerFactory.getLogger(ChromeOrderTrackingPageTests.class);
    //private final BrowserFlow browserFlow = new BrowserFlow();

    private final Map<String, TestResultStatus> testResultsStatus = new HashMap<>();

    @BeforeEach
    protected void init(TestInfo testInfo) {
        this.testInfo = testInfo;
    }

    private static Stream<Arguments> browserParameters() {
        return Stream.of(
                arguments(BrowserName.Chrome.name(), "Windows", "10", "latest", "1280x1024", ""),
                arguments(BrowserName.Chrome.name(), "OS X", "Ventura", "latest", "1280x1024", "")
                //arguments(BrowserName.Chrome.name(), "Android", "12.0", "", "", "Samsung Galaxy S22 Ultra")
        );
    }

    @ParameterizedTest(name = "Run {index}: {1} {2}, {0} {3}, {4}{5}")
    @MethodSource(value = "browserParameters")
    @DisplayName(value = "orderTrackingPageTest")
    @Step
    public void orderTrackingPageTest(String browserName, String platform, String platformVersion,
                                      String browserVersion, String screenResolution, String deviceName) {

        String screenshotName = TestBase.getScreenshotName(testInfo, platform, platformVersion, browserName, browserVersion, screenResolution, deviceName);
        TestBase.setUpDriver(browserName, platform, platformVersion, testInfo, browserVersion, screenResolution, deviceName);
        Selenide.open("https://www.browserstack.com/");
        Selenide.sleep(5000);
        //percy = new Percy(WebDriverRunner.getWebDriver());
        percy.snapshot(screenshotName, Arrays.asList(1280, 768, 375), 1024, true);
//    atOrderTrackingPage()
//            .openOrderTrackingPage(OrderTrackingElements.class)
//            .setOrderIntoInputField("12")
//            .clickOnOrderTrackingButton();
//    Selenide.sleep(5000);
    }
//    public static void setEnv(String key, String value) {
//        try {
//            Map<String, String> env = System.getenv();
//            Class<?> cl = env.getClass();
//            Field field = cl.getDeclaredField("m");
//            field.setAccessible(true);
//            Map<String, String> writableEnv = (Map<String, String>) field.get(env);
//            writableEnv.put(key, value);
//        } catch (Exception e) {
//            throw new IllegalStateException("Failed to set environment variable", e);
//        }
//    }
private enum TestResultStatus {
    PASSED, ABORTED, FAILED, DISABLED
}

@Override
//@Order(0)
public void testSuccessful(ExtensionContext context) {
    String testClass = this.getTestClassName(context).getSimpleName();
    String testMethod = this.getTestMethodName(context).getName();
    String testName = testClass + "." + testMethod + " " + context.getDisplayName();
    this.markTestStatus(TestResultStatus.PASSED.name(), TestResultStatus.PASSED.name());
    logger.info("Test Successful for {}: ", context.getDisplayName());
    testResultsStatus.put(testName, TestResultStatus.PASSED);
    //TestWatcher.super.testSuccessful(context);
}

    @Override
    //@Order(0)
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
    //@Order(0)
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

    private void markTestStatus(String status, String reason) {
        try {
            JavascriptExecutor jse = (JavascriptExecutor) WebDriverRunner.getAndCheckWebDriver();
            jse.executeScript("browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\": \"" + status + "\", \"reason\": \"" + reason + "\"}}");
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}
