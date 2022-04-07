package com.truelayer.java.integration;

import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.truelayer.java.TestUtils.assertNotError;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.truelayer.java.TestUtils;
import com.truelayer.java.TestUtils.RequestStub;
import com.truelayer.java.http.entities.ApiResponse;
import com.truelayer.java.mandates.entities.CreateMandateRequest;
import com.truelayer.java.mandates.entities.CreateMandateResponse;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Mandates integration tests")
public class MandatesIntegrationTests extends IntegrationTests {

    @SneakyThrows
    @Test
    @DisplayName("It should create a mandate")
    public void shouldCreateAMandate() {
        String jsonResponseFile = "mandates/201.create_mandate.json";
        RequestStub.New()
                .method("post")
                .path(urlPathEqualTo("/connect/token"))
                .status(200)
                .bodyFile("auth/200.access_token.json")
                .build();
        RequestStub.New()
                .method("post")
                .path(urlPathEqualTo("/mandates"))
                .withAuthorization()
                .withSignature()
                .status(201)
                .bodyFile(jsonResponseFile)
                .build();
        CreateMandateRequest createMandateRequest =
                CreateMandateRequest.builder().build();

        ApiResponse<CreateMandateResponse> response =
                tlClient.mandates().createMandate(createMandateRequest).get();

        assertNotError(response);
        CreateMandateResponse expected = TestUtils.deserializeJsonFileTo(jsonResponseFile, CreateMandateResponse.class);
        assertEquals(expected, response.getData());
    }

    @Test
    @Disabled
    @DisplayName("It should start an authorization flow")
    public void shouldStartAnAuthorizationFlow() {}

    @Test
    @Disabled
    @DisplayName("It should submit a provider selection")
    public void shouldSubmitProviderSelection() {}

    @Test
    @Disabled
    @DisplayName("It should get a mandate by id")
    public void shouldGetAMandateById() {}
}
