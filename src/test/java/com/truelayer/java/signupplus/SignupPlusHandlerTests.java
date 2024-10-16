package com.truelayer.java.signupplus;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.truelayer.java.Constants;
import com.truelayer.java.entities.RequestScopes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class SignupPlusHandlerTests {

    private static final String A_PAYMENT_ID = "a-payment-id";
    private static final RequestScopes SCOPES = RequestScopes.builder()
            .scope(Constants.Scopes.SIGNUP_PLUS)
            .scope("a-custom-scope")
            .build();

    private SignupPlusHandler sut;
    private ISignupPlusApi signupPlusApiMock;

    @BeforeEach
    public void setup() {
        signupPlusApiMock = Mockito.mock(ISignupPlusApi.class);
        sut = SignupPlusHandler.builder()
                .signupPlusApi(signupPlusApiMock)
                .scopes(SCOPES)
                .build();
    }

    @Test
    @DisplayName("It should call the get user data by payment endpoint")
    public void shouldCallCreatePayoutEndpoint() {
        sut.getUserDataByPayment(A_PAYMENT_ID);

        verify(signupPlusApiMock, times(1)).getUserDataByPayment(SCOPES, A_PAYMENT_ID);
    }
}
