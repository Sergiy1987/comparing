package io.percy.selenium;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import io.percy.selenium.data.BrowserName;
import io.percy.selenium.logger.TestResultLoggerExtension;
import io.percy.selenium.testBase.TestBase1;
import io.percy.selenium.testBase.TestBaseTestNg;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;

@Slf4j
public class ChromeWindowsTestNgTests extends TestBaseTestNg {
    private Method method;
    //private final BrowserFlow browserFlow = new BrowserFlow();

    @BeforeMethod
    protected void init(Method method) {
        this.method = method;
    }

    @DataProvider(name = "browserParameters")
    public Object[][] browserParameters() {
        return new Object[][]{
                new Object[]{BrowserName.Chrome.name(), "latest", "Windows_10", "1280x1024", ""}
        };
    }


    @Step
    @Test(dataProvider = "browserParameters")
    @Parameters({"browserName", "browserVersion", "platformName", "screenResolution", "deviceName"})
    public void orderTrackingPageTest(String browserName, String browserVersion,
                                       String platformName, String screenResolution, String deviceName) {

        String screenshotName = TestBaseTestNg.getScreenshotName(browserName, browserVersion, platformName, screenResolution, deviceName, this.method);
        System.out.println("screenshotName = " + screenshotName);
        //TestBase1.setUpDriver(browserName, platformName, screenResolution);
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

    @Step
    @Test(dataProvider = "browserParameters")
    @Parameters({"browserName", "browserVersion", "platformName", "screenResolution", "deviceName"})
    public void orderTrackingPageTest1(String browserName, String browserVersion,
                                      String platformName, String screenResolution, String deviceName) {

        String screenshotName = TestBaseTestNg.getScreenshotName(browserName, browserVersion, platformName, screenResolution, deviceName, this.method);
        System.out.println("screenshotName = " + screenshotName);
        //TestBase1.setUpDriver(browserName, platformName, screenResolution);
        Selenide.open("https://www.browserstack.com/");
        Selenide.sleep(5000);
        //percy = new Percy(WebDriverRunner.getWebDriver());
        percy.snapshot(screenshotName+"1", Arrays.asList(1280, 768, 375), 1024, true);
//    atOrderTrackingPage()
//            .openOrderTrackingPage(OrderTrackingElements.class)
//            .setOrderIntoInputField("12")
//            .clickOnOrderTrackingButton();
//    Selenide.sleep(5000);
    }
}
