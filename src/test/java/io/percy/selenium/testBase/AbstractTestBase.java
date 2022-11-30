package io.percy.selenium.testBase;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import io.percy.selenium.Percy;
import io.percy.selenium.core.properties.Properties;
import io.percy.selenium.core.properties.PropertiesLoader;
import io.percy.selenium.flow.basePageFlow.InitFlow;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.TestInfo;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Slf4j
public class AbstractTestBase {
    protected static RemoteWebDriver driver;
    protected static Percy percy;

    private static final String buildName = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

    public static void setUpDriver(String browserName, String platform,
                                   String platformVersion, TestInfo testInfo,
                                   String browserVersion, String screenResolution) {
        PropertiesLoader.loadProperties();
        setEnv("PERCY_TOKEN", "daa6c215ff1261f935236c4084944d7dd076d48392588c0d34210791d1d51223");
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
        browserstackOptions.setCapability("localProxyHost", "127.0.0.1");
        browserstackOptions.setCapability("localProxyPort", "8080");
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

    public static void setEnv(String key, String value) {
        try {
            Map<String, String> env = System.getenv();
            Class<?> cl = env.getClass();
            Field field = cl.getDeclaredField("m");
            field.setAccessible(true);
            Map<String, String> writableEnv = (Map<String, String>) field.get(env);
            writableEnv.put(key, value);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to set environment variable", e);
        }
    }

}
