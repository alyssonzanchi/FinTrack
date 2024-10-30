package br.edu.ifrs.fintrack.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EmailUtilsTest {

    @Test
    void testValidEmails() {
        assertTrue(EmailUtils.isValidEmail("example@example.com"));
        assertTrue(EmailUtils.isValidEmail("user.name+tag+sorting@example.com"));
        assertTrue(EmailUtils.isValidEmail("user_name@example.co.uk"));
    }

    @Test
    void testInvalidEmails() {
        assertFalse(EmailUtils.isValidEmail("plainaddress"));
        assertFalse(EmailUtils.isValidEmail("@missingusername.com"));
        assertFalse(EmailUtils.isValidEmail("username@.com"));
        assertFalse(EmailUtils.isValidEmail("username@domain@domain.com"));
        assertFalse(EmailUtils.isValidEmail("username@domain,com"));
    }
}
