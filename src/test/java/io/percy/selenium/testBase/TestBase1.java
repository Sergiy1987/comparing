package io.percy.selenium.testBase;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.percy.selenium.Percy;
import io.percy.selenium.core.properties.Properties;
import io.percy.selenium.core.properties.PropertiesLoader;
import io.qameta.allure.Step;
import io.qameta.allure.selenide.AllureSelenide;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import utils.ConvertUtils;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

@Slf4j
public class TestBase1 {
    private static RemoteWebDriver driver;
    protected static Percy percy;
    //private static final String buildName = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

    @Step
    //@MethodSource(value = "browserParameters")
    //@BeforeAll
    public static void setUpDriver(String browserName,
                                   String platformName,
                                   String screenResolution) throws IOException, ParseException {
                                   //TestInfo testInfo)
                                   //String deviceName)

        PropertiesLoader.loadProperties();
        selenideAllureLogger();
        String hubUrl = System.getProperty(Properties.BROWSER_STACK_USER_NAME) + ":" +
                System.getProperty(Properties.BROWSER_STACK_API_KEY) + System.getProperty(Properties.BROWSER_STACK_HUB_URL);
        System.out.println("platformName = " + platformName + ", browserName = " + browserName);

        if (platformName.contains("Windows") | platformName.contains("OS X")) {
            JSONParser parser = new JSONParser();
            JSONObject fullConfig = (JSONObject) parser.parse(new FileReader("capabilities.json"));
            Map<String, Object> browser = ConvertUtils.getJsonObject((JSONObject) fullConfig.get(browserName));//parameter
            Map<String, Object> platform = ConvertUtils.getJsonObject((JSONObject) fullConfig.get(platformName));//parameter
            Map<String, Object> browserStackCaps = ConvertUtils.getJsonObject((JSONObject) fullConfig.get("bstack:options_web"));
            browserStackCaps.put("os", platform.get("os"));//no
            browserStackCaps.put("osVersion", platform.get("osVersion"));//no
            browserStackCaps.put("resolution", screenResolution);//yes
            //browserStackCaps.put("projectName", testInfo.getDisplayName());
            browserStackCaps.put("buildName", "Visibility_testing_" + platformName + "_" + browserName);
            browserStackCaps.put("sessionName",  "Visibility_testing_" + platformName + "_" + browserName);

            MutableCapabilities browserOptions = ConvertUtils.mapToCapabilities(browser);
            MutableCapabilities browserstackOptions = ConvertUtils.mapToCapabilities(browserStackCaps);

            browserOptions.setCapability("bstack:options", browserstackOptions);
            setConnectionForBrowserStack(hubUrl, browserOptions);
        } else {
//            MutableCapabilities mobileOptions = setMobileOptions(browserName, platform);
//            MutableCapabilities browserstackOptions = setBrowserStackMobileOptions(platform, platformVersion, testInfo, browserName, deviceName);
//            mobileOptions.setCapability("bstack:options", browserstackOptions);
//            setConnectionForBrowserStack(hubUrl, mobileOptions);
        }
        Configuration.timeout = 10000;
        WebDriverRunner.setWebDriver(driver);
        //percy = new Percy(WebDriverRunner.getWebDriver());
        //Selenide.open(System.getProperty(Properties.STAGE_OT_URL));
    }

    private static void setConnectionForBrowserStack(String hubUrl, MutableCapabilities deviceOptions) {
        try {
            driver = new RemoteWebDriver(new URL(hubUrl), deviceOptions);
        } catch (MalformedURLException e) {
            log.error(e.getMessage());
        }
    }

    public static String getScreenshotName(String browserName, String platform,
                                           String screenResolution, String deviceName, TestInfo testInfo) {
        String testName = testInfo.getTestMethod().map(Method::getName).map(String::toString).orElse("");
        String platformName = platform.replace(" ", "_");
        return StringUtils.isEmpty(deviceName) ?
                testName + "_" + platformName + "_" + browserName + "_" + screenResolution :
                testName + "_" + platformName + "_" + browserName + "_" + deviceName;
    }

    private static void selenideAllureLogger() {
        SelenideLogger.addListener("AllureSelenideLogger", new AllureSelenide()
                .screenshots(false)
                .includeSelenideSteps(true)
                .savePageSource(false));
    }

    @AfterAll
    public static void closeDriver() {
        SelenideLogger.removeListener("AllureSelenideLogger");
        if (driver != null | WebDriverRunner.webdriverContainer.hasWebDriverStarted()) {
            driver.quit();
            log.info("Quit!!!!");
        }
    }
}
