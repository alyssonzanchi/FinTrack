package br.edu.ifrs.fintrack.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PasswordUtilsTest {

    @Test
    void testHashAndCheckPassword() {
        String password = "strongPassword";
        String hashedPassword = PasswordUtils.hashPassword(password);

        // Verifique que o hash foi gerado corretamente
        assertTrue(hashedPassword.startsWith("$2a$"));

        // Verifique se o checkPassword consegue validar a senha com o hash gerado
        assertTrue(PasswordUtils.checkPassword(password, hashedPassword));

        // Teste com uma senha incorreta
        assertFalse(PasswordUtils.checkPassword("wrongPassword", hashedPassword));
    }
}
