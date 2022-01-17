package truelayer.java.payments;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import truelayer.java.http.entities.ApiResponse;
import truelayer.java.payments.entities.CreatePaymentRequest;
import truelayer.java.payments.entities.CreatePaymentResponse;
import truelayer.java.payments.entities.GetPaymentByIdResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static retrofit2.Response.success;
import static truelayer.java.TestUtils.*;

class PaymentHandlerTests {

    @Test
    @DisplayName("It should yield a payment object when creating a payment")
    public void itShouldYieldAPaymentWhenCreatingAPayment() {
        var createPaymentResponse = buildCreatePaymentResponse();
        var paymentHandler = stubCreatePaymentResponse(createPaymentResponse);

        var response = paymentHandler.createPayment(
                CreatePaymentRequest.builder()
                        .amountInMinor(100)
                        .build());

        assertNotError(response);
        assertEquals(createPaymentResponse, response.getData());
    }

    @Test
    @DisplayName("It should yield a payment object when getting a payment by id")
    public void itShouldYieldAPaymentWhenGettingAPayment() {
        var payment = buildGetPaymentByIdResponse();
        var paymentHandler = stubGetPaymentByIdResponse(payment);

        var response = paymentHandler.getPayment("a-payment-id");

        assertNotError(response);
        assertEquals(payment, response.getData());
    }

    private PaymentHandler stubCreatePaymentResponse(CreatePaymentResponse paymentResponse) {

        var paymentsApi = Mockito.mock(IPaymentsApi.class);
        var apiResponse = stubApiResponse(
                success(
                        ApiResponse.<CreatePaymentResponse>builder()
                                .data(paymentResponse)
                                .build()
                )
        );
        when(paymentsApi.createPayment(any(CreatePaymentRequest.class)))
                .thenReturn(
                        apiResponse
                );
        return PaymentHandler.builder()
                .paymentsApi(paymentsApi)
                .build();
    }

    private PaymentHandler stubGetPaymentByIdResponse(GetPaymentByIdResponse paymentResponse) {
        var paymentsApi = Mockito.mock(IPaymentsApi.class);
        var apiResponse = stubApiResponse(
                success(
                        ApiResponse.<GetPaymentByIdResponse>builder()
                                .data(paymentResponse)
                                .build()
                )
        );
        when(paymentsApi.getPayment(anyString()))
                .thenReturn(
                        apiResponse
                );
        return PaymentHandler.builder()
                .paymentsApi(paymentsApi)
                .build();
    }
}