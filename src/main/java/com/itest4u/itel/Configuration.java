package com.itest4u.itel;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Configuration {
    private static Properties properties;

    public static void load(String filename) {
        properties = new Properties();
        try {
            properties.load(new FileReader(filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getProperty(String propertyName) {
        if (properties == null) {
            return null;
        }
        return properties.getProperty(propertyName);
    }
}
