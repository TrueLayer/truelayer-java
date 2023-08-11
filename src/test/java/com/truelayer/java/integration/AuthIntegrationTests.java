package com.truelayer.java.integration;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.truelayer.java.TestUtils.assertNotError;
import static com.truelayer.java.TestUtils.getClientCredentials;
import static org.junit.jupiter.api.Assertions.*;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.truelayer.java.Constants;
import com.truelayer.java.TestUtils;
import com.truelayer.java.TestUtils.RequestStub;
import com.truelayer.java.Utils;
import com.truelayer.java.auth.entities.AccessToken;
import com.truelayer.java.auth.entities.GenerateOauthTokenRequest;
import com.truelayer.java.http.entities.ApiResponse;
import com.truelayer.java.http.entities.ProblemDetails;
import com.truelayer.java.http.mappers.ErrorMapper;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;

@WireMockTest
@Tag("integration")
@DisplayName("Auth integration tests")
public class AuthIntegrationTests extends IntegrationTests {

    @BeforeEach
    public void init() {
        resetAllRequests();
    }

    @Test
    @DisplayName("It should return an error in case on an authorized error from the auth API.")
    @SneakyThrows
    public void shouldReturnErrorIfUnauthorized() {
        RequestStub.New()
                .method("post")
                .path(urlPathEqualTo("/connect/token"))
                .status(400)
                .bodyFile("auth/400.invalid_client.json")
                .build();

        ApiResponse<AccessToken> response = tlClient.auth()
                .getOauthToken(Collections.singletonList("payments"))
                .get();

        assertTrue(response.isError());
        ProblemDetails problemDetails = response.getError();
        assertEquals("invalid_client", problemDetails.getTitle());
        Assertions.assertEquals(ErrorMapper.GENERIC_ERROR_TYPE, problemDetails.getType());
        assertEquals(400, problemDetails.getStatus());
        assertFalse(problemDetails.getTraceId().isEmpty());
    }

    @Test
    @DisplayName("It should create and return an access token with a single scope")
    @SneakyThrows
    public void shouldReturnAnAccessTokenWithSingleScope() {
        String jsonResponseFile = "auth/200.access_token.json";
        GenerateOauthTokenRequest generateOauthTokenRequest = GenerateOauthTokenRequest.builder()
                .clientId(getClientCredentials().clientId())
                .clientSecret(getClientCredentials().clientId())
                .scope(Constants.Scopes.PAYMENTS)
                .build();
        RequestStub.New()
                .method("post")
                .path(urlPathEqualTo("/connect/token"))
                .bodyFile(Utils.getObjectMapper().writeValueAsString(generateOauthTokenRequest))
                .status(200)
                .bodyFile(jsonResponseFile)
                .build();

        ApiResponse<AccessToken> response = tlClient.auth()
                .getOauthToken(Collections.singletonList(Constants.Scopes.PAYMENTS))
                .get();

        verify(
                1,
                postRequestedFor(urlPathEqualTo("/connect/token"))
                        .withRequestBody(matchingJsonPath(
                                "$.client_id", equalTo(getClientCredentials().clientId())))
                        .withRequestBody(matchingJsonPath(
                                "$.client_secret",
                                equalTo(getClientCredentials().clientSecret())))
                        .withRequestBody(matchingJsonPath("$.scope", equalTo(Constants.Scopes.PAYMENTS)))
                        .withRequestBody(matchingJsonPath(
                                "$.grant_type",
                                equalTo(GenerateOauthTokenRequest.GrantType.CLIENT_CREDENTIALS.getType()))));
        assertNotError(response);
        AccessToken expected = TestUtils.deserializeJsonFileTo(jsonResponseFile, AccessToken.class);
        assertEquals(expected, response.getData());
    }

    @Test
    @DisplayName("It should create and return an access token with multiple scopes, whitespace separated")
    @SneakyThrows
    public void shouldReturnAnAccessTokenWithMultipleScopes() {
        String jsonResponseFile = "auth/200.access_token.json";
        List<String> scopes = Collections.unmodifiableList(Arrays.asList(Constants.Scopes.PAYMENTS, "another-scope"));
        GenerateOauthTokenRequest generateOauthTokenRequest = GenerateOauthTokenRequest.builder()
                .clientId(getClientCredentials().clientId())
                .clientSecret(getClientCredentials().clientId())
                .scope(Constants.Scopes.PAYMENTS)
                .build();
        RequestStub.New()
                .method("post")
                .path(urlPathEqualTo("/connect/token"))
                .bodyFile(Utils.getObjectMapper().writeValueAsString(generateOauthTokenRequest))
                .status(200)
                .bodyFile(jsonResponseFile)
                .build();

        ApiResponse<AccessToken> response =
                tlClient.auth().getOauthToken(scopes).get();

        verify(
                1,
                postRequestedFor(urlPathEqualTo("/connect/token"))
                        .withRequestBody(matchingJsonPath(
                                "$.client_id", equalTo(getClientCredentials().clientId())))
                        .withRequestBody(matchingJsonPath(
                                "$.client_secret",
                                equalTo(getClientCredentials().clientSecret())))
                        .withRequestBody(matchingJsonPath("$.scope", equalTo(String.join(" ", scopes))))
                        .withRequestBody(matchingJsonPath(
                                "$.grant_type",
                                equalTo(GenerateOauthTokenRequest.GrantType.CLIENT_CREDENTIALS.getType()))));

        assertNotError(response);
        AccessToken expected = TestUtils.deserializeJsonFileTo(jsonResponseFile, AccessToken.class);
        assertEquals(expected, response.getData());
    }
}
