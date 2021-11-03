package truelayer.java.auth;

import com.google.gson.Gson;
import org.apache.commons.configuration2.ex.ConfigurationException;
import truelayer.java.Configuration;
import truelayer.java.auth.entities.AccessToken;
import truelayer.java.auth.exceptions.AuthenticationException;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Authentication implements IAuthentication{

    private Configuration configuration;

    public Authentication() throws ConfigurationException {
        this.configuration = new Configuration();
    }

    @Override
    public AccessToken getOauthToken(String grantType, String scope, String clientId, String clientSecret) throws AuthenticationException{
        var formData = new HashMap<String, String>();
        formData.put("client_id", clientId);
        formData.put("client_secret", clientSecret);
        formData.put("grant_type", grantType);
        formData.put("scope", scope);

        var request = HttpRequest.newBuilder()
                .uri(URI.create(configuration.getTokenEndpoint()))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(ofFormData(formData))
                .build();

        var httpClient = HttpClient.newHttpClient();
        try {
            var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if(response.statusCode() >= 400)
                throw new AuthenticationException(String.valueOf(response.statusCode()), response.body());
            return new Gson().fromJson(response.body(), AccessToken.class);
        } catch (Exception e) {
            throw new AuthenticationException(e);
        }
    }

    private static HttpRequest.BodyPublisher ofFormData(Map<String, String> data) {
        var formUrlEncoded = data.keySet().stream()
                .map(key -> key + "=" + URLEncoder.encode(data.get(key), StandardCharsets.UTF_8))
                .collect(Collectors.joining("&"));
        return HttpRequest.BodyPublishers.ofString(formUrlEncoded);
    }
}
