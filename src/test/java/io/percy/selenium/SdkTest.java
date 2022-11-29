package io.percy.selenium;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import io.percy.selenium.core.properties.PropertiesLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.stream.Stream;

@ExtendWith({TestResultLoggerExtension.class})
public class SdkTest extends AbstractTestBase {
  private TestInfo testInfo;
  private Percy percy;

  @BeforeEach
  protected void init(TestInfo testInfo) {
    this.testInfo = testInfo;
  }

  private static Stream<Arguments> arguments() {
    return Stream.of(
            Arguments.of(BrowserName.Chrome.name(), "Windows", "10", "latest", "1280x1024", "Chrome_latest_Windows_10_1280x1024")
//            Arguments.of(BrowserName.Chrome.name(), "Windows", "11", "latest", "1280x1024", "Chrome_latest_Windows_11_1280x1024"),
//            Arguments.of(BrowserName.Firefox.name(), "Windows", "10", "latest", "1280x1024", "Firefox_latest_Windows_10_1280x1024"),
//            Arguments.of(BrowserName.Safari.name(), "OS X", "Ventura", "latest", "1280x1024", "Safari_latest_Ventura_1280x1024"),
//            Arguments.of(BrowserName.Chrome.name(), "OS X", "Monterey", "latest", "1280x1024", "Chrome_latest_Monterey_1280x1024")
    );
  }


  @ParameterizedTest(name = "Run {index}: {1} {2}, {0}:{3}, {4}")
  @MethodSource(value = "arguments")
  public void takesLocalAppSnapshotWithProvidedName(String browserName, String platform,
                                                    String platformVersion, String browserVersion,
                                                    String screenResolution, String deviceName) throws MalformedURLException {
//    MutableCapabilities browserOptions = setBrowserOptions(browserName, browserVersion);
//    MutableCapabilities browserstackOptions = setBrowserStackOptions(platform, platformVersion, screenResolution);
//    browserOptions.setCapability("bstack:options", browserstackOptions);
//
//    URL url = new URL(TestResultLoggerExtension.URL);
//    driver = new RemoteWebDriver(url, browserOptions);
//    WebDriverRunner.setWebDriver(driver);
//    percy = new Percy(driver);

    AbstractTestBase.setUpDriver(browserName, platform, platformVersion, testInfo, browserVersion, screenResolution);
    Selenide.open("https://www.browserstack.com/");
    Selenide.sleep(5000);
    percy = new Percy(WebDriverRunner.getWebDriver());
    percy.snapshot(deviceName, Arrays.asList(1280, 768, 375), 1024, true);
    //driver.quit();
  }

  @Test
  public void test() {
    PropertiesLoader.loadProperties();
  }

//  private MutableCapabilities setBrowserStackOptions(String platform, String platformVersion,
//                                                     String screenResolution) {
//    MutableCapabilities browserstackOptions = new MutableCapabilities();
//    browserstackOptions.setCapability("os", platform);
//    browserstackOptions.setCapability("osVersion", platformVersion);
//    browserstackOptions.setCapability("projectName", testInfo.getDisplayName());
//    browserstackOptions.setCapability("buildName", buildName);
//    browserstackOptions.setCapability("sessionName", "Home page must have a title");
//    browserstackOptions.setCapability("resolution", screenResolution);
//    browserstackOptions.setCapability("local", "false");
//    browserstackOptions.setCapability("debug", "true");
//    browserstackOptions.setCapability("consoleLogs", "info");
//    browserstackOptions.setCapability("networkLogs", "true");
//    browserstackOptions.setCapability("video", "false");
//    browserstackOptions.setCapability("seleniumVersion", "4.1.0");
//    browserstackOptions.setCapability("telemetryLogs", "true");
//    return browserstackOptions;
//  }

//  private MutableCapabilities setBrowserOptions(String browserName, String browserVersion) {
//    MutableCapabilities browserOptions = new MutableCapabilities();
//    browserOptions.setCapability(CapabilityType.BROWSER_NAME, browserName);
//    browserOptions.setCapability(CapabilityType.BROWSER_VERSION, browserVersion);
//    return browserOptions;
//  }
}
