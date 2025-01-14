package com.truelayer.java.http.auth.cache;

import static com.truelayer.java.Constants.Scopes.*;
import static org.junit.jupiter.api.Assertions.*;

import com.truelayer.java.TrueLayerException;
import com.truelayer.java.entities.RequestScopes;
import java.text.MessageFormat;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class CredentialsCacheHelperTests {

    @DisplayName("It should build a cache key")
    @ParameterizedTest(name = "with clientId={0} and scopes={1}")
    @MethodSource("validParameters")
    public void shouldBuildACacheKey(String clientId, RequestScopes scopes, String expectedScopesHash) {
        String cacheKey = CredentialsCacheHelper.buildKey(clientId, scopes);

        assertFalse(cacheKey.isEmpty());
        String expectedKey = MessageFormat.format("tl-auth-token:{0}:{1}", clientId, expectedScopesHash);
        assertEquals(expectedKey, cacheKey);
    }

    @Test
    @DisplayName("It should build the same key for scopes in different order")
    public void shouldBuildACacheKeyOrderNotRelevant() {
        String clientId = UUID.randomUUID().toString();
        RequestScopes scopes1 =
                RequestScopes.builder().scope(SIGNUP_PLUS).scope(PAYMENTS).build();
        RequestScopes scopes2 =
                RequestScopes.builder().scopes(List.of(PAYMENTS, SIGNUP_PLUS)).build();

        String cacheKey1 = CredentialsCacheHelper.buildKey(clientId, scopes1);
        String cacheKey2 = CredentialsCacheHelper.buildKey(clientId, scopes2);

        assertFalse(cacheKey1.isEmpty());
        assertFalse(cacheKey2.isEmpty());

        String expectedKey =
                MessageFormat.format("tl-auth-token:{0}:{1}", clientId, "c18feeafe5a3b05f2e627def2a88b208");
        assertEquals(expectedKey, cacheKey1);
        assertEquals(expectedKey, cacheKey2);
    }

    @DisplayName("It should throw an exception if invalid parameters are provided")
    @ParameterizedTest(name = "with clientId={0} and scopes={1}")
    @MethodSource("invalidParameters")
    public void shouldThrowAnExceptionIfInvalidParameters(String clientId, RequestScopes scopes) {
        Throwable thrown = Assertions.assertThrows(
                TrueLayerException.class, () -> CredentialsCacheHelper.buildKey(clientId, scopes));

        assertEquals("Invalid client id or request scopes provided", thrown.getMessage());
    }

    private static Stream<Arguments> validParameters() {
        return Stream.of(
                Arguments.of(
                        UUID.randomUUID().toString(),
                        RequestScopes.builder().scope(PAYMENTS).build(),
                        "84d5eaf713c96eecb3d2c4a83e64dc9a"),
                Arguments.of(
                        UUID.randomUUID().toString(),
                        RequestScopes.builder()
                                .scopes(List.of(PAYMENTS, SIGNUP_PLUS))
                                .build(),
                        "c18feeafe5a3b05f2e627def2a88b208"));
    }

    private static Stream<Arguments> invalidParameters() {
        return Stream.of(
                Arguments.of(null, null),
                Arguments.of("", null),
                Arguments.of(UUID.randomUUID().toString(), null),
                Arguments.of(
                        UUID.randomUUID().toString(), RequestScopes.builder().build()),
                Arguments.of(null, RequestScopes.builder().scope(PAYMENTS).build()),
                Arguments.of("", RequestScopes.builder().scope(PAYMENTS).build()));
    }
}
