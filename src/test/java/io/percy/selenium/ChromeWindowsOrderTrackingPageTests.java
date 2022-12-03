package io.percy.selenium;

import com.codeborne.selenide.Selenide;
import io.percy.selenium.data.BrowserName;
import io.percy.selenium.listener.TestResultLoggerExtension;
import io.percy.selenium.testBase.TestBase1;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;

@Slf4j
@ExtendWith({TestResultLoggerExtension.class})
public class ChromeWindowsOrderTrackingPageTests extends TestBase1 {
    private TestInfo testInfo;
    //private final BrowserFlow browserFlow = new BrowserFlow();

    @BeforeEach
    protected void init(TestInfo testInfo) {
        this.testInfo = testInfo;
    }

    private static Stream<Arguments> browserParameters() {
        return Stream.of(
                arguments(BrowserName.Chrome.name(), "Windows_10", "1280x1024", "")
                //arguments(BrowserName.Chrome.name(), "Android", "12.0", "", "", "Samsung Galaxy S22 Ultra")
        );
    }

    //@ParameterizedTest(name = "Run {index}: {1} {0}, {2}, {3}")
    @MethodSource(value = "browserParameters")
    @DisplayName(value = "orderTrackingPageTest")
    @Step
    public void orderTrackingPageTest(String browserName,
                                       String platformName, String screenResolution) throws IOException, ParseException {

        String screenshotName = TestBase1.getScreenshotName(browserName, platformName, screenResolution, "", testInfo);
        System.out.println("screenshotName = " + screenshotName);
        TestBase1.setUpDriver(browserName, platformName, screenResolution);
        Selenide.open("https://www.browserstack.com/");
        Selenide.sleep(5000);
        //percy = new Percy(WebDriverRunner.getWebDriver());
        //percy.snapshot(screenshotName, Arrays.asList(1280, 768, 375), 1024, true);
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
