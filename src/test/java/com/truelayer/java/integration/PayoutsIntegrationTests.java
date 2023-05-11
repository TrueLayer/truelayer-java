package com.truelayer.java.integration;

import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.truelayer.java.TestUtils.assertNotError;
import static org.junit.jupiter.api.Assertions.*;

import com.truelayer.java.TestUtils;
import com.truelayer.java.TestUtils.RequestStub;
import com.truelayer.java.http.entities.ApiResponse;
import com.truelayer.java.http.entities.ProblemDetails;
import com.truelayer.java.payouts.entities.CreatePayoutRequest;
import com.truelayer.java.payouts.entities.CreatePayoutResponse;
import com.truelayer.java.payouts.entities.Payout;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("Payouts integration tests")
public class PayoutsIntegrationTests extends IntegrationTests {

    private static final String A_PAYOUT_ID = "a-payout-id";

    @Test
    @DisplayName("It should create a payout")
    @SneakyThrows
    public void shouldCreateAPayout() {
        String jsonResponseFile = "payouts/202.create_payout.json";
        RequestStub.New()
                .method("post")
                .path(urlPathEqualTo("/connect/token"))
                .status(200)
                .bodyFile("auth/200.access_token.json")
                .build();
        RequestStub.New()
                .method("post")
                .path(urlPathEqualTo("/payouts"))
                .withAuthorization()
                .withSignature()
                .withIdempotencyKey()
                .status(202)
                .bodyFile(jsonResponseFile)
                .build();
        CreatePayoutRequest payoutRequest = CreatePayoutRequest.builder().build();

        ApiResponse<CreatePayoutResponse> response =
                tlClient.payouts().createPayout(payoutRequest).get();

        assertNotError(response);
        CreatePayoutResponse expected = TestUtils.deserializeJsonFileTo(jsonResponseFile, CreatePayoutResponse.class);
        assertEquals(expected, response.getData());
    }

    @DisplayName("It should get payout details")
    @ParameterizedTest(name = "of a payout with status {0}")
    @ValueSource(strings = {"PENDING", "AUTHORIZED", "EXECUTED", "FAILED"})
    @SneakyThrows
    public void shouldReturnPayoutDetails(Payout.Status expectedStatus) {
        String jsonResponseFile = "payouts/200.get_payout_by_id." + expectedStatus.getStatus() + ".json";
        RequestStub.New()
                .method("post")
                .path(urlPathEqualTo("/connect/token"))
                .status(200)
                .bodyFile("auth/200.access_token.json")
                .build();
        RequestStub.New()
                .method("get")
                .path(urlPathEqualTo("/payouts/" + A_PAYOUT_ID))
                .withAuthorization()
                .status(200)
                .bodyFile(jsonResponseFile)
                .build();

        ApiResponse<Payout> response = tlClient.payouts().getPayout(A_PAYOUT_ID).get();

        assertNotError(response);
        Payout expected = TestUtils.deserializeJsonFileTo(jsonResponseFile, Payout.class);
        assertEquals(expectedStatus, response.getData().getStatus());
        assertEquals(expected, response.getData());
    }

    @Test
    @DisplayName("It should throw an error if a payout is not found")
    @SneakyThrows
    public void shouldThrowIfPayoutNotFound() {
        String jsonResponseFile = "payouts/404.payout_not_found.json";
        String anInvalidPayoutId = "an-invalid-payout-id";
        RequestStub.New()
                .method("post")
                .path(urlPathEqualTo("/connect/token"))
                .status(200)
                .bodyFile("auth/200.access_token.json")
                .build();
        RequestStub.New()
                .method("get")
                .path(urlPathEqualTo("/payouts/" + anInvalidPayoutId))
                .withAuthorization()
                .status(404)
                .bodyFile(jsonResponseFile)
                .build();

        ApiResponse<Payout> response =
                tlClient.payouts().getPayout(anInvalidPayoutId).get();

        assertTrue(response.isError());
        ProblemDetails expected = TestUtils.deserializeJsonFileTo(jsonResponseFile, ProblemDetails.class);
        assertEquals(expected, response.getError());
    }
}
