package io.percy.selenium.core;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class Application {
    private static final Map<Class<?>, Object> objectHolder = new HashMap<>();

    @SuppressWarnings(value = {"unchecked", "all"})
    private synchronized static <T> T returnSingleton(T targetPage) {
        Object instance = objectHolder.get(targetPage.getClass());
        if (instance == null) {
            synchronized (targetPage.getClass().toString()) {
                if (instance == null) {
                    try {
                        instance = targetPage.getClass().newInstance();
                        objectHolder.put(targetPage.getClass(), instance);
                    } catch (Exception e) {
                        log.error(e.getMessage());
                    }
                }
            }
        }
        //log.info("instance = {}", instance);
        return (T) objectHolder.get(targetPage.getClass());
    }
}
