package truelayer.java.http.interceptors;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class UserAgentInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request newRequest;

        newRequest = request.newBuilder()
                .header("User-Agent", String.format("%s/%s",
                        getClass().getPackage().getName(),
                        getClass().getPackage().getImplementationVersion()))
                .build();
        return chain.proceed(newRequest);
    }
}
