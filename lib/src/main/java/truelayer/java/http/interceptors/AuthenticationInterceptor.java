package truelayer.java.http.interceptors;

import static truelayer.java.common.Constants.HeaderNames.AUTHORIZATION;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import truelayer.java.TrueLayerException;
import truelayer.java.auth.IAuthenticationHandler;
import truelayer.java.auth.entities.AccessToken;
import truelayer.java.http.entities.ApiResponse;

@RequiredArgsConstructor
public class AuthenticationInterceptor implements Interceptor {

    private final IAuthenticationHandler authenticationHandler;

    private final List<String> scopes;

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        var accessToken = tryGetToken(authenticationHandler.getOauthToken(scopes));

        Request request = chain.request();
        var newRequest = request.newBuilder()
                .header(AUTHORIZATION, buildAuthorizationHeader(accessToken.getAccessToken()))
                .build();
        return chain.proceed(newRequest);
    }

    private AccessToken tryGetToken(CompletableFuture<ApiResponse<AccessToken>> responseFuture) {
        ApiResponse<AccessToken> accessTokenResponse;
        try {
            accessTokenResponse = responseFuture.get();
        } catch (Exception e) {
            throw new TrueLayerException("unable to get an access token response", e);
        }

        if (accessTokenResponse.isError()) {
            throw new TrueLayerException(
                    String.format("Unable to authenticate request: %s", accessTokenResponse.getError()));
        }

        return accessTokenResponse.getData();
    }

    private String buildAuthorizationHeader(String token) {
        return new StringBuilder("Bearer").append(" ").append(token).toString();
    }
}
