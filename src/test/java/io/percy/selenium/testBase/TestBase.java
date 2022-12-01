package io.percy.selenium.testBase;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import io.percy.selenium.Percy;
import io.percy.selenium.annotations.Flow;
import io.percy.selenium.core.properties.Properties;
import io.percy.selenium.core.properties.PropertiesLoader;
import io.percy.selenium.flow.SeleniumFlow;
import io.percy.selenium.flow.basePageFlow.InitFlow;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.*;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
//@ExtendWith({TestResultLoggerExtension.class})
public class TestBase extends InitFlow implements TestWatcher, BeforeTestExecutionCallback, AfterTestExecutionCallback, AfterAllCallback {
    private static RemoteWebDriver driver;
    protected static Percy percy;
    private static final Logger logger = LoggerFactory.getLogger(TestBase.class);
    @Flow SeleniumFlow seleniumFlow;
    private final List<TestResultStatus> testResultsStatus = new ArrayList<>();
    private static final String START_TIME = "start time";
    private enum TestResultStatus { SUCCESSFUL, ABORTED, FAILED, DISABLED }
    //private static final String buildName = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

    @Step
    public static void setUpDriver(String browserName, String platform,
                                   String platformVersion, TestInfo testInfo,
                                   String browserVersion, String screenResolution, String deviceName) {
        PropertiesLoader.loadProperties();

        String hubUrl = System.getProperty(Properties.BROWSER_STACK_USER_NAME) + ":" +
                System.getProperty(Properties.BROWSER_STACK_API_KEY) + System.getProperty(Properties.BROWSER_STACK_HUB_URL);
        System.out.println("platformName = " + platform + ", browserName = " + browserName);

        if (platform.equals("Windows") | platform.equals("OS X")) {
            MutableCapabilities browserOptions = setBrowserOptions(browserName, browserVersion);
            MutableCapabilities browserstackOptions = setBrowserStackBrowserOptions(platform, platformVersion, testInfo, screenResolution, browserName);
            browserOptions.setCapability("bstack:options", browserstackOptions);
            setConnectionForBrowserStack(hubUrl, browserOptions);
        } else {
            MutableCapabilities mobileOptions = setMobileOptions(browserName, platform);
            MutableCapabilities browserstackOptions = setBrowserStackMobileOptions(platform, platformVersion, testInfo, browserName, deviceName);
            mobileOptions.setCapability("bstack:options", browserstackOptions);
            setConnectionForBrowserStack(hubUrl, mobileOptions);
        }
        Configuration.timeout = 10000;
        WebDriverRunner.setWebDriver(driver);
        percy = new Percy(WebDriverRunner.getWebDriver());
        //Selenide.open(System.getProperty(Properties.STAGE_OT_URL));
    }


    private static MutableCapabilities setBrowserStackBrowserOptions(String platform, String platformVersion,
                                                                     TestInfo testInfo, String screenResolution, String browserName) {
        MutableCapabilities browserstackOptions = new MutableCapabilities();
        browserstackOptions.setCapability("os", platform);
        browserstackOptions.setCapability("osVersion", platformVersion);
        browserstackOptions.setCapability("projectName", testInfo.getDisplayName());
        browserstackOptions.setCapability("buildName", "Visibility_testing_" + platform + "_" + browserName);//buildName
        browserstackOptions.setCapability("sessionName", "Visibility_testing_" + platform + "_" + browserName);
        browserstackOptions.setCapability("resolution", screenResolution);
        browserstackOptions.setCapability("local", "false");
        browserstackOptions.setCapability("debug", "true");
        browserstackOptions.setCapability("consoleLogs", "info");
        browserstackOptions.setCapability("networkLogs", "true");
        browserstackOptions.setCapability("video", "false");
        browserstackOptions.setCapability("seleniumVersion", "4.1.0");
        browserstackOptions.setCapability("telemetryLogs", "true");
        browserstackOptions.setCapability("idleTimeout", "90");
        return browserstackOptions;
    }

    private static MutableCapabilities setBrowserOptions(String browserName, String browserVersion) {
        MutableCapabilities browserOptions = new MutableCapabilities();
        browserOptions.setCapability(CapabilityType.BROWSER_NAME, browserName);
        browserOptions.setCapability(CapabilityType.BROWSER_VERSION, browserVersion);
        return browserOptions;
    }

