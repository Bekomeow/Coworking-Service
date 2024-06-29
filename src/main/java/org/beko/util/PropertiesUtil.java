package org.beko.util;

import java.io.IOException;
import java.util.Properties;

/**
 * Utility class for loading properties from the application.properties file.
 */
public final class PropertiesUtil {
    private static final Properties PROPERTIES = new Properties();

    static {
        loadProperties();
    }

    /**
     * Gets a property value by key.
     *
     * @param key the property key
     * @return the property value
     */
    public static String get(String key) {
        return PROPERTIES.getProperty(key);
    }

    /**
     * Loads properties from the application.properties file.
     */
    private static void loadProperties() {
        try(var inputStream = PropertiesUtil.class.getClassLoader().getResourceAsStream("application.properties")) {
            PROPERTIES.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private PropertiesUtil() {
    }
}