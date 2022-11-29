package io.percy.selenium;

import com.codeborne.selenide.WebDriverRunner;
import io.percy.selenium.core.properties.Properties;
import io.percy.selenium.core.properties.PropertiesLoader;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.TestInfo;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class AbstractTestBase {
    protected static RemoteWebDriver driver;
    protected static Percy percy;

    private static final String buildName = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

    public static void setUpDriver(String browserName, String platform,
                                   String platformVersion, TestInfo testInfo,
                                   String browserVersion, String screenResolution) throws MalformedURLException {
        PropertiesLoader.loadProperties();

        MutableCapabilities browserOptions = setBrowserOptions(browserName, browserVersion);
        MutableCapabilities browserstackOptions = setBrowserStackOptions(platform, platformVersion, testInfo, screenResolution);
        browserOptions.setCapability("bstack:options", browserstackOptions);
        System.out.println("browserName = " + browserName);
        System.out.println("browserOptions = " + browserOptions);

        String hubUrl = System.getProperty(Properties.BROWSER_STACK_USER_NAME) + ":" +
                System.getProperty(Properties.BROWSER_STACK_API_KEY) + System.getProperty(Properties.BROWSER_STACK_HUB_URL);

        driver = new RemoteWebDriver(new URL(hubUrl), browserOptions);
        WebDriverRunner.setWebDriver(driver);
        percy = new Percy(WebDriverRunner.getAndCheckWebDriver());
    }


    private static MutableCapabilities setBrowserStackOptions(String platform, String platformVersion,
                                                             TestInfo testInfo, String screenResolution) {
        MutableCapabilities browserstackOptions = new MutableCapabilities();
        browserstackOptions.setCapability("os", platform);
        browserstackOptions.setCapability("osVersion", platformVersion);
        browserstackOptions.setCapability("projectName", testInfo.getDisplayName());
        browserstackOptions.setCapability("buildName", buildName);
        browserstackOptions.setCapability("sessionName", "");
        browserstackOptions.setCapability("resolution", screenResolution);
        browserstackOptions.setCapability("local", "false");
        browserstackOptions.setCapability("debug", "true");
        browserstackOptions.setCapability("consoleLogs", "info");
        browserstackOptions.setCapability("networkLogs", "true");
        browserstackOptions.setCapability("video", "false");
        browserstackOptions.setCapability("seleniumVersion", "4.1.0");
        browserstackOptions.setCapability("telemetryLogs", "true");
        return browserstackOptions;
    }

    private static MutableCapabilities setBrowserOptions(String browserName, String browserVersion) {
        MutableCapabilities browserOptions = new MutableCapabilities();
        browserOptions.setCapability(CapabilityType.BROWSER_NAME, browserName);
        browserOptions.setCapability(CapabilityType.BROWSER_VERSION, browserVersion);
        return browserOptions;
    }

    @AfterAll
    public static void closeDriver() {
        WebDriverRunner.getWebDriver().quit();
    }

}
