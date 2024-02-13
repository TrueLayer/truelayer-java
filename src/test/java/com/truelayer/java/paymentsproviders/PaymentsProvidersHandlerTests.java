package com.truelayer.java.paymentsproviders;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.truelayer.java.Constants;
import com.truelayer.java.entities.RequestScopes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class PaymentsProvidersHandlerTests {

    private static final String A_PROVIDER_ID = "a-provider-id";
    private static final RequestScopes SCOPES =
            RequestScopes.builder().scope("a-custom-scope").build();

    private PaymentsProvidersHandler sut;
    private IPaymentsProvidersApi paymentsProvidersApiMock;

    @BeforeEach
    public void setup() {
        paymentsProvidersApiMock = Mockito.mock(IPaymentsProvidersApi.class);
        sut = PaymentsProvidersHandler.builder()
                .paymentsProvidersApi(paymentsProvidersApiMock)
                .scopes(SCOPES)
                .build();
    }

    @Test
    @DisplayName("It should call the get payments-providers by id endpoint with the default scopes")
    public void shouldCallCreatePaymentWithDefaultScopes() {
        PaymentsProvidersHandler sut = PaymentsProvidersHandler.builder()
                .paymentsProvidersApi(paymentsProvidersApiMock)
                .build();

        sut.getProvider(A_PROVIDER_ID);

        RequestScopes expectedDefaultScopes =
                RequestScopes.builder().scope(Constants.Scopes.PAYMENTS).build();
        verify(paymentsProvidersApiMock, times(1)).getProvider(expectedDefaultScopes, A_PROVIDER_ID);
    }

    @Test
    @DisplayName("It should call the get payments-providers by id endpoint")
    public void shouldCallGetPaymentById() {
        sut.getProvider(A_PROVIDER_ID);

        verify(paymentsProvidersApiMock, times(1)).getProvider(SCOPES, A_PROVIDER_ID);
    }
}
