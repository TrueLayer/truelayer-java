package com.truelayer.java;

import static com.truelayer.java.TestUtils.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import com.truelayer.java.http.auth.cache.ICredentialsCache;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TrueLayerClientBuilderTests {

    @Test
    @DisplayName("It should build a basic client")
    public void itShouldBuildABasicClient() {
        TrueLayerClientBuilder sut = new TrueLayerClientBuilder()
                .clientCredentials(getClientCredentials())
                .signingOptions(getSigningOptions());

        assertDoesNotThrow(sut::build);
    }

    @Test
    @DisplayName("It should build a client with default logging and credentials caching")
    public void itShouldBuildAClientWithDefaultLoggingAndCaching() {
        TrueLayerClientBuilder sut = new TrueLayerClientBuilder()
                .clientCredentials(getClientCredentials())
                .signingOptions(getSigningOptions())
                .withHttpLogs()
                .withCredentialsCaching();

        assertDoesNotThrow(sut::build);
    }

    @Test
    @DisplayName("It should build a client with custom logging and credentials caching")
    public void itShouldBuildAClientWithCustomLoggingAndCaching() {
        TrueLayerClientBuilder sut = new TrueLayerClientBuilder()
                .clientCredentials(getClientCredentials())
                .signingOptions(getSigningOptions())
                .withHttpLogs(System.out::println)
                .withCredentialsCaching(mock(ICredentialsCache.class));
        assertDoesNotThrow(sut::build);
    }

    @Test
    @DisplayName("It should throw an exception if credentials options are missing")
    public void itShouldBuildASandboxTrueLaterClient() {
        Throwable thrown = assertThrows(
                TrueLayerException.class, () -> TrueLayerClient.New().build());

        assertEquals("client credentials must be set", thrown.getMessage());
    }

    @Test
    @DisplayName(
            "It should throw an initialization error when invoking the payments handler and signing options are missing")
    public void itShouldThrowIfPaymentsHandlerIsInvokedWithoutSigningOptions() {
        ITrueLayerClient trueLayerClient =
                TrueLayerClient.New().clientCredentials(getClientCredentials()).build();

        Throwable thrown = assertThrows(TrueLayerException.class, trueLayerClient::payments);

        assertEquals(
                "payments handler not initialized. Make sure you specified the required signing options while initializing the library",
                thrown.getMessage());
    }

    @Test
    @DisplayName(
            "It should throw an initialization error when invoking the merchant accounts handler and signing options are missing")
    public void itShouldThrowIfMerchantAccountsHandlerIsInvokedWithoutSigningOptions() {
        ITrueLayerClient trueLayerClient =
                TrueLayerClient.New().clientCredentials(getClientCredentials()).build();

        Throwable thrown = assertThrows(TrueLayerException.class, trueLayerClient::merchantAccounts);

        assertEquals(
                "merchant accounts handler not initialized. Make sure you specified the required signing options while initializing the library",
                thrown.getMessage());
    }

    @Test
    @DisplayName(
            "It should throw an initialization error when invoking the mandates handler and signing options are missing")
    public void itShouldThrowIfMandatesHandlerIsInvokedWithoutSigningOptions() {
        ITrueLayerClient trueLayerClient =
                TrueLayerClient.New().clientCredentials(getClientCredentials()).build();

        Throwable thrown = assertThrows(TrueLayerException.class, trueLayerClient::mandates);

        assertEquals(
                "mandates handler not initialized. Make sure you specified the required signing options while initializing the library",
                thrown.getMessage());
    }

    @Test
    @DisplayName(
            "It should throw an initialization error when invoking the payouts handler and signing options are missing")
    public void itShouldThrowIfPayoutsHandlerIsInvokedWithoutSigningOptions() {
        ITrueLayerClient trueLayerClient =
                TrueLayerClient.New().clientCredentials(getClientCredentials()).build();

        Throwable thrown = assertThrows(TrueLayerException.class, trueLayerClient::payouts);

        assertEquals(
                "payouts handler not initialized. Make sure you specified the required signing options while initializing the library",
                thrown.getMessage());
    }
}
