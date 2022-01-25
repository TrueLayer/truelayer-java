package truelayer.java.http.interceptors;

import static truelayer.java.common.Constants.HeaderNames.USER_AGENT;

import java.io.IOException;
import lombok.RequiredArgsConstructor;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import truelayer.java.configuration.Configuration;

@RequiredArgsConstructor
public class UserAgentInterceptor implements Interceptor {
    private final Configuration.VersionInfo versionInfo;

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request newRequest = request.newBuilder()
                .header(USER_AGENT, String.format("%s/%s", versionInfo.libraryName(), versionInfo.libraryVersion()))
                .build();
        return chain.proceed(newRequest);
    }
}
