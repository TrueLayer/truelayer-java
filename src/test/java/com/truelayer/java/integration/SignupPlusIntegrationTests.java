package com.truelayer.java.integration;

import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.truelayer.java.Constants.Scopes.SIGNUP_PLUS;
import static com.truelayer.java.TestUtils.assertNotError;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.truelayer.java.TestUtils.RequestStub;
import com.truelayer.java.http.entities.ApiResponse;
import com.truelayer.java.signupplus.entities.*;
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
    public void shouldGetUserDataByPaymentUk() {
        String jsonResponseFile = "signup_plus/200.get_user_data_by_payment.gb.json";
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

        Assertions.assertEquals("Mr", response.getData().getTitle().orElseThrow());
        Assertions.assertEquals("Sherlock", response.getData().getFirstName().orElseThrow());
        Assertions.assertEquals("Holmes", response.getData().getLastName().orElseThrow());
        Assertions.assertEquals(
                "1854-01-06", response.getData().getDateOfBirth().orElseThrow());
        assertTrue(response.getData().getSex().isEmpty());
        assertTrue(response.getData().getNationalIdentificationNumber().isEmpty());
        Address address = response.getData().getAddress().orElseThrow();
        Assertions.assertEquals("221B Baker St", address.getAddressLine1().orElseThrow());
        Assertions.assertEquals("Flat 2", address.getAddressLine2().orElseThrow());
        Assertions.assertEquals("London", address.getCity().orElseThrow());
        Assertions.assertEquals("Greater London", address.getState().orElseThrow());
        Assertions.assertEquals("NW1 6XE", address.getZip().orElseThrow());
        Assertions.assertEquals("GB", address.getCountry().orElseThrow());
    }

    @Test
    @DisplayName("It should get user data for a payment in Finland")
    @SneakyThrows
    public void shouldGetUserDataByPaymentFinland() {
        String jsonResponseFile = "signup_plus/200.get_user_data_by_payment.fi.json";
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

        assertTrue(response.getData().getTitle().isEmpty());
        Assertions.assertEquals("Väinö", response.getData().getFirstName().orElseThrow());
        Assertions.assertEquals("Tunnistus", response.getData().getLastName().orElseThrow());
        Assertions.assertEquals(
                "1970-07-07", response.getData().getDateOfBirth().orElseThrow());
        Assertions.assertEquals(Sex.Male, response.getData().getSex().orElseThrow());
        Assertions.assertEquals(
                "070770-905D",
                response.getData().getNationalIdentificationNumber().orElseThrow());
        Address address = response.getData().getAddress().orElseThrow();
        Assertions.assertEquals("Sepänkatu 11 A 5", address.getAddressLine1().orElseThrow());
        assertTrue(address.getAddressLine2().isEmpty());
        Assertions.assertEquals("KUOPIO", address.getCity().orElseThrow());
        assertTrue(address.getState().isEmpty());
        Assertions.assertEquals("70100", address.getZip().orElseThrow());
        Assertions.assertEquals("FI", address.getCountry().orElseThrow());
    }

    @Test
    @DisplayName("It should generate an auth URI for a payment in Finland")
    @SneakyThrows
    public void shouldGenerateAuthUriByPaymentFinland() {
        String jsonResponseFile = "signup_plus/200.generate_auth_uri.json";
        RequestStub.New()
                .method("post")
                .path(urlPathEqualTo("/connect/token"))
                .status(200)
                .bodyFile("auth/200.access_token.json")
                .build();
        RequestStub.New()
                .method("post")
                .path(urlPathEqualTo("/signup-plus/authuri"))
                .withAuthorization()
                .status(200)
                .bodyFile(jsonResponseFile)
                .build();

        ApiResponse<GenerateAuthUriResponse> response = tlClient.signupPlus()
                .generateAuthUri(
                        GenerateAuthUriRequest.builder().paymentId(A_PAYMENT_ID).build())
                .get();

        verifyGeneratedToken(Collections.singletonList(SIGNUP_PLUS));
        assertNotError(response);
        assertNotNull(response.getData().getAuthUri());
        assertTrue(response.getData().getAuthUri().toString().contains("truelayer.com"));
    }
}
