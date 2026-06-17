package com.remind.back.utils;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class UtilsTest {

    private final Utils utils = new Utils();

    @Test
    void testGenerateRandomUsername() {
        String username = utils.generateRandomUsername("Juan", "Perez");
        assertTrue(username.startsWith("juan.perez"));
        assertTrue(username.length() > "juan.perez".length());
    }

    @Test
    void testGenerateRandomUsername_WithSpaces() {
        String username = utils.generateRandomUsername("  Jose  Maria  ", "  Lopez  ");
        assertTrue(username.startsWith("josemaria.lopez"));
    }

    @Test
    void testGenerateRandomPassword() {
        String password = utils.generateRandomPassword("Ana", "Gomez");
        assertNotNull(password);
        assertTrue(password.length() >= 6);
        assertTrue(password.matches(".*[A-Z].*"));
        assertTrue(password.matches(".*[a-z].*"));
        assertTrue(password.matches(".*\\d.*"));
        assertTrue(password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{}|;:,.<>?].*"));
    }

    @Test
    void testGenerateRandomPassword_ShortName() {
        String password = utils.generateRandomPassword("A", "B");
        assertNotNull(password);
        assertTrue(password.startsWith("US"));
    }

    @Test
    void testGenerateRandomPassword_NullName() {
        String password = utils.generateRandomPassword(null, null);
        assertNotNull(password);
    }

    @Test
    void testGenerateRandomPassword_WithAccents() {
        String password = utils.generateRandomPassword("María", "José");
        assertNotNull(password);
        assertTrue(password.startsWith("MA"));
    }
}
