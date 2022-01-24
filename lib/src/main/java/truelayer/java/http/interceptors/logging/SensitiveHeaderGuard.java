package truelayer.java.http.interceptors.logging;

import static truelayer.java.common.Constants.HeaderNames.AUTHORIZATION;
import static truelayer.java.common.Constants.HeaderNames.COOKIE;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import okhttp3.Headers;

public class SensitiveHeaderGuard {

    protected static final String SENSITIVE_HEADER_MASK = "***";

    public List<String> getSanitizedHeaders(Headers headers) {
        var sanitizedHeaders = new ArrayList<String>();
        headers.forEach(h -> {
            var headerName = h.getFirst();
            var headerValue = h.getSecond();
            sanitizedHeaders.add(String.format(
                    "%s=%s", h.getFirst(), isSensitiveHeader(headerName) ? SENSITIVE_HEADER_MASK : headerValue));
        });
        return Collections.unmodifiableList(sanitizedHeaders);
    }

    protected boolean isSensitiveHeader(String headerName) {
        return headerName.equalsIgnoreCase(AUTHORIZATION) || headerName.equalsIgnoreCase(COOKIE);
    }
}
