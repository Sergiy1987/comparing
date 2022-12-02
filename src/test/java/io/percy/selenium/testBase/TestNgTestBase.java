package io.percy.selenium.testBase;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.percy.selenium.Percy;
import io.percy.selenium.core.properties.Properties;
import io.percy.selenium.core.properties.PropertiesLoader;
import io.percy.selenium.data.BrowserName;
import io.qameta.allure.Step;
import io.qameta.allure.selenide.AllureSelenide;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;

@Slf4j
public class TestNgTestBase {
    private static RemoteWebDriver driver;
    protected static Percy percy;
    //private static final String buildName = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

    //@Step
    @BeforeClass
    @Parameters(value = {"browserName", "browserVersion", "platform", "platformVersion", //"method",
                            "screenResolution", "deviceName"})
    public void setUpDriver(String browserName, String browserVersion, String platform,
                                   String platformVersion, //Method method,
                                    String screenResolution, String deviceName) {
        PropertiesLoader.loadProperties();

        String hubUrl = System.getProperty(Properties.BROWSER_STACK_USER_NAME) + ":" +
                System.getProperty(Properties.BROWSER_STACK_API_KEY) + System.getProperty(Properties.BROWSER_STACK_HUB_URL);
        System.out.println("platformName = " + platform + ", browserName = " + browserName);

        if (platform.equals("Windows") | platform.equals("OS X")) {
            MutableCapabilities browserOptions = setBrowserOptions(browserName, browserVersion);
            MutableCapabilities browserstackOptions = setBrowserStackBrowserOptions(platform, platformVersion, //method,
                    screenResolution, browserName);
            browserOptions.setCapability("bstack:options", browserstackOptions);
            setConnectionForBrowserStack(hubUrl, browserOptions);
        } else {
            MutableCapabilities mobileOptions = setMobileOptions(browserName, platform);
            MutableCapabilities browserstackOptions = setBrowserStackMobileOptions(platform, platformVersion, //method,
                    browserName, deviceName);
            mobileOptions.setCapability("bstack:options", browserstackOptions);
            setConnectionForBrowserStack(hubUrl, mobileOptions);
        }
        Configuration.timeout = 10000;
        WebDriverRunner.setWebDriver(driver);
        percy = new Percy(WebDriverRunner.getWebDriver());
        selenideAllureLogger();
        //Selenide.open(System.getProperty(Properties.STAGE_OT_URL));
    }


    private static MutableCapabilities setBrowserStackBrowserOptions(String platform, String platformVersion,
                                                                     //Method method,
                                                                     String screenResolution, String browserName) {
        MutableCapabilities browserstackOptions = new MutableCapabilities();
        browserstackOptions.setCapability("os", platform);
        browserstackOptions.setCapability("osVersion", platformVersion);
        //browserstackOptions.setCapability("projectName", method.getName());
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
                                                                    //Method method,
                                                                    String browserName, String deviceName) {
        MutableCapabilities browserstackOptions = new MutableCapabilities();
        browserstackOptions.setCapability("osVersion", platformVersion);
        browserstackOptions.setCapability("deviceName", deviceName);
        //browserstackOptions.setCapability("projectName", method.getName());
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

    public static String getScreenshotName(Method method, String platform, String platformVersion,
                                           String browserName, String browserVersion, String screenResolution,
                                           String deviceName) {
        String testName = method.getName();
        String platformName = platform.replace(" ", "_");
        return StringUtils.isEmpty(deviceName) ?
                testName + "_" + platformName + "_" + platformVersion + "_" + browserName + "_" + browserVersion + "_" + screenResolution :
                testName + "_" + platformName + "_" + platformVersion + "_" + browserName + "_" + deviceName;
    }

    private void selenideAllureLogger() {
        SelenideLogger.addListener("AllureSelenideLogger", new AllureSelenide()
                .screenshots(false)
                .includeSelenideSteps(true)
                .savePageSource(false));
    }

    @AfterClass
    public static void closeDriver() {
        SelenideLogger.removeListener("AllureSelenideLogger");
        if (driver != null) {
            driver.quit();
            log.info("Quit!!!!");
        }
    }
}
