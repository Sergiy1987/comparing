package io.percy.selenium.flow;

import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import io.qameta.allure.Step;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.stream.IntStream;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.sleep;

public class SeleniumFlow {

    private static final String CLICK_ON_ELEMENT = "arguments[0].click();";
    private static final String SCROLL_TO_ELEMENT = "arguments[0].scrollIntoView(true);";
    private static final String SCROLL_ON_ELEMENT = "arguments[0].scrollBy(arguments[1], arguments[2])";
    private static final String SCROLL_TO_PAGE_TOP = "window.scrollTo(0,0);";
    private static final String SET_ELEMENT_VALUE = "arguments[0].setAttribute('value', arguments[1])";
    private static final String SET_ELEMENT_ATTRIBUTE = "arguments[0].setAttribute(arguments[1], arguments[2])";
    private static final String DOCUMENT_IS_READY = "return document.readyState == 'complete'";
    private static final String JQUERY_IS_READY = "return window.jQuery != undefined && jQuery.active === 0";

    @Deprecated
    protected void click(SelenideElement element) {
        click(element, false);
    }

    @SuppressWarnings(value = "unused")
    protected void scrollAndClick(SelenideElement element) {
        performScrollToElement(element);
        click(element, false);
    }

    public void click(SelenideElement element, boolean jsClick) {
        element.shouldBe(visible);
        if (jsClick) {
            performClickOnElement(element);
        } else {
            element.click();
        }
        waitForScriptsExecution();
    }

    @SuppressWarnings(value = "unused")
    protected void selectOptionByValue(SelenideElement element, String option) {
        element.shouldBe(visible).selectOptionByValue(option);
    }

    @SuppressWarnings(value = "unused")
    protected void selectOptionContainingText(SelenideElement element, String text) {
        element.shouldBe(visible).selectOptionContainingText(text);
    }

    @SuppressWarnings(value = "unused")
    protected void clearField(SelenideElement field) {
        field.shouldBe(visible).clear();
        waitForScriptsExecution();
    }

    @Step
    public void clearInputField(SelenideElement inputField) {
        String currentFieldValue;
        this.performClickOnElement(inputField);
        sleep(500);
        currentFieldValue = inputField.val();
        if (currentFieldValue.length() > 0) {
            IntStream.range(0, currentFieldValue.length()).forEach(i -> inputField.sendKeys(Keys.RIGHT));
            IntStream.range(0, currentFieldValue.length()).forEach(i -> inputField.sendKeys(Keys.BACK_SPACE));
        }
    }

    @Step
    public void actionSendKeys(SelenideElement element, String fieldValue) {
        new Actions(this.getWebDriver()).sendKeys(element, fieldValue).build().perform();
    }

    @Step
    public void contextClick(SelenideElement element) {
        new Actions(this.getWebDriver()).contextClick(element).build().perform();
    }

    public WebDriver getWebDriver() {
        return WebDriverRunner.webdriverContainer.getAndCheckWebDriver();
    }


    @SuppressWarnings(value = "unused")
    public void fillInField(SelenideElement field, String value) {
        if (StringUtils.isNotEmpty(value)) {
            field.shouldBe(visible).clear();
            field.sendKeys(value);
        }
    }

    @SuppressWarnings(value = "unused")
    protected void setFieldValue(SelenideElement field, String value) {
        if (StringUtils.isNotEmpty(value)) {
            field.shouldBe(visible).setValue(value);
        }
    }

    public JavascriptExecutor getJsExecutor() {
        return (JavascriptExecutor) this.getWebDriver();
    }

    public void performClickOnElement(WebElement element) {
        getJsExecutor().executeScript(CLICK_ON_ELEMENT, element);
    }

    protected void performScrollToElement(WebElement element) {
        this.getJsExecutor().executeScript(SCROLL_TO_ELEMENT, element);
        waitForScriptsExecution();
    }

    @SuppressWarnings(value = "unused")
    protected void performScrollOnElement(WebElement element, int x, int y) {
        this.getJsExecutor().executeScript(SCROLL_ON_ELEMENT, element, x, y);
        waitForScriptsExecution();
    }

    @SuppressWarnings(value = "unused")
    public void setElementValue(WebElement element, String value) {
        this.getJsExecutor().executeScript(SET_ELEMENT_VALUE, element, value);
        waitForScriptsExecution();
    }

    @SuppressWarnings(value = "unused")
    public void setElementAttribute(WebElement element, String attribute, String value) {
        this.getJsExecutor().executeScript(SET_ELEMENT_ATTRIBUTE, element, attribute, value);
        waitForScriptsExecution();
    }

    protected void waitForScriptsExecution() {
        waitForDocumentReadyState();
        //        waitForJqueryToBeZero();
    }

    @SuppressWarnings(value = "unused")
    protected void performScrollToPageTop() {
        this.getJsExecutor().executeScript(SCROLL_TO_PAGE_TOP);
        waitForScriptsExecution();
    }

    @Step
    public void waitForDocumentReadyState() {
        new WebDriverWait(this.getWebDriver(), Duration.ofSeconds(60)) // was 60
                .until(webDriver -> Boolean.valueOf(this.getJsExecutor().executeScript(DOCUMENT_IS_READY).toString()));
    }

    @SuppressWarnings(value = "unused")
    private void waitForJqueryToBeZero() {
        new WebDriverWait(this.getWebDriver(), Duration.ofSeconds(30)) // was 60
                .until(webDriver -> Boolean.valueOf(this.getJsExecutor().executeScript(JQUERY_IS_READY).toString()));
    }

    public SelenideElement getVisibleElement(List<SelenideElement> elements, String notFoundMessage) throws NoSuchElementException {
        return elements.stream().filter(SelenideElement::isDisplayed).findFirst().
                orElseThrow(() -> new NoSuchElementException(notFoundMessage));
    }
}
