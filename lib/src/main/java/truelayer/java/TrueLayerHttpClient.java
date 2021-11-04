package truelayer.java;

import lombok.Builder;
import org.apache.http.HttpHeaders;
import org.apache.http.entity.ContentType;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.joining;

@Builder
public class TrueLayerHttpClient {

    private final static String GRANT_TYPE_CLIENT_CREDENTIALS = "client_credentials";
    private final static String TOKEN_ENDPOINT = "https://auth.t7r.dev/connect/token";

    private String clientId;
    private String clientSecret;

    private HttpClient httpClient;

    public HttpResponse<String> sendOauthRequest(List<String> scopes) throws IOException, InterruptedException {
        var formData = new HashMap<String, String>();
        formData.put("client_id", clientId);
        formData.put("client_secret", clientSecret);
        formData.put("grant_type", GRANT_TYPE_CLIENT_CREDENTIALS);
        formData.put("scope", scopes.stream().collect(joining(" ")));

        var request = HttpRequest.newBuilder()
                .uri(URI.create(TOKEN_ENDPOINT))
                .header(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_FORM_URLENCODED.toString())
                .POST(ofFormData(formData))
                .build();

        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }


    //todo unit test
    private static HttpRequest.BodyPublisher ofFormData(Map<String, String> data) {
        var formUrlEncoded = data.keySet().stream()
                .map(key -> key + "=" + URLEncoder.encode(data.get(key), StandardCharsets.UTF_8))
                .collect(joining("&"));
        return HttpRequest.BodyPublishers.ofString(formUrlEncoded);
    }

}
