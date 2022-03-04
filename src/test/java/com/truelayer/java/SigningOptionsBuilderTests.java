package com.truelayer.java;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class SigningOptionsBuilderTests {

    public static final String A_KEY_ID = "a-key-id";
    public static final byte[] A_PRIVATE_KEY = "a-private-key".getBytes(StandardCharsets.UTF_8);

    @Test
    @DisplayName("It should build signing options")
    public void itShouldBuildASigningOptionsInstance() {
        SigningOptions signingOptions = SigningOptions.builder()
                .keyId(A_KEY_ID)
                .privateKey(A_PRIVATE_KEY)
                .build();

        assertEquals(A_KEY_ID, signingOptions.keyId());
        assertEquals(A_PRIVATE_KEY, signingOptions.privateKey());
    }

    @Test
    @DisplayName("It should throw an exception if key id is not set")
    public void itShouldThrowExceptionIfKeyIdNotSet() {
        Throwable thrown = assertThrows(
                TrueLayerException.class, () -> SigningOptions.builder().build());

        assertEquals("key id must be set", thrown.getMessage());
    }

    @Test
    @DisplayName("It should throw an exception if private key is not set")
    public void itShouldThrowExceptionIfPrivateKeyNotSet() {
        Throwable thrown = assertThrows(
                TrueLayerException.class,
                () -> SigningOptions.builder().keyId(A_KEY_ID).build());

        assertEquals("private key must be set", thrown.getMessage());
    }
}
