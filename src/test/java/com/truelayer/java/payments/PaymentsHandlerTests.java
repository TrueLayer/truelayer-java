package com.truelayer.java.payments;

import static com.truelayer.java.TestUtils.buildTestHeaders;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.truelayer.java.http.entities.Headers;
import com.truelayer.java.payments.entities.CreatePaymentRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class PaymentsHandlerTests {
    // TODO: complete

    @Test
    @DisplayName("It should call the create payment endpoint")
    public void shouldCallCreatePayment() {
        IPaymentsApi paymentsApi = Mockito.mock(IPaymentsApi.class);
        PaymentsHandler sut = new PaymentsHandler(paymentsApi);
        CreatePaymentRequest request = CreatePaymentRequest.builder().build();

        sut.createPayment(request);

        verify(paymentsApi, times(1)).createPayment(null, request);
    }

    @Test
    @DisplayName("It should call the create payment endpoint with additional headers")
    public void shouldCallCreatePaymentWithAdditionalHeaders() {
        IPaymentsApi paymentsApi = Mockito.mock(IPaymentsApi.class);
        PaymentsHandler sut = new PaymentsHandler(paymentsApi);
        Headers customHeaders = buildTestHeaders();
        CreatePaymentRequest request = CreatePaymentRequest.builder().build();

        sut.createPayment(customHeaders, request);

        verify(paymentsApi, times(1)).createPayment(customHeaders, request);
    }
}
