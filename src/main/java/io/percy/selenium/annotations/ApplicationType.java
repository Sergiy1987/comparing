package io.percy.selenium.annotations;

import io.percy.selenium.core.properties.Properties;
import lombok.Getter;

public enum ApplicationType {
    STAGING_OT(System.getProperty(Properties.STAGE_OT_URL));

    @Getter
    private final String baseUrl;

    ApplicationType(String baseUrl) {
        this.baseUrl = baseUrl;
    }
}

