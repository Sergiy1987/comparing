package io.percy.selenium.annotations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public final class FlowInjections {
    private static final Logger logger = LoggerFactory.getLogger(FlowInjections.class);

    private FlowInjections() {
    }

    public static void inject(Object aClass) {
        Set<Field> fields = getAnnotatedClassFields(aClass.getClass(), Flow.class);
        fields.stream()
                .filter(field -> !isInstantiated(field, aClass))
               //.findFirst()
                .forEach(field -> setValue(field, aClass, initResource(field.getType())));
    }

    private static Set<Field> getClassFields(Class<?> targetClass) {
        Set<Field> fields = new HashSet<>();
        fields.addAll(Arrays.asList(targetClass.getDeclaredFields()));
        fields.addAll(Arrays.asList(targetClass.getFields()));
        if (targetClass != Object.class) {
            fields.addAll(getClassFields(targetClass.getSuperclass()));
        }
        return fields;
    }

    @SuppressWarnings(value = "SameParameterValue")
    private static Set<Field> getAnnotatedClassFields(Class<?> targetClass, Class<? extends Annotation> annotationClass) {
        return getClassFields(targetClass).stream()
                .filter(field -> field.isAnnotationPresent(annotationClass) & field.getAnnotation(annotationClass) != null)
                .collect(Collectors.toSet());
    }

    private static boolean isInstantiated(Field field, Object target) {
        field.setAccessible(true);
        Object value = null;
        try {
            value = field.get(target);
        } catch (IllegalAccessException e) {
            logger.error("No access to field {}", field, e);
        }
        return value != null;
    }

    private static void setValue(Field field, Object target, Object value) {
        field.setAccessible(true);
        try {
            field.set(target, value);
        } catch (IllegalAccessException e) {
            logger.error("Cannot set value to field {}", field, e);
        }
    }

    private static Object initResource(Class<?> resourceClass) {
        try {
            return resourceClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            logger.error("Unable to create instance of {}", resourceClass, e);
            throw new IllegalStateException("Unable to create instance of " + resourceClass);
        }
    }

}
