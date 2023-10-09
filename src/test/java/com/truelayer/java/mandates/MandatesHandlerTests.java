package com.truelayer.java.mandates;

import static com.truelayer.java.Constants.Scopes.PAYMENTS;
import static com.truelayer.java.Constants.Scopes.RECURRING_PAYMENTS_SWEEPING;
import static com.truelayer.java.TestUtils.buildTestHeaders;
import static com.truelayer.java.http.mappers.HeadersMapper.toMap;
import static java.util.Collections.emptyMap;
import static org.mockito.Mockito.*;

import com.truelayer.java.entities.RequestScopes;
import com.truelayer.java.http.entities.Headers;
import com.truelayer.java.mandates.entities.CreateMandateRequest;
import com.truelayer.java.mandates.entities.ListMandatesQuery;
import com.truelayer.java.payments.entities.StartAuthorizationFlowRequest;
import com.truelayer.java.payments.entities.SubmitProviderSelectionRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class MandatesHandlerTests {
    private static final String A_MANDATE_ID = "a-mandate-id";
    private static final String A_CURSOR = "a-cursor";
    private static final String A_USER_ID = "a-user-id";
    private static final int A_LIMIT = 15;
    private static final String AN_AMOUNT_IN_MINOR = "10";
    private static final String A_CURRENCY = "GBP";
    private static final RequestScopes SCOPES = RequestScopes.builder()
            .scope(PAYMENTS)
            .scope(RECURRING_PAYMENTS_SWEEPING)
            .scope("a-custom-scope")
            .build();

    private MandatesHandler sut;
    private IMandatesApi mandatesApiMock;

    @BeforeEach
    public void setup() {
        mandatesApiMock = Mockito.mock(IMandatesApi.class);
        sut = MandatesHandler.builder()
                .mandatesApi(mandatesApiMock)
                .scopes(SCOPES)
                .build();
    }

    @Test
    @DisplayName("It should call the create mandate endpoint with empty headers map")
    public void shouldCallCreateMandate() {
        CreateMandateRequest request = CreateMandateRequest.builder().build();

        sut.createMandate(request);

        verify(mandatesApiMock, times(1)).createMandate(SCOPES, emptyMap(), request);
    }

    @Test
    @DisplayName("It should call the create mandate endpoint with custom headers")
    public void shouldCallCreateMandateWithCustomHeaders() {
        CreateMandateRequest request = CreateMandateRequest.builder().build();
        Headers customHeaders = buildTestHeaders();

        sut.createMandate(customHeaders, request);

        verify(mandatesApiMock, times(1)).createMandate(SCOPES, toMap(customHeaders), request);
        verifyNoMoreInteractions(mandatesApiMock);
    }

    @Test
    @DisplayName("It should call the create mandate endpoint with the default scopes")
    public void shouldCallCreateAMandateWithDefaultScopes() {
        MandatesHandler sut =
                MandatesHandler.builder().mandatesApi(mandatesApiMock).build();
        CreateMandateRequest request = CreateMandateRequest.builder().build();

        sut.createMandate(request);

        RequestScopes expectedDefaultScopes =
                RequestScopes.builder().scope(RECURRING_PAYMENTS_SWEEPING).build();
        verify(mandatesApiMock, times(1)).createMandate(expectedDefaultScopes, emptyMap(), request);
    }

    @Test
    @DisplayName("It should call the start authorization flow endpoint with empty headers map")
    public void shouldCallStartAuthFlow() {
        StartAuthorizationFlowRequest request =
                StartAuthorizationFlowRequest.builder().build();

        sut.startAuthorizationFlow(A_MANDATE_ID, request);

        verify(mandatesApiMock, times(1)).startAuthorizationFlow(SCOPES, emptyMap(), A_MANDATE_ID, request);
    }

    @Test
    @DisplayName("It should call the start authorization flow endpoint with custom headers")
    public void shouldCallStartAuthFlowWithCustomHeaders() {
        Headers customHeaders = buildTestHeaders();
        StartAuthorizationFlowRequest request =
                StartAuthorizationFlowRequest.builder().build();

        sut.startAuthorizationFlow(customHeaders, A_MANDATE_ID, request);

        verify(mandatesApiMock, times(1)).startAuthorizationFlow(SCOPES, toMap(customHeaders), A_MANDATE_ID, request);
    }

    @Test
    @DisplayName("It should call the submit provider endpoint with empty headers map")
    public void shouldCallSubmitProviderEndpoint() {
        SubmitProviderSelectionRequest request =
                SubmitProviderSelectionRequest.builder().build();

        sut.submitProviderSelection(A_MANDATE_ID, request);

        verify(mandatesApiMock, times(1)).submitProviderSelection(SCOPES, emptyMap(), A_MANDATE_ID, request);
    }

    @Test
    @DisplayName("It should call the submit provider endpoint with custom headers")
    public void shouldCallSubmitProviderEndpointWithCustomHeaders() {
        Headers customHeaders = buildTestHeaders();
        SubmitProviderSelectionRequest request =
                SubmitProviderSelectionRequest.builder().build();

        sut.submitProviderSelection(customHeaders, A_MANDATE_ID, request);

        verify(mandatesApiMock, times(1)).submitProviderSelection(SCOPES, toMap(customHeaders), A_MANDATE_ID, request);
    }

    @Test
    @DisplayName("It should call the list mandates endpoint without parameters")
    public void shouldCallListMandatesEndpointWithoutParams() {
        sut.listMandates();

        verify(mandatesApiMock, times(1)).listMandates(SCOPES, null, null, null);
    }

    @Test
    @DisplayName("It should call the list mandates endpoint")
    public void shouldCallListMandatesEndpoint() {
        ListMandatesQuery query = ListMandatesQuery.builder()
                .userId(A_USER_ID)
                .cursor(A_CURSOR)
                .limit(A_LIMIT)
                .build();

        sut.listMandates(query);

        verify(mandatesApiMock, times(1)).listMandates(SCOPES, A_USER_ID, A_CURSOR, A_LIMIT);
    }

    @Test
    @DisplayName("It should call the get mandate endpoint")
    public void shouldCallGetMandatesEndpoint() {
        sut.getMandate(A_MANDATE_ID);

        verify(mandatesApiMock, times(1)).getMandate(SCOPES, A_MANDATE_ID);
    }

    @Test
    @DisplayName("It should call the revoke mandate endpoint with empty headers map")
    public void shouldCallRevokeMandatesEndpoint() {
        sut.revokeMandate(A_MANDATE_ID);

        verify(mandatesApiMock, times(1)).revokeMandate(SCOPES, emptyMap(), A_MANDATE_ID);
    }

    @Test
    @DisplayName("It should call the revoke mandate endpoint with custom headers")
    public void shouldCallRevokeMandatesEndpointWithCustomHeaders() {
        Headers customHeaders = buildTestHeaders();

        sut.revokeMandate(customHeaders, A_MANDATE_ID);

        verify(mandatesApiMock, times(1)).revokeMandate(SCOPES, toMap(customHeaders), A_MANDATE_ID);
    }

    @Test
    @DisplayName("It should call the CoF endpoint")
    public void shouldCallCOFEndpoint() {
        sut.getConfirmationOfFunds(A_MANDATE_ID, AN_AMOUNT_IN_MINOR, A_CURRENCY);

        verify(mandatesApiMock, times(1)).getConfirmationOfFunds(SCOPES, A_MANDATE_ID, AN_AMOUNT_IN_MINOR, A_CURRENCY);
    }

    @Test
    @DisplayName("It should call the Constraints endpoint")
    public void shouldCallConstraintsEndpoint() {
        sut.getMandateConstraints(A_MANDATE_ID);

        verify(mandatesApiMock, times(1)).getMandateConstraints(SCOPES, A_MANDATE_ID);
    }
}
