package br.edu.ifrs.fintrack.util;

import br.edu.ifrs.fintrack.exception.ConfigException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigProperties {
    private static final Properties PROPERTIES = new Properties();

    static {
        loadProperties("application.properties");
    }

    public static void loadProperties(String fileName) {
        try (InputStream input = ConfigProperties.class.getClassLoader().getResourceAsStream(fileName)) {
            if (input == null) {
                throw new ConfigException("Desculpe, não foi possível encontrar o arquivo " + fileName);
            }
            PROPERTIES.load(input);
        } catch (IOException e) {
            throw new ConfigException("Erro ao carregar o arquivo " + fileName, e);
        }
    }

    public static String getProperty(String key) {
        return PROPERTIES.getProperty(key);
    }
}
