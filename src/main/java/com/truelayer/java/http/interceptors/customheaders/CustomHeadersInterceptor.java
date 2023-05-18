package com.truelayer.java.http.interceptors.customheaders;

import static org.apache.commons.lang3.ObjectUtils.isEmpty;

import com.truelayer.java.http.entities.Headers;
import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class CustomHeadersInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        Headers customHeaders = request.tag(Headers.class);

        if (isEmpty(customHeaders)) {
            // no custom header to attach to the request's headers
            return chain.proceed(request);
        }

        Request.Builder newRequestBuilder = request.newBuilder();
        CustomHeadersConverter.toMap(customHeaders).forEach(newRequestBuilder::header);

        return chain.proceed(newRequestBuilder.build());
    }
}
