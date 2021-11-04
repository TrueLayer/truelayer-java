package truelayer.java.auth;

import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import truelayer.java.TrueLayerHttpClient;
import truelayer.java.auth.entities.AccessToken;
import truelayer.java.auth.exceptions.AuthenticationException;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Random;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class AuthenticationTests {

    public static final String A_SCOPE = "paydirect";

    @Mock
    private TrueLayerHttpClient httpClientMock;

    @Mock
    private HttpResponse<String> httpResponseMock;

    private String accessToken, scope, tokenType;
    private int expiresIn;

    @BeforeEach
    void setUp() {
        accessToken = randomAlphabetic(20);
        scope = randomAlphabetic(20);
        tokenType = randomAlphabetic(20);
        expiresIn = new Random().nextInt();
    }

    @Test
    @DisplayName("It should yield and access token if correct credentials are supplied")
    public void itShouldYieldAnAccessToken() throws AuthenticationException, IOException, InterruptedException {
        List<String> scopes = List.of(A_SCOPE);
        String accessTokenJsonString = getAccessTokenJsonString();

        var sut = Authentication.builder()
                .httpClient(httpClientMock)
                .build();

        Mockito.when(httpResponseMock.statusCode()).thenReturn(200);
        Mockito.when(httpResponseMock.body()).thenReturn(accessTokenJsonString);

        Mockito.when(httpClientMock.sendOauthRequest(scopes)).thenReturn(httpResponseMock);

        var token = sut.getOauthToken(scopes);

        assertEquals(token.getAccessToken(), accessToken);
        assertEquals(token.getTokenType(), tokenType);
        assertEquals(token.getExpiresIn(), expiresIn);
    }

    private String getAccessTokenJsonString() {
        return new Gson().toJson(new AccessToken(accessToken, expiresIn, scope, tokenType));
    }
}
