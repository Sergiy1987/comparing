package io.percy.selenium.core.properties;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class Properties { //final == from this class we can't to extends
    //@UtilityClass //deny to create the object of this class

    public static final String STAGE_OT_URL = "stage.ot.url";
    public static final String BROWSER_STACK_USER_NAME = "browser.stack.user.name";
    public static final String BROWSER_STACK_API_KEY = "browser.stack.api.key";
    public static final String BROWSER_STACK_HUB_URL = "browser.stack.hub.url";
    public static final String PERCY_TOKEN = "percy.token";
}
