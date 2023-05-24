package com.truelayer.java.mandates;

import static com.truelayer.java.TestUtils.buildTestHeaders;
import static com.truelayer.java.http.mappers.HeadersMapper.toMap;
import static java.util.Collections.emptyMap;
import static org.mockito.Mockito.*;

import com.truelayer.java.http.entities.Headers;
import com.truelayer.java.mandates.entities.CreateMandateRequest;
import com.truelayer.java.mandates.entities.ListMandatesQuery;
import com.truelayer.java.payments.entities.StartAuthorizationFlowRequest;
import com.truelayer.java.payments.entities.SubmitProviderSelectionRequest;
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

    @Test
    @DisplayName("It should call the create mandate endpoint with empty headers map")
    public void shouldCallCreateMandate() {
        IMandatesApi mandatesApi = Mockito.mock(IMandatesApi.class);
        MandatesHandler sut = new MandatesHandler(mandatesApi);
        CreateMandateRequest request = CreateMandateRequest.builder().build();

        sut.createMandate(request);

        verify(mandatesApi, times(1)).createMandate(emptyMap(), request);
    }

    @Test
    @DisplayName("It should call the create mandate endpoint with custom headers")
    public void shouldCallCreateMandateWithCustomHeaders() {
        IMandatesApi mandatesApi = Mockito.mock(IMandatesApi.class);
        MandatesHandler sut = new MandatesHandler(mandatesApi);
        CreateMandateRequest request = CreateMandateRequest.builder().build();
        Headers customHeaders = buildTestHeaders();

        sut.createMandate(customHeaders, request);

        verify(mandatesApi, times(1)).createMandate(toMap(customHeaders), request);
        verifyNoMoreInteractions(mandatesApi);
    }

    @Test
    @DisplayName("It should call the start authorization flow endpoint with empty headers map")
    public void shouldCallStartAuthFlow() {
        IMandatesApi mandatesApi = Mockito.mock(IMandatesApi.class);
        MandatesHandler sut = new MandatesHandler(mandatesApi);
        StartAuthorizationFlowRequest request =
                StartAuthorizationFlowRequest.builder().build();

        sut.startAuthorizationFlow(A_MANDATE_ID, request);

        verify(mandatesApi, times(1)).startAuthorizationFlow(emptyMap(), A_MANDATE_ID, request);
    }

    @Test
    @DisplayName("It should call the start authorization flow endpoint with custom headers")
    public void shouldCallStartAuthFlowWithCustomHeaders() {
        IMandatesApi mandatesApi = Mockito.mock(IMandatesApi.class);
        MandatesHandler sut = new MandatesHandler(mandatesApi);
        Headers customHeaders = buildTestHeaders();
        StartAuthorizationFlowRequest request =
                StartAuthorizationFlowRequest.builder().build();

        sut.startAuthorizationFlow(customHeaders, A_MANDATE_ID, request);

        verify(mandatesApi, times(1)).startAuthorizationFlow(toMap(customHeaders), A_MANDATE_ID, request);
    }

    @Test
    @DisplayName("It should call the submit provider endpoint with empty headers map")
    public void shouldCallSubmitProviderEndpoint() {
        IMandatesApi mandatesApi = Mockito.mock(IMandatesApi.class);
        MandatesHandler sut = new MandatesHandler(mandatesApi);
        SubmitProviderSelectionRequest request =
                SubmitProviderSelectionRequest.builder().build();

        sut.submitProviderSelection(A_MANDATE_ID, request);

        verify(mandatesApi, times(1)).submitProviderSelection(emptyMap(), A_MANDATE_ID, request);
    }

    @Test
    @DisplayName("It should call the submit provider endpoint with custom headers")
    public void shouldCallSubmitProviderEndpointWithCustomHeaders() {
        IMandatesApi mandatesApi = Mockito.mock(IMandatesApi.class);
        MandatesHandler sut = new MandatesHandler(mandatesApi);
        Headers customHeaders = buildTestHeaders();
        SubmitProviderSelectionRequest request =
                SubmitProviderSelectionRequest.builder().build();

        sut.submitProviderSelection(customHeaders, A_MANDATE_ID, request);

        verify(mandatesApi, times(1)).submitProviderSelection(toMap(customHeaders), A_MANDATE_ID, request);
    }

    @Test
    @DisplayName("It should call the list mandates endpoint without parameters")
    public void shouldCallListMandatesEndpointWithoutParams() {
        IMandatesApi mandatesApi = Mockito.mock(IMandatesApi.class);
        MandatesHandler sut = new MandatesHandler(mandatesApi);

        sut.listMandates();

        verify(mandatesApi, times(1)).listMandates(null, null, null);
    }

    @Test
    @DisplayName("It should call the list mandates endpoint")
    public void shouldCallListMandatesEndpoint() {
        IMandatesApi mandatesApi = Mockito.mock(IMandatesApi.class);
        MandatesHandler sut = new MandatesHandler(mandatesApi);
        ListMandatesQuery query = ListMandatesQuery.builder()
                .userId(A_USER_ID)
                .cursor(A_CURSOR)
                .limit(A_LIMIT)
                .build();

        sut.listMandates(query);

        verify(mandatesApi, times(1)).listMandates(A_USER_ID, A_CURSOR, A_LIMIT);
    }

    @Test
    @DisplayName("It should call the get mandate endpoint")
    public void shouldCallGetMandatesEndpoint() {
        IMandatesApi mandatesApi = Mockito.mock(IMandatesApi.class);
        MandatesHandler sut = new MandatesHandler(mandatesApi);

        sut.getMandate(A_MANDATE_ID);

        verify(mandatesApi, times(1)).getMandate(A_MANDATE_ID);
    }

    @Test
    @DisplayName("It should call the revoke mandate endpoint with empty headers map")
    public void shouldCallRevokeMandatesEndpoint() {
        IMandatesApi mandatesApi = Mockito.mock(IMandatesApi.class);
        MandatesHandler sut = new MandatesHandler(mandatesApi);

        sut.revokeMandate(A_MANDATE_ID);

        verify(mandatesApi, times(1)).revokeMandate(emptyMap(), A_MANDATE_ID);
    }

    @Test
    @DisplayName("It should call the revoke mandate endpoint with custom headers")
    public void shouldCallRevokeMandatesEndpointWithCustomHeaders() {
        IMandatesApi mandatesApi = Mockito.mock(IMandatesApi.class);
        MandatesHandler sut = new MandatesHandler(mandatesApi);
        Headers customHeaders = buildTestHeaders();

        sut.revokeMandate(customHeaders, A_MANDATE_ID);

        verify(mandatesApi, times(1)).revokeMandate(toMap(customHeaders), A_MANDATE_ID);
    }

    @Test
    @DisplayName("It should call the CoF endpoint")
    public void shouldCallCOFEndpoint() {
        IMandatesApi mandatesApi = Mockito.mock(IMandatesApi.class);
        MandatesHandler sut = new MandatesHandler(mandatesApi);

        sut.getConfirmationOfFunds(A_MANDATE_ID, AN_AMOUNT_IN_MINOR, A_CURRENCY);

        verify(mandatesApi, times(1)).getConfirmationOfFunds(A_MANDATE_ID, AN_AMOUNT_IN_MINOR, A_CURRENCY);
    }

    @Test
    @DisplayName("It should call the Constraints endpoint")
    public void shouldCallConstraintsEndpoint() {
        IMandatesApi mandatesApi = Mockito.mock(IMandatesApi.class);
        MandatesHandler sut = new MandatesHandler(mandatesApi);

        sut.getMandateConstraints(A_MANDATE_ID);

        verify(mandatesApi, times(1)).getMandateConstraints(A_MANDATE_ID);
    }
}
