package com.truelayer.java;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

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

    @ParameterizedTest
    @MethodSource("validationTestData")
    public void itShouldThrowExceptionIfValidationFails(
            String keyId, byte[] privateKey, List<String> expectedValidationErrors) {
        Throwable thrown = assertThrows(TrueLayerException.class, () -> SigningOptions.builder()
                .keyId(keyId)
                .privateKey(privateKey)
                .build());

        expectedValidationErrors.forEach(
                validationError -> assertThat(thrown.getMessage(), containsString(validationError)));
    }

    private static Stream<Arguments> validationTestData() {
        final String KEY_ID_VALIDATION_ERROR = "key id must be set";
        final String PRIVATE_KEY_VALIDATION_ERROR = "private key must be set";

        return Stream.of(
                Arguments.of(A_KEY_ID, null, new ArrayList<>(Collections.singletonList(PRIVATE_KEY_VALIDATION_ERROR))),
                Arguments.of(
                        A_KEY_ID,
                        new byte[] {},
                        new ArrayList<>(Collections.singletonList(PRIVATE_KEY_VALIDATION_ERROR))),
                Arguments.of(null, A_PRIVATE_KEY, new ArrayList<>(Collections.singletonList(KEY_ID_VALIDATION_ERROR))),
                Arguments.of("", A_PRIVATE_KEY, new ArrayList<>(Collections.singletonList(KEY_ID_VALIDATION_ERROR))),
                Arguments.of(
                        "",
                        new byte[] {},
                        new ArrayList<>(Arrays.asList(KEY_ID_VALIDATION_ERROR, PRIVATE_KEY_VALIDATION_ERROR))),
                Arguments.of(
                        null,
                        null,
                        new ArrayList<>(Arrays.asList(KEY_ID_VALIDATION_ERROR, PRIVATE_KEY_VALIDATION_ERROR))));
    }
}
