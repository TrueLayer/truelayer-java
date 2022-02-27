package truelayer.java.http.interceptors.logging;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum HttpLogPrefix {
    INCOMING("<--"),
    OUTGOING("-->");

    private final String prefix;

    @Override
    public String toString() {
        return prefix;
    }
}
