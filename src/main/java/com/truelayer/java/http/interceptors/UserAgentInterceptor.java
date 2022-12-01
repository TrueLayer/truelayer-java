package com.truelayer.java.http.interceptors;

import com.truelayer.java.Constants;
import com.truelayer.java.versioninfo.VersionInfo;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

@RequiredArgsConstructor
public class UserAgentInterceptor implements Interceptor {
    private final VersionInfo versionInfo;

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request newRequest = request.newBuilder()
                .header(
                        Constants.HeaderNames.TL_AGENT,
                        String.format("%s/%s", versionInfo.libraryName(), versionInfo.libraryVersion()))
                .build();
        return chain.proceed(newRequest);
    }
}
