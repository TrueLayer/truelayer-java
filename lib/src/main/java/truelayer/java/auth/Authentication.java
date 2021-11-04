package truelayer.java.auth;

import com.google.gson.Gson;
import lombok.Builder;
import truelayer.java.TrueLayerHttpClient;
import truelayer.java.auth.entities.AccessToken;
import truelayer.java.auth.exceptions.AuthenticationException;

import java.util.List;

@Builder
//todo add validation on the built object
public class Authentication implements IAuthentication {

    private TrueLayerHttpClient httpClient;

    @Override
    public AccessToken getOauthToken(List<String> scopes) throws AuthenticationException {
        try {
            var response = httpClient.sendOauthRequest(scopes);
            if (response.statusCode() >= 400)
                throw new AuthenticationException(String.valueOf(response.statusCode()), response.body());
            return new Gson().fromJson(response.body(), AccessToken.class);
        } catch (Exception e) {
            throw new AuthenticationException(e);
        }
    }
}
