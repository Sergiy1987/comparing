package io.percy.selenium.testBase;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import io.percy.selenium.Percy;
import io.percy.selenium.core.properties.Properties;
import io.percy.selenium.core.properties.PropertiesLoader;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.TestInfo;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
public class AbstractTestBase {
    protected static RemoteWebDriver driver;
    protected static Percy percy;
    private static final String buildName = System.getenv("JENKINS_LABEL");
    //private static final String buildName = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

    public static void setUpDriver(String browserName, String platform,
                                   String platformVersion, TestInfo testInfo,
                                   String browserVersion, String screenResolution) {
        PropertiesLoader.loadProperties();
        MutableCapabilities browserOptions = setBrowserOptions(browserName, browserVersion);
        MutableCapabilities browserstackOptions = setBrowserStackOptions(platform, platformVersion, testInfo, screenResolution);
        browserOptions.setCapability("bstack:options", browserstackOptions);
        System.out.println("browserName = " + browserName);
        System.out.println("browserOptions = " + browserOptions);

        String hubUrl = System.getProperty(Properties.BROWSER_STACK_USER_NAME) + ":" +
                System.getProperty(Properties.BROWSER_STACK_API_KEY) + System.getProperty(Properties.BROWSER_STACK_HUB_URL);
        Configuration.baseUrl = System.getProperty(Properties.STAGE_OT_URL);
        Configuration.timeout = 10000;
        try {
            driver = new RemoteWebDriver(new URL(hubUrl), browserOptions);
            //new ChromeDriver((ChromeOptions) browserOptions);
        } catch (MalformedURLException e) {
           log.error(e.getMessage());
        }
        WebDriverRunner.setWebDriver(driver);
        percy = new Percy(driver);//WebDriverRunner.getAndCheckWebDriver()
        //Selenide.open(System.getProperty(Properties.STAGE_OT_URL));
    }


    private static MutableCapabilities setBrowserStackOptions(String platform, String platformVersion,
                                                             TestInfo testInfo, String screenResolution) {
        MutableCapabilities browserstackOptions = new MutableCapabilities();
        browserstackOptions.setCapability("os", platform);
        browserstackOptions.setCapability("osVersion", platformVersion);
        browserstackOptions.setCapability("projectName", testInfo.getDisplayName());
        browserstackOptions.setCapability("buildName", "Visibility_testing");//buildName
        browserstackOptions.setCapability("sessionName", "BStack Build Name: " + buildName);
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
