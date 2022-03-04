package com.truelayer.java;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

public class ClientCredentialsBuilderTests {

    public static final String A_CLIENT_ID = "a-client-id";
    public static final String A_CLIENT_SECRET = "a-client-secret";

    @Test
    @DisplayName("It should build client credentials")
    public void itShouldBuildAClientCredentialsInstance() {
        ClientCredentials clientCredentials = ClientCredentials.builder()
                .clientId(A_CLIENT_ID)
                .clientSecret(A_CLIENT_SECRET)
                .build();

        assertEquals(A_CLIENT_ID, clientCredentials.clientId);
        assertEquals(A_CLIENT_SECRET, clientCredentials.clientSecret);
    }

    @ParameterizedTest
    @MethodSource("validationTestData")
    public void itShouldThrowExceptionIfValidationFails(
            String clientId, String clientSecret, List<String> expectedValidationErrors) {
        Throwable thrown = assertThrows(TrueLayerException.class, () -> ClientCredentials.builder()
                .clientId(clientId)
                .clientSecret(clientSecret)
                .build());

        expectedValidationErrors.forEach(
                validationError -> assertThat(thrown.getMessage(), containsString(validationError)));
    }

    private static Stream<Arguments> validationTestData() {
        final String CLIENT_ID_VALIDATION_ERROR = "client id must be set";
        final String CLIENT_SECRET_VALIDATION_ERROR = "client secret must be set";

        return Stream.of(
                Arguments.of(
                        A_CLIENT_ID, null, new ArrayList<>(Collections.singletonList(CLIENT_SECRET_VALIDATION_ERROR))),
                Arguments.of(
                        A_CLIENT_ID, "", new ArrayList<>(Collections.singletonList(CLIENT_SECRET_VALIDATION_ERROR))),
                Arguments.of(
                        null, A_CLIENT_SECRET, new ArrayList<>(Collections.singletonList(CLIENT_ID_VALIDATION_ERROR))),
                Arguments.of(
                        "", A_CLIENT_SECRET, new ArrayList<>(Collections.singletonList(CLIENT_ID_VALIDATION_ERROR))),
                Arguments.of(
                        "",
                        "",
                        new ArrayList<>(Arrays.asList(CLIENT_ID_VALIDATION_ERROR, CLIENT_SECRET_VALIDATION_ERROR))),
                Arguments.of(
                        null,
                        null,
                        new ArrayList<>(Arrays.asList(CLIENT_ID_VALIDATION_ERROR, CLIENT_SECRET_VALIDATION_ERROR))));
    }
}
