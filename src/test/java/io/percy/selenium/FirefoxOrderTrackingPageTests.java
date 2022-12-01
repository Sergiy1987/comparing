package io.percy.selenium;

import com.codeborne.selenide.Selenide;
import io.percy.selenium.data.BrowserName;
import io.percy.selenium.logger.TestResultLoggerExtension;
import io.percy.selenium.testBase.AbstractTestBase;
import io.qameta.allure.Step;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;

@ExtendWith({TestResultLoggerExtension.class})
public class FirefoxOrderTrackingPageTests extends AbstractTestBase {
    private TestInfo testInfo;

    @BeforeEach
    protected void init(TestInfo testInfo) {
        this.testInfo = testInfo;
    }

    private static Stream<Arguments> browserParameters() {
        return Stream.of(
                arguments(BrowserName.Firefox.name(), "Windows", "10", "latest", "1280x1024", ""),
                arguments(BrowserName.Firefox.name(), "OS X", "Ventura", "latest", "1280x1024", "")
               // arguments(BrowserName.Firefox.name(), "Android", "12.0", "", "", "Samsung Galaxy S22 Ultra")
        );
    }

    @ParameterizedTest(name = "Run {index}: {1} {2}, {0} {3}, {4}{5}")
    @MethodSource(value = "browserParameters")
    @DisplayName(value = "orderTrackingPageTest")
    @Step
    public void orderTrackingPageTest(String browserName, String platform, String platformVersion,
                                      String browserVersion, String screenResolution, String deviceName) {
        String screenshotName = AbstractTestBase.getScreenshotName(testInfo, platform, platformVersion, browserName, browserVersion, screenResolution, deviceName);
        AbstractTestBase.setUpDriver(browserName, platform, platformVersion, testInfo, browserVersion, screenResolution, deviceName);
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
}
