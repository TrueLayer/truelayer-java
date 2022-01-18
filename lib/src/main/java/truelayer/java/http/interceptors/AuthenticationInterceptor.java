package truelayer.java.http.interceptors;

import static truelayer.java.Constants.HeaderNames.AUTHORIZATION;

import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import truelayer.java.TrueLayerException;
import truelayer.java.auth.IAuthenticationHandler;

@RequiredArgsConstructor
public class AuthenticationInterceptor implements Interceptor {

    private final IAuthenticationHandler authenticationHandler;

    private final List<String> scopes;

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        var accessToken = authenticationHandler.getOauthToken(scopes);

        if (accessToken.isError()) {
            throw new TrueLayerException(String.format("Unable to authenticate request: %s", accessToken.getError()));
        }

        Request request = chain.request();
        var newRequest = request.newBuilder()
                .header(
                        AUTHORIZATION,
                        buildAuthorizationHeader(accessToken.getData().getAccessToken()))
                .build();
        return chain.proceed(newRequest);
    }

    private String buildAuthorizationHeader(String token) {
        return new StringBuilder("Bearer").append(" ").append(token).toString();
    }
}
