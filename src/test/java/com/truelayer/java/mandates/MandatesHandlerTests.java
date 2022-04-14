package com.truelayer.java.mandates;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.truelayer.java.mandates.entities.CreateMandateRequest;
import com.truelayer.java.mandates.entities.ListMandatesQuery;
import com.truelayer.java.payments.entities.StartAuthorizationFlowRequest;
import com.truelayer.java.payments.entities.SubmitProviderSelectionRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class MandatesHandlerTests {
    private static String A_MANDATE_ID = "a-mandate-id";
    private static String A_CURSOR = "a-cursor";
    private static String A_USER_ID = "a-user-id";
    private static int A_LIMIT = 15;

    @Test
    @DisplayName("It should call the create mandate endpoint")
    public void shouldCallCreateMandate() {
        IMandatesApi mandatesApi = Mockito.mock(IMandatesApi.class);
        MandatesHandler sut = new MandatesHandler(mandatesApi);
        CreateMandateRequest request = CreateMandateRequest.builder().build();

        sut.createMandate(request);

        verify(mandatesApi, times(1)).createMandate(request);
    }

    @Test
    @DisplayName("It should call the start authorization flow endpoint")
    public void shouldCallStartAuthFlow() {
        IMandatesApi mandatesApi = Mockito.mock(IMandatesApi.class);
        MandatesHandler sut = new MandatesHandler(mandatesApi);
        StartAuthorizationFlowRequest request =
                StartAuthorizationFlowRequest.builder().build();

        sut.startAuthorizationFlow(A_MANDATE_ID, request);

        verify(mandatesApi, times(1)).startAuthorizationFlow(A_MANDATE_ID, request);
    }

    @Test
    @DisplayName("It should call the submit provider endpoint")
    public void shouldCallSubmitProviderEndpoint() {
        IMandatesApi mandatesApi = Mockito.mock(IMandatesApi.class);
        MandatesHandler sut = new MandatesHandler(mandatesApi);
        SubmitProviderSelectionRequest request =
                SubmitProviderSelectionRequest.builder().build();

        sut.submitProviderSelection(A_MANDATE_ID, request);

        verify(mandatesApi, times(1)).submitProviderSelection(A_MANDATE_ID, request);
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
    @DisplayName("It should call the revoke mandate endpoint")
    public void shouldCallRevokeMandatesEndpoint() {
        IMandatesApi mandatesApi = Mockito.mock(IMandatesApi.class);
        MandatesHandler sut = new MandatesHandler(mandatesApi);

        sut.revokeMandate(A_MANDATE_ID);

        verify(mandatesApi, times(1)).revokeMandate(A_MANDATE_ID);
    }
}
