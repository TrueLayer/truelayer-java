package truelayer.java.signing;

import java.util.Base64;
import java.util.Map;
import java.util.stream.Collectors;

public class Utils {

    public static String parseHeaders(Map<String, String> headers) {
        return headers.keySet().stream()
                .map(key -> key + ": " + headers.get(key))
                .collect(Collectors.joining("\n"));
    }

    public static String buildPayload(String httpMethod, String path, String body, Map<String, String> headers) {
        var builder = new StringBuilder(httpMethod.toUpperCase() + " " + path);
        if(headers != null)
            builder.append("\n")
                    .append(parseHeaders(headers));

        if(body != null)
            builder.append("\n")
                    .append(body);

        return Base64.getUrlEncoder().encodeToString(builder.toString().getBytes());
    }
}
