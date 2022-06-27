package com.truelayer.java.http;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.truelayer.java.TestUtils;
import com.truelayer.java.versioninfo.VersionInfo;
import com.truelayer.java.versioninfo.VersionInfoLoader;
import java.time.Duration;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OkHttpClientFactoryTests {

    @Test
    @DisplayName("It should build an Auth API client")
    public void shouldCreateAnAuthApiClient() {
        OkHttpClientFactory sut = getOkHttpClientFactory();

        OkHttpClient authClient =
                sut.buildAuthApiClient(TestUtils.getClientCredentials(), Duration.ofSeconds(1), null, null, null);

        // todo: improve assertions and pass non null values above
        assertNotNull(authClient);
        assertFalse(authClient.interceptors().isEmpty());
    }

    @Test
    @Disabled
    public void shouldThrowCredentialsMissingException() {}

    private OkHttpClientFactory getOkHttpClientFactory() {
        VersionInfo versionInfo = VersionInfo.builder()
                .libraryName("truelayer-java")
                .libraryVersion("1.0.0")
                .build();
        VersionInfoLoader versionInfoLoader = mock(VersionInfoLoader.class);
        when(versionInfoLoader.load()).thenReturn(versionInfo);

        return new OkHttpClientFactory(versionInfoLoader);
    }
}
