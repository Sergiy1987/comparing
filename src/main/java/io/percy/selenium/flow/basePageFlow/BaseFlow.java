package io.percy.selenium.flow.basePageFlow;

import com.codeborne.selenide.Configuration;
import io.percy.selenium.annotations.ApplicationUnderTest;
import io.percy.selenium.flow.BrowserFlow;
import io.percy.selenium.elements.basePage.PageObject;

public class BaseFlow extends BrowserFlow {

    @Override
    public void open(Class<? extends PageObject> pageObject, boolean switchToUrl2, Object... params) {
        Configuration.baseUrl = getBaseUrl(pageObject);
        try {
            super.open(pageObject, switchToUrl2, params);
        } catch (org.openqa.selenium.TimeoutException e) {
            logger.error("Failed to load {} page once, retrying.", pageObject);
            refreshPage();
            super.open(pageObject, switchToUrl2, params);
        }
    }

    public String getBaseUrl(Class<? extends PageObject> pageObject) {
        return pageObject.getAnnotation(ApplicationUnderTest.class).app().getBaseUrl();
    }

    public String getFullUrl(Class<? extends PageObject> pageObject, boolean switchToUrl2, Object... params) {
        return getBaseUrl(pageObject) + String.format(getDefaultUrl(pageObject, switchToUrl2), params);
    }
}
