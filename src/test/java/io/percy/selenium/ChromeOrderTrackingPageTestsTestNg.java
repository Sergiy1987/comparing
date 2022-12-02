package io.percy.selenium;

import com.codeborne.selenide.Selenide;
import io.percy.selenium.data.BrowserName;
import io.percy.selenium.testBase.TestNgTestBase;
import io.qameta.allure.Step;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.Arrays;

public class ChromeOrderTrackingPageTestsTestNg extends TestNgTestBase {
    private Method method;
    //private final BrowserFlow browserFlow = new BrowserFlow();

    @BeforeMethod
    protected void init(Method method) {
        this.method = method;
    }

    @DataProvider(name = "crossBrowserData")
    public static Object[][] crossBrowserData() {
        return new Object[][] {
                new Object[] { BrowserName.Chrome.name(), "Windows", "10", "latest", "1280x1024", "" },
//                new Object[] { BrowserName.Chrome.name(), "OS X", "Ventura", "latest", "1280x1024", "" },
//                new Object[] { BrowserName.Chrome.name(), "Android", "12.0", "", "", "Samsung Galaxy S22 Ultra" }
        };
    }

//    @ParameterizedTest(name = "Run {index}: {1} {2}, {0} {3}, {4}{5}")
//    @MethodSource(value = "browserParameters")
//    @DisplayName(value = "orderTrackingPageTest")
    @Step
    @Test//(dataProvider = "crossBrowserData")
//    @Parameters(value = {"browserName", "platform", "platformVersion", "browserVersion",
//            "screenResolution", "deviceName"})
    public void orderTrackingPageTest() {//(String browserName, String platform, String platformVersion,
                                      //String browserVersion, String screenResolution, String deviceName) {

        String screenshotName = "orderTrackingPageTest_Windows_10_Chrome_latest_1280x1024";
                //TestNgTestBase.getScreenshotName(method, platform, platformVersion, browserName, browserVersion, screenResolution, deviceName);
        //TestBase.setUpDriver(browserName, platform, platformVersion, method, browserVersion, screenResolution, deviceName);
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
//    public static void setEnv(String key, String value) {
//        try {
//            Map<String, String> env = System.getenv();
//            Class<?> cl = env.getClass();
//            Field field = cl.getDeclaredField("m");
//            field.setAccessible(true);
//            Map<String, String> writableEnv = (Map<String, String>) field.get(env);
//            writableEnv.put(key, value);
//        } catch (Exception e) {
//            throw new IllegalStateException("Failed to set environment variable", e);
//        }
//    }
}