    private static MutableCapabilities setMobileOptions(String browserName, String platform) {
        MutableCapabilities mobileOptions = new MutableCapabilities();
        mobileOptions.setCapability(CapabilityType.BROWSER_NAME, browserName);
        mobileOptions.setCapability(CapabilityType.PLATFORM_NAME, platform);
        return mobileOptions;
    }

    private static MutableCapabilities setBrowserStackMobileOptions(String platform, String platformVersion,
                                                                    TestInfo testInfo, String browserName, String deviceName) {
        MutableCapabilities browserstackOptions = new MutableCapabilities();
        browserstackOptions.setCapability("osVersion", platformVersion);
        browserstackOptions.setCapability("deviceName", deviceName);
        browserstackOptions.setCapability("projectName", testInfo.getDisplayName());
        browserstackOptions.setCapability("buildName", "Visibility_testing_" + platform + "_" + browserName);//buildName
        browserstackOptions.setCapability("sessionName", "Visibility_testing_" + platform + "_" + browserName);
        browserstackOptions.setCapability("local", "false");
        browserstackOptions.setCapability("debug", "true");
        browserstackOptions.setCapability("consoleLogs", "info");
        browserstackOptions.setCapability("networkLogs", "true");
        browserstackOptions.setCapability("video", "false");
        browserstackOptions.setCapability("seleniumVersion", "4.1.0");
        browserstackOptions.setCapability("telemetryLogs", "true");
        browserstackOptions.setCapability("idleTimeout", "90");
        return browserstackOptions;
    }

    private static void setConnectionForBrowserStack(String hubUrl, MutableCapabilities deviceOptions) {
        try {
            driver = new RemoteWebDriver(new URL(hubUrl), deviceOptions);
        } catch (MalformedURLException e) {
            log.error(e.getMessage());
        }
    }

    public static String getScreenshotName(TestInfo testInfo, String platform, String platformVersion,
                                           String browserName, String browserVersion, String screenResolution,
                                           String deviceName) {
        String testName = testInfo.getTestMethod().map(Method::getName).map(String::toString).orElse("");
        String platformName = platform.replace(" ", "_");
        return StringUtils.isEmpty(deviceName) ?
                testName + "_" + platformName + "_" + platformVersion + "_" + browserName + "_" + browserVersion + "_" + screenResolution :
                testName + "_" + platformName + "_" + platformVersion + "_" + browserName + "_" + deviceName;
    }

    @AfterEach
    protected void closeDriver() {
        if (driver != null) {
            driver.quit();
            log.info("Quit!!!!");
        }
    }

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
        this.markTestStatus(TestResultStatus.DISABLED.name(), reason.orElse("Disabled"));
    }

    @Override
    public void testSuccessful(ExtensionContext context) {
        logger.info("Test Successful for {}: ", context.getDisplayName());
        testResultsStatus.add(TestResultStatus.SUCCESSFUL);
        TestWatcher.super.testSuccessful(context);
        this.markTestStatus(TestResultStatus.SUCCESSFUL.name(), "");
    }

    @Override
    public void testAborted(ExtensionContext context, Throwable cause) {
        logger.info("Test Aborted for {}: ", context.getDisplayName());
        testResultsStatus.add(TestResultStatus.ABORTED);
        TestWatcher.super.testAborted(context, cause);
        this.markTestStatus(TestResultStatus.ABORTED.name(), cause.getMessage());
    }

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        logger.info("Test Failed for {}: ", context.getDisplayName());
        testResultsStatus.add(TestResultStatus.FAILED);
        TestWatcher.super.testFailed(context, cause);
        this.markTestStatus(TestResultStatus.FAILED.name(), cause.getMessage());
    }

    private Method getTestMethodName(ExtensionContext context) {
        return context.getRequiredTestMethod();
    }

    private Class<?> getTestClassName(ExtensionContext context) {
        return context.getRequiredTestClass();
    }

    private ExtensionContext.Store getStore(ExtensionContext context) {
        return context.getStore(ExtensionContext.Namespace.create(getClass(), context.getRequiredTestMethod()));
    }

    private void markTestStatus(String status, String reason) {
        try {
            seleniumFlow.getJsExecutor().executeScript("browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\": \"" + status + "\", \"reason\": \"" + reason + "\"}}");
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}
