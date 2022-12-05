package com.truelayer.java.http.interceptors;

import static org.apache.commons.lang3.StringUtils.isEmpty;

import com.truelayer.java.Constants;
import com.truelayer.java.versioninfo.VersionInfo;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

@RequiredArgsConstructor
public class TrueLayerAgentInterceptor implements Interceptor {
    private final VersionInfo versionInfo;

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        Request newRequest = request.newBuilder()
                .header(
                        Constants.HeaderNames.TL_AGENT,
                        String.format(
                                "%s/%s %s",
                                versionInfo.libraryName(), versionInfo.libraryVersion(), buildJavaVersionIdentifier()))
                .build();
        return chain.proceed(newRequest);
    }

    private String buildJavaVersionIdentifier() {
        return isEmpty(versionInfo.javaVersion()) ? "" : String.format("(Java/%s)", versionInfo.javaVersion());
    }
}
