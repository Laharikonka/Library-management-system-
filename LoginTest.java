package com.library;

import static org.junit.jupiter.api.Assertions.*;
import com.library.security.PasswordUtil;
import com.library.util.InputValidator;
import org.junit.jupiter.api.Test;

class LoginTest {
    @Test void hashVerifiesCorrectPassword() { assertTrue(PasswordUtil.verify("secret1", PasswordUtil.hash("secret1"))); }
    @Test void rejectsWrongPassword() { assertFalse(PasswordUtil.verify("nope", PasswordUtil.hash("secret1"))); }
    @Test void samePasswordGivesDifferentHashes() { assertNotEquals(PasswordUtil.hash("a1b2c3"), PasswordUtil.hash("a1b2c3")); }
    @Test void validatesEmails() {
        assertTrue(InputValidator.isValidEmail("asha@example.com"));
        assertFalse(InputValidator.isValidEmail("asha@"));
    }
}
