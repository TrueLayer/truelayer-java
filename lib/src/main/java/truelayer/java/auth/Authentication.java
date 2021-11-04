package truelayer.java.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.Builder;
import org.apache.http.HttpHeaders;
import org.apache.http.entity.ContentType;
import truelayer.java.auth.entities.AccessToken;
import truelayer.java.auth.exceptions.AuthenticationException;

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
//todo add validation on the built object
public class Authentication implements IAuthentication{

    private String clientId;

    private String clientSecret;

    private final static String GRANT_TYPE_CLIENT_CREDENTIALS = "client_credentials";

    private final static String TOKEN_ENDPOINT = "https://auth.t7r.dev/connect/token";

    @Override
    public AccessToken getOauthToken(List<String> scopes) throws AuthenticationException{
        var formData = new HashMap<String, String>();
        formData.put("client_id", clientId);
        formData.put("client_secret", clientSecret);
        formData.put("grant_type", GRANT_TYPE_CLIENT_CREDENTIALS);
        formData.put("scope", scopes.stream().collect(joining(" ")));

        //todo: inject this client at construction time: + testability, + flexibility
        var request = HttpRequest.newBuilder()
                .uri(URI.create(TOKEN_ENDPOINT))
                .header(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_FORM_URLENCODED.toString())
                .POST(ofFormData(formData))
                .build();

        var httpClient = HttpClient.newHttpClient();
        try {
            var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if(response.statusCode() >= 400)
                throw new AuthenticationException(String.valueOf(response.statusCode()), response.body());

            return new ObjectMapper().readValue(response.body(), AccessToken.class);
        } catch (Exception e) {
            throw new AuthenticationException(e);
        }
    }

    private static HttpRequest.BodyPublisher ofFormData(Map<String, String> data) {
        var formUrlEncoded = data.keySet().stream()
                .map(key -> key + "=" + URLEncoder.encode(data.get(key), StandardCharsets.UTF_8))
                .collect(joining("&"));
        return HttpRequest.BodyPublishers.ofString(formUrlEncoded);
    }
}
