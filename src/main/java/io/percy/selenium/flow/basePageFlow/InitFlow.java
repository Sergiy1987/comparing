package io.percy.selenium.flow.basePageFlow;

import io.percy.selenium.annotations.FlowInjections;

public class InitFlow {

    @SuppressWarnings("unused")
    public static void inject(Object aClass) {
        FlowInjections.inject(aClass);
    }

    public InitFlow() {
        FlowInjections.inject(this);
    }
}
