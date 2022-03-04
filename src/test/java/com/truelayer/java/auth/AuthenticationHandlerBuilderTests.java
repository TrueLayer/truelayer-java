package com.truelayer.java.auth;

import static com.truelayer.java.TestUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import com.truelayer.java.TrueLayerException;
import com.truelayer.java.http.RetrofitFactory;
import java.net.URI;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import retrofit2.Retrofit;

class AuthenticationHandlerBuilderTests {

    @Test
    @DisplayName("It should yield an authentication handler")
    public void itShouldYieldAnAuthenticationHandler() {
        AuthenticationHandler handler = AuthenticationHandler.New()
                .httpClient(getTestHttpClient())
                .clientCredentials(getClientCredentials())
                .build();

        assertNotNull(handler.getAuthenticationApi());
        assertEquals(getClientCredentials(), handler.getClientCredentials());
    }

    @Test
    @DisplayName("It should throw and exception if credentials are missing")
    public void itShouldThrowExceptionIfCredentialsMissing() {
        Throwable thrown = assertThrows(TrueLayerException.class, () -> AuthenticationHandler.New()
                .httpClient(getTestHttpClient())
                .build());

        assertEquals("client credentials must be set", thrown.getMessage());
    }

    private Retrofit getTestHttpClient() {
        return RetrofitFactory.build(new OkHttpClient(), URI.create("http://localhost"));
    }
}
