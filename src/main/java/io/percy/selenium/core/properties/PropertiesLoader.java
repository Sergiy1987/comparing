package io.percy.selenium.core.properties;

import lombok.experimental.UtilityClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@UtilityClass
public final class PropertiesLoader {

    private static final Logger logger = LoggerFactory.getLogger(PropertiesLoader.class);

    public static void loadProperties() {
        Properties sysProperties = readPropertiesFromFile();
        saveToSystemProperties(sysProperties);
        logger.debug("Properties loaded: " + sysProperties);
    }

    private static Properties readPropertiesFromFile() {
        Properties properties = new Properties();
        try (InputStream inputStream = ClassLoader.getSystemResourceAsStream("core/properties/stage.properties")) {
            properties.load(inputStream);
        } catch (IOException e) {
            logger.error("Unable to read properties from file", e);
        }
        return properties;
    }

    private static void saveToSystemProperties(Properties properties) {
        properties.stringPropertyNames().forEach(propertyName -> {
            if (!System.getProperties().containsKey(propertyName)) {
                logger.info("PropertyName: {}, PropertyValue: {}", propertyName, properties.getProperty(propertyName));
                System.setProperty(propertyName, properties.getProperty(propertyName));
            }
        });
    }
}
