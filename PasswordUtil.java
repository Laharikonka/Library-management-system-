package com.library.security;

import java.security.*;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/** PBKDF2 with a random salt. Stored format: base64(salt):base64(hash). */
public final class PasswordUtil {
    private PasswordUtil() {}
    public static String hash(String password) {
        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt) + ":" + Base64.getEncoder().encodeToString(derive(password, salt));
    }
    public static boolean verify(String password, String stored) {
        String[] p = stored.split(":");
        return MessageDigest.isEqual(derive(password, Base64.getDecoder().decode(p[0])), Base64.getDecoder().decode(p[1]));
    }
    private static byte[] derive(String password, byte[] salt) {
        try {
            return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
                .generateSecret(new PBEKeySpec(password.toCharArray(), salt, 65536, 256)).getEncoded();
        } catch (GeneralSecurityException e) { throw new IllegalStateException(e); }
    }
}
