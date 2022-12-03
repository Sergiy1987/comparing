package io.percy.selenium;

import io.percy.selenium.listener.TestResultLoggerExtension;

public class VisualMobilePlatformTests extends TestResultLoggerExtension {
/*
    @Parameterized.Parameter
    public String browserName;
    @Parameterized.Parameter(1)
    public String platform;
    @Parameterized.Parameter(2)
    public String platformVersion;
    @Parameterized.Parameter(3)
    public String deviceName;

    @Parameterized.Parameters(name = "Run {index}: {1} {2}, {0}, {3}" )
    public static Collection<Object[]> crossBrowserData() {
        return Arrays.asList(new Object[][] {
                {"Safari", "iOS", "16", "iPhone 14 Pro Max"},
//                {"Safari", "iOS", "15", "iPhone 13 Pro Max"},
//                {"Safari", "iOS", "13", "iPhone 11 Pro Max"},
//                {"Safari", "iOS", "12", "iPhone XS Max"},
//                {"Chrome", "Android", "12.0", "Samsung Galaxy S22 Ultra"},
//                {"Chrome", "Android", "12.0", "Samsung Galaxy S22 Plus"}
        });
    }

    @Before
    public void setUp() throws MalformedURLException {
        MutableCapabilities capabilities = new MutableCapabilities();
        capabilities.setCapability("browserName", browserName);
        capabilities.setCapability("platformName", platform);

        MutableCapabilities browserstackOptions = new MutableCapabilities();
        browserstackOptions.setCapability("osVersion", platformVersion);
        browserstackOptions.setCapability("deviceName", deviceName);
        browserstackOptions.setCapability("projectName", testName.getMethodName());
        browserstackOptions.setCapability("buildName", buildName);
        browserstackOptions.setCapability("sessionName", "Home page must have a title");//login  test
        browserstackOptions.setCapability("local", "false");
        browserstackOptions.setCapability("debug", "true");
        browserstackOptions.setCapability("consoleLogs", "info");
        browserstackOptions.setCapability("networkLogs", "true");
        browserstackOptions.setCapability("video", "false");
        browserstackOptions.setCapability("seleniumVersion", "4.1.0");
        browserstackOptions.setCapability("telemetryLogs", "true");
        capabilities.setCapability("bstack:options", browserstackOptions);

        URL url = new URL(AbstractTestBase.URL);
        driver = new RemoteWebDriver(url, capabilities);
    }

    //@Test
    public void crossMobilePlatformTest() {
        driver.get("https://www.browserstack.com/");
    }

*/
}
