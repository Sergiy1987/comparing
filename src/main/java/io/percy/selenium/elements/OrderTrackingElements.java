package io.percy.selenium.elements;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import io.percy.selenium.annotations.ApplicationUnderTest;
import io.percy.selenium.annotations.DefaultUrl;
import io.percy.selenium.elements.basePage.PageObject;
import lombok.Getter;
import org.openqa.selenium.support.FindBy;
import static io.percy.selenium.annotations.ApplicationType.STAGING_OT;

@Getter
//@AllArgsConstructor
@ApplicationUnderTest(app = STAGING_OT)
@DefaultUrl(url = "/", url2 = "/%s")
public class OrderTrackingElements extends PageObject {

    @Override
    protected boolean isLoadedCondition() {
        return orderInput.is(Condition.visible) & trackButton.is(Condition.visible);
    }

    @FindBy(css = "input[type='text']")
    private SelenideElement orderInput;

    @FindBy(css = "button[type='submit']")
    private SelenideElement trackButton;
}
