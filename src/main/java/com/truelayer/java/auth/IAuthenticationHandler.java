package com.truelayer.java.auth;

import com.truelayer.java.auth.entities.AccessToken;
import com.truelayer.java.http.entities.ApiResponse;
import java.util.List;
import java.util.concurrent.CompletableFuture;

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
    CompletableFuture<ApiResponse<AccessToken>> getOauthTokenAsync(List<String> scopes);

    // todo: remove. test only
    ApiResponse<AccessToken> getOauthToken(List<String> scopes);
}
