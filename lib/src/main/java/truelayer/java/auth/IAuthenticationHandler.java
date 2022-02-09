package truelayer.java.auth;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import truelayer.java.auth.entities.AccessToken;
import truelayer.java.http.entities.ApiResponse;

/**
 * Exposes all the authentication related capabilities of the library.
 *
 * @see <a href="https://docs.truelayer.com/reference/generateaccesstoken"><i>Authentication</i> API reference</a>
 */
public interface IAuthenticationHandler {

    /**
     * Gets an <i>OAuth</i> access token for the given scope(s).
     *
     * @param scopes a list of scopes. Might contain a single element.
     * @return the response of the <i>Generate Access token</i> operation
     * @see <a href="https://docs.truelayer.com/reference/generateaccesstoken"><i>Generate Access token</i> API reference</a>
     */
    CompletableFuture<ApiResponse<AccessToken>> getOauthToken(List<String> scopes);
}
