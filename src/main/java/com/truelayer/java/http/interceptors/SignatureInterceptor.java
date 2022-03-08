package com.truelayer.java.http.interceptors;

import static com.truelayer.java.Constants.HeaderNames.IDEMPOTENCY_KEY;
import static com.truelayer.java.Constants.HeaderNames.TL_SIGNATURE;
import static org.apache.commons.lang3.ObjectUtils.isEmpty;
import static org.apache.commons.lang3.ObjectUtils.isNotEmpty;

import com.truelayer.java.SigningOptions;
import com.truelayer.signing.Signer;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okio.Buffer;

@RequiredArgsConstructor
public class SignatureInterceptor implements Interceptor {

    private final SigningOptions signingOptions;

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        if (needsSignature(request)) {
            Request clonedRequest = request.newBuilder().build();

            String signature = computeSignature(
                    clonedRequest.method().toLowerCase(),
                    clonedRequest.url().encodedPath(),
                    clonedRequest.header(IDEMPOTENCY_KEY),
                    getBodyAsString(clonedRequest));
            Request newRequest =
                    request.newBuilder().header(TL_SIGNATURE, signature).build();
            return chain.proceed(newRequest);
        }

        return chain.proceed(request);
    }

    private String getBodyAsString(Request request) throws IOException {
        if (isEmpty(request.body())) {
            return null;
        }

        try (Buffer buffer = new Buffer()) {
            request.body().writeTo(buffer);
            return buffer.readUtf8();
        }
    }

    private boolean needsSignature(Request request) {
        return !request.method().equalsIgnoreCase("get");
    }

    private String computeSignature(String method, String path, String idempotencyKey, String jsonBody) {
        byte[] privateKey = signingOptions.privateKey();

        Signer signer = Signer.from(signingOptions.keyId(), privateKey)
                .header(IDEMPOTENCY_KEY, idempotencyKey)
                .method(method)
                .path(path);

        if (isNotEmpty(jsonBody)) {
            signer.body(jsonBody);
        }

        return signer.sign();
    }
}
