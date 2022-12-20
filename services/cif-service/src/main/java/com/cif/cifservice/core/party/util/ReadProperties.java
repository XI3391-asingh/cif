package com.cif.cifservice.core.party.util;

import com.cif.cifservice.resources.exceptions.ServerSideException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class ReadProperties {

    private static ReadProperties instance = null;
    private Properties properties = null;

    private ReadProperties() {
        properties = new Properties();
        try {
            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("validation-message.properties");
            properties.load(inputStream);
        } catch (IOException exception) {
            throw new ServerSideException("ERROR", "Error occurs while reading properties file! ", exception);
        }
    }

    public static synchronized ReadProperties getInstance() {
        if (instance == null) {
            instance = new ReadProperties();
        }
        return instance;
    }

    public String getValue(String key) {
        return this.properties.getProperty(key, String.format("The key %s does not exists!", key));
    }
}