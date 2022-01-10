package truelayer.java.http.interceptors;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang3.ObjectUtils;
import truelayer.java.VersionInfo;
import truelayer.java.payments.entities.User;

import java.io.IOException;

import static org.apache.commons.lang3.ObjectUtils.isNotEmpty;

@RequiredArgsConstructor
public class UserAgentInterceptor implements Interceptor {
    private final VersionInfo versionInfo;

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request newRequest;

        newRequest = request.newBuilder()
                .header("User-Agent", String.format("%s/%s",
                        versionInfo.getName(),
                        versionInfo.getVersion()))
                .build();
        return chain.proceed(newRequest);
    }
}
