package io.percy.selenium.elements.basePage;

import com.codeborne.selenide.WebDriverRunner;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public abstract class PageObject {

    public void waitUntilLoaded() {
        new WebDriverWait(WebDriverRunner.getWebDriver(), Duration.ofSeconds(60))
                .until(webDriver -> isLoadedCondition());
    }

    protected abstract boolean isLoadedCondition();
}
