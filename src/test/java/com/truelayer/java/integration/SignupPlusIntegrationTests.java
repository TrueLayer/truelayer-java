package com.truelayer.java.integration;

import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.truelayer.java.Constants.Scopes.SIGNUP_PLUS;
import static com.truelayer.java.TestUtils.assertNotError;

import com.truelayer.java.TestUtils.RequestStub;
import com.truelayer.java.http.entities.ApiResponse;
import com.truelayer.java.signupplus.entities.UserData;
import java.util.Collections;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Signup+ integration tests")
public class SignupPlusIntegrationTests extends IntegrationTests {

    public static final String A_PAYMENT_ID = "a-payment-id";

    @Test
    @DisplayName("It should get user data for a payment in UK")
    @SneakyThrows
    public void shouldGetUserDataByPayment() {
        String jsonResponseFile = "signup_plus/get_user_data_by_payment.json";
        RequestStub.New()
                .method("post")
                .path(urlPathEqualTo("/connect/token"))
                .status(200)
                .bodyFile("auth/200.access_token.json")
                .build();
        RequestStub.New()
                .method("get")
                .path(urlPathEqualTo("/signup-plus/payments"))
                .withQueryParam("payment_id", A_PAYMENT_ID)
                .withAuthorization()
                .status(200)
                .bodyFile(jsonResponseFile)
                .build();

        ApiResponse<UserData> response =
                tlClient.signupPlus().getUserDataByPayment(A_PAYMENT_ID).get();

        verifyGeneratedToken(Collections.singletonList(SIGNUP_PLUS));
        assertNotError(response);

        Assertions.assertEquals("Mrs", response.getData().getTitle().orElseThrow());
    }
}
