package truelayer.java.http.interceptors.logging;

import static org.apache.commons.lang3.ObjectUtils.isEmpty;
import static truelayer.java.Constants.HeaderNames.AUTHORIZATION;
import static truelayer.java.Constants.HeaderNames.COOKIE;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import okhttp3.Headers;
import truelayer.java.http.entities.Header;

public class SensitiveHeaderGuard {

    protected static final String SENSITIVE_HEADER_MASK = "***";

    public List<Header> getSanitizedHeaders(Headers headers) {
        if (isEmpty(headers)) {
            return Collections.emptyList();
        }
        List<Header> sanitizedHeaders = new ArrayList<>();
        headers.toMultimap()
                .forEach((headerName, headerValues) -> sanitizedHeaders.add(new Header(
                        headerName,
                        isSensitiveHeader(headerName) ? SENSITIVE_HEADER_MASK : String.join(",", headerValues))));
        return Collections.unmodifiableList(sanitizedHeaders);
    }

    protected boolean isSensitiveHeader(String headerName) {
        return headerName.equalsIgnoreCase(AUTHORIZATION) || headerName.equalsIgnoreCase(COOKIE);
    }
}
