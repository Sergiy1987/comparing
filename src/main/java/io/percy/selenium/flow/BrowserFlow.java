package io.percy.selenium.flow;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import com.google.common.collect.Lists;
import io.percy.selenium.annotations.ApplicationUnderTest;
import io.percy.selenium.annotations.DefaultUrl;
import io.percy.selenium.elements.basePage.PageObject;
import io.qameta.allure.Step;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.html5.WebStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class BrowserFlow extends SeleniumFlow {

    protected static Logger logger = LoggerFactory.getLogger(BrowserFlow.class);

    public String getCurrentUrl() {
        return WebDriverRunner.url();
    }

    @Step
    public void open(Class<? extends PageObject> pageObject, boolean switchToUrl2, Object... params) {
        logger.info("Open {}", pageObject.getSimpleName().replace("Elements", "Page"));
        Selenide.open(String.format(getFullUrl(pageObject, switchToUrl2), params), pageObject);
    }

    protected String getDefaultUrl(Class<? extends PageObject> pageObject, boolean switchToUrl2) {
        if (switchToUrl2) {
            return pageObject.getAnnotation(DefaultUrl.class).url2();
        } else {
            return pageObject.getAnnotation(DefaultUrl.class).url();
        }
    }

    public String getBaseUrl(Class<? extends PageObject> pageObject) {
        return pageObject.getAnnotation(ApplicationUnderTest.class).app().getBaseUrl();
    }

    public String getFullUrl(Class<? extends PageObject> pageObject, boolean switchToUrl2, Object... params) {
        return getBaseUrl(pageObject) + String.format(getDefaultUrl(pageObject, switchToUrl2), params);
    }

    @Step
    public void waitUntilPageIsLoaded(Class<? extends PageObject> pageObject) {
        Selenide.page(pageObject).waitUntilLoaded();
        logger.info("{} is loaded", pageObject.getName().replace("pages.", ""));
        //waitForScriptsExecution();
    }

    @SuppressWarnings(value = "unused")
    public void takeScreenShot(String fileName) {
        Selenide.screenshot(fileName);
    }

    public void refreshPage() {
        Selenide.refresh();
        waitForScriptsExecution();
    }

    @SuppressWarnings(value = "unused")
    public void navigateBack() {
        Selenide.back();
    }

    public void clearBrowserLocalStorage() {
        WebDriver driver = getWebDriver();
        if (driver instanceof WebStorage) {
            ((WebStorage) driver).getSessionStorage().clear();
            ((WebStorage) driver).getLocalStorage().clear();
        }
    }

    public void clearBrowserCache() {
        WebDriverRunner.webdriverContainer.clearBrowserCache();
    }

    @SuppressWarnings(value = "unused")
    public void addToLocalStorage(String item, String value) {
        WebDriver driver = getWebDriver();
        if (driver instanceof WebStorage) {
            ((WebStorage) driver).getLocalStorage().setItem(item, value);
        }
    }

    @Step
    public void switchToNewTab(int tabNumber) {
        WebDriver driver = getWebDriver();
        ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
        driver.switchTo().window(tabs.get(tabs.size() - tabNumber));
    }

    @Step
    public void switchToFrame(WebElement element) {
        WebDriver driver = getWebDriver();
        driver.switchTo().frame(element);
    }

    @Step
    public void switchToFrame(String frameName) {
        WebDriver driver = getWebDriver();
        driver.switchTo().frame(frameName);
    }

    @Step
    public void switchToWindow(String tabName) {
        Selenide.switchTo().window(tabName);
    }

    @Step
    public void closeAllTabsExceptMain(String excludeTabTitle) {
        WebDriver driver = getWebDriver();
        ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
        AtomicReference<String> mainTab = new AtomicReference<>("");
        tabs.stream()
                .filter(tab -> tabs.size() > 1)
                .forEach(tab -> {
                    driver.switchTo().window(tab);
                    String tabTitle = driver.getTitle();
                    if (!tabTitle.equals(excludeTabTitle)) driver.close();
                    else mainTab.set(tab);
                });
        if (!mainTab.get().equals("")) driver.switchTo().window(mainTab.get());
    }

    @Step
    public boolean verifyBrowserTabIsOpened(String tabToClose, String tabToSwitch) {
        WebDriver driver = getWebDriver();
        ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
        AtomicBoolean tabIsOpened = new AtomicBoolean(false);
        AtomicReference<String> mainTab = new AtomicReference<>("");
        tabs.forEach(tab -> {
            driver.switchTo().window(tab);
            String tabTitle = driver.getTitle();
            if (tabTitle.equals(tabToClose)) tabIsOpened.set(true);
            else if (tabTitle.equals(tabToSwitch)) mainTab.set(tab);
        });
        if (mainTab.get().equals(tabToSwitch)) driver.switchTo().window(mainTab.get());
        return tabIsOpened.get();
    }

    @SuppressWarnings(value = "unused")
    public void addCookies(List<Cookie> cookies) {
        cookies.forEach(cookie -> getWebDriver().manage().addCookie(cookie));
    }

    @SuppressWarnings(value = "unused")
    public void deleteBrowserCookies(List<String> cookies) {
        cookies.forEach(cookie -> getWebDriver().manage().deleteCookieNamed(cookie));
    }

    public void deleteBrowserCookies() {
        getCookies().forEach(cookie -> getWebDriver().manage().deleteCookie(cookie));
    }

    public List<Cookie> getCookies() {
        return Lists.newArrayList(getWebDriver().manage().getCookies());
    }

    public String getPageTitle() {
        return Selenide.title();
    }
}
