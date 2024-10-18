package com.truelayer.java.signupplus;

import static com.truelayer.java.Constants.Scopes.SIGNUP_PLUS;

import com.truelayer.java.IAuthenticatedHandler;
import com.truelayer.java.entities.RequestScopes;
import com.truelayer.java.http.entities.ApiResponse;
import com.truelayer.java.signupplus.entities.UserData;
import java.util.concurrent.CompletableFuture;
import lombok.Builder;

@Builder
public class SignupPlusHandler implements IAuthenticatedHandler, ISignupPlusHandler {

    private ISignupPlusApi signupPlusApi;

    @Builder.Default
    private RequestScopes scopes = RequestScopes.builder().scope(SIGNUP_PLUS).build();

    @Override
    public RequestScopes getRequestScopes() {
        return scopes;
    }

    @Override
    public CompletableFuture<ApiResponse<UserData>> getUserDataByPayment(String paymentId) {
        return signupPlusApi.getUserDataByPayment(getRequestScopes(), paymentId);
    }
}
