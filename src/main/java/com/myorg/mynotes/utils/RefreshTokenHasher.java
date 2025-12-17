package com.myorg.mynotes.utils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public final class RefreshTokenHasher {

    private static final String HMAC_ALGO = "HmacSHA256";
    private static final byte[] SECRET =
            "change-this-to-long-random-secret".getBytes();

    private RefreshTokenHasher() {}

    public static String hash(String token) {
        try {
            Mac mac = Mac.getInstance(HMAC_ALGO);
            mac.init(new SecretKeySpec(SECRET, HMAC_ALGO));
            byte[] raw = mac.doFinal(token.getBytes());
            return Base64.getUrlEncoder().withoutPadding().encodeToString(raw);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to hash refresh token", e);
        }
    }
}