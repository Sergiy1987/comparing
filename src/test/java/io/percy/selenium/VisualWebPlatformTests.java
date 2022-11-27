package io.percy.selenium;
/*
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

public class VisualWebPlatformTests extends AbstractTestBase {

    private static Percy percy;

    @Parameterized.Parameters(name = "Run {index}: {1} {2}, {0}:{3}, {4}" )
    public static Collection<Object[]> crossBrowserData() {
        return Arrays.asList(new Object[][] {
                { "Chrome", "Windows", "10", "latest", "1280x1024", "Chrome_latest_Windows_10_1280x1024" },
//                { "Chrome", "Windows", "11", "latest", "1280x1024", "Chrome_latest_Windows_11_1280x1024" },
//                { "Firefox", "Windows", "10", "latest", "1280x1024", "Firefox_latest_Windows_10_1280x1024" },
//                { "Safari", "OS X", "Ventura", "latest", "1280x1024", "Safari_latest_Ventura_1280x1024" },
//                { "Chrome", "OS X", "Monterey", "latest", "1280x1024", "Chrome_latest_Monterey_1280x1024" }
        });//   {"Safari", "iOS", "16", "iPhone 14 Pro Max"},
    }

    @After
    public static void testTeardown() {
        TestServer.shutdown();
    }

    @Before
    public void setUp() throws IOException {
        MutableCapabilities browserOptions = new MutableCapabilities();
        browserOptions.setCapability(CapabilityType.BROWSER_NAME, browserName);
        browserOptions.setCapability(CapabilityType.BROWSER_VERSION, browserVersion);
        //browserOptions.setCapability(CapabilityType.PLATFORM_NAME, platform);

        MutableCapabilities browserstackOptions = new MutableCapabilities();
        browserstackOptions.setCapability("os", platform);
        browserstackOptions.setCapability("osVersion", platformVersion);
        browserstackOptions.setCapability("projectName", testName.getMethodName());
        browserstackOptions.setCapability("buildName", buildName);
        browserstackOptions.setCapability("sessionName", "Home page must have a title");
        browserstackOptions.setCapability("resolution", screenResolution);
        browserstackOptions.setCapability("local", "false");
        browserstackOptions.setCapability("debug", "true");
        browserstackOptions.setCapability("consoleLogs", "info");
        browserstackOptions.setCapability("networkLogs", "true");
        browserstackOptions.setCapability("video", "false");
        browserstackOptions.setCapability("seleniumVersion", "4.1.0");
        browserstackOptions.setCapability("telemetryLogs", "true");
        browserOptions.setCapability("bstack:options", browserstackOptions);
        System.out.println("browserOptions = " + browserOptions.toJson());


       // MutableCapabilities visualOptions = new MutableCapabilities();
        //visualOptions.setCapability("projectName", "BrowserStack Demo Java");
        //visualOptions.setCapability("viewportSize", screenResolution);
       // visualOptions.setCapability("failOnNewStates", false);

       // browserOptions.setCapability("sauce:visual", visualOptions);
        TestServer.startServer();
        URL url = new URL(AbstractTestBase.URL);
        driver = new RemoteWebDriver(url, browserOptions);
        ProcessBuilder processBuilder = new ProcessBuilder();
        Map<String, String> environment = processBuilder.environment();
        environment.forEach((key, value) -> System.out.println(key + " --> " + value));

        processBuilder.command("/usr/bin/env", "echo $PERCY_TOKEN");
        Process process = processBuilder.start();
        //process.destroy();
        System.out.println("\n\n\n");
        environment.forEach((key, value) -> System.out.println(key + " --> " + value));
        driver.setFileDetector(new LocalFileDetector());
        percy = new Percy(driver);
        WebDriverRunner.setWebDriver(driver);
    }

    @Test
    public void crossWebPlatformTest() {
        Selenide.open("https://www.browserstack.com/");
        percy.snapshot(deviceName);
    }
}*/
