package truelayer.java.http.interceptors;

import lombok.RequiredArgsConstructor;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okio.Buffer;
import org.jetbrains.annotations.NotNull;
import truelayer.java.SigningOptions;
import truelayer.java.signing.Signer;

import java.io.IOException;

import static truelayer.java.Constants.HeaderNames.IDEMPOTENCY_KEY;
import static truelayer.java.Constants.HeaderNames.TL_SIGNATURE;

@RequiredArgsConstructor
public class SignatureInterceptor implements Interceptor {

    private final SigningOptions signingOptions;

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        var request = chain.request();

        if (needsSignature(request)) {
            var clonedRequest = request.newBuilder().build();

            var signature = computeSignature(
                    clonedRequest.method().toLowerCase(),
                    clonedRequest.url().encodedPath(),
                    clonedRequest.header(IDEMPOTENCY_KEY),
                    getBodyAsString(clonedRequest)
            );
            var newRequest = request.newBuilder()
                    .header(TL_SIGNATURE, signature)
                    .build();
            return chain.proceed(newRequest);
        }

        return chain.proceed(request);
    }

    private String getBodyAsString(Request request) throws IOException {
        try(var buffer = new Buffer()) {
            request.body().writeTo(buffer);
            return buffer.readUtf8();
        }
    }

    private boolean needsSignature(Request request) {
        return request.method().equalsIgnoreCase("post");
    }

    private String computeSignature(String method, String path, String idempotencyKey, String jsonBody) {
        byte[] privateKey = signingOptions.getPrivateKey();

        return new Signer.Builder(signingOptions.getKeyId(), privateKey)
                .addHeader(IDEMPOTENCY_KEY, idempotencyKey)
                .addHttpMethod(method)
                .addPath(path)
                .addBody(jsonBody)
                .getSignature();
    }
}
