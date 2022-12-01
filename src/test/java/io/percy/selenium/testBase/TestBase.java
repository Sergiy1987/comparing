package io.percy.selenium.testBase;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import io.percy.selenium.Percy;
import io.percy.selenium.core.properties.Properties;
import io.percy.selenium.core.properties.PropertiesLoader;
import io.percy.selenium.logger.TestResultLoggerExtension;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;

@Slf4j
@ExtendWith({TestResultLoggerExtension.class})
public abstract class TestBase {
    private static RemoteWebDriver driver;
    protected static Percy percy;
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
}
