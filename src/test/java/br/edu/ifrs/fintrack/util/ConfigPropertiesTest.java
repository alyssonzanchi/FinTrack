package br.edu.ifrs.fintrack.util;

import br.edu.ifrs.fintrack.exception.ConfigException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ConfigPropertiesTest {

    @Test
    public void testGetProperty_ValidKey() {
        String value = ConfigProperties.getProperty("test.key");
        assertEquals("test.value", value);
    }

    @Test
    public void testGetProperty_NullKey() {
        assertThrows(NullPointerException.class, () -> ConfigProperties.getProperty(null));
    }

    @Test
    public void testGetProperty_InvalidKey() {
        String value = ConfigProperties.getProperty("invalid.key");
        assertNull(value);
    }

    @Test
    public void testGetProperty_FileNotFound() {
        assertThrows(ConfigException.class, () -> {
            ConfigProperties.loadProperties("nonexistent.properties"); // Tenta carregar um arquivo inexistente
        });
    }
}
