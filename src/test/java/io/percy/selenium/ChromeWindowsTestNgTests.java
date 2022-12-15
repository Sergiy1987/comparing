package io.percy.selenium;

import com.codeborne.selenide.Selenide;
import io.percy.selenium.data.BrowserName;
import io.percy.selenium.listener.BrowserStackTestStatusListener;
import io.percy.selenium.testBase.TestBaseTestNg;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.util.Arrays;

@Slf4j
@Listeners({BrowserStackTestStatusListener.class})
public class ChromeWindowsTestNgTests extends TestBaseTestNg {
    private Method method;
    //private final BrowserFlow browserFlow = new BrowserFlow();

    @BeforeMethod
    protected void init(Method method) {
        this.method = method;
    }

    @DataProvider(name = "browserParameters")
    public Object[][] browserParameters() {
        return new Object[][] {
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
        //Selenide.sleep(5000);
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
        //Selenide.sleep(5000);
        //percy = new Percy(WebDriverRunner.getWebDriver());
        percy.snapshot(screenshotName+"1", Arrays.asList(1280, 768, 375), 1024, true);
//    atOrderTrackingPage()
//            .openOrderTrackingPage(OrderTrackingElements.class)
//            .setOrderIntoInputField("12")
//            .clickOnOrderTrackingButton();
//    Selenide.sleep(5000);
    }
}
