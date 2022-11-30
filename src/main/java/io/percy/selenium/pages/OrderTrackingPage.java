package io.percy.selenium.pages;

import io.percy.selenium.annotations.Flow;
import io.percy.selenium.elements.OrderTrackingElements;
import io.percy.selenium.elements.basePage.PageObject;
import io.percy.selenium.flow.basePageFlow.BaseFlow;
import io.percy.selenium.flow.basePageFlow.InitFlow;

import static com.codeborne.selenide.Selenide.page;

public class OrderTrackingPage extends InitFlow {
    @Flow BaseFlow baseFlow;

    public OrderTrackingPage openOrderTrackingPage(Class<? extends PageObject> pageObject) {
        baseFlow.open(pageObject, false);
        baseFlow.waitUntilPageIsLoaded(pageObject);
        return this;
    }

    public OrderTrackingPage setOrderIntoInputField(String order) {
        baseFlow.fillInField(page(OrderTrackingElements.class).getOrderInput(), order);
        return this;
    }

    public OrderTrackingPage clickOnOrderTrackingButton() {
        baseFlow.performClickOnElement(page(OrderTrackingElements.class).getTrackButton());
        return this;
    }


}
