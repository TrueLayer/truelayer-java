package truelayer.java.payments;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import retrofit2.Call;
import truelayer.java.TestUtils;
import truelayer.java.auth.AuthenticationHandler;
import truelayer.java.http.ApiResponse;
import truelayer.java.http.ApiResponseCallAdapter;
import truelayer.java.http.ApiResponseCallAdapter.ApiCall;
import truelayer.java.payments.entities.CreatePaymentRequest;
import truelayer.java.payments.entities.Payment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static retrofit2.Response.success;
import static truelayer.java.TestUtils.getClientCredentialsOptions;
import static truelayer.java.TestUtils.stubApiResponse;

class PaymentHandlerTests {

    @Test
    @DisplayName("It should yield a payment object when creating a payment")
    public void itShouldYieldAPaymentWhenCreatingAPayment() {
        var payment = TestUtils.buildPayment();
        var paymentHandler = buildPaymentHandlerWithPaymentResponse(payment);

        var response = paymentHandler.createPayment(CreatePaymentRequest.builder()
                .amountInMinor(100)
                .build());

        assertFalse(response.isError());
        assertEquals(payment, response.getData());
    }

    @Test
    @DisplayName("It should yield a payment object when getting a payment by id")
    public void itShouldYieldAPaymentWhenGettingAPayment() {
        var payment = TestUtils.buildPayment();
        var paymentHandler = buildPaymentHandlerWithPaymentResponse(payment);

        var response = paymentHandler.getPayment("a-payment-id");

        assertFalse(response.isError());
        assertEquals(payment, response.getData());
    }

    private PaymentHandler buildPaymentHandlerWithPaymentResponse(Payment payment) {
        var apiResponse = new ApiResponse<>(payment, null);

        var authenticationHandler = AuthenticationHandler.builder()
                .authenticationApi((clientId, clientSecret, grantType, scopes) ->
                        stubApiResponse(success(TestUtils.buildAccessToken())))
                .clientCredentialsOptions(getClientCredentialsOptions())
                .build();
        return PaymentHandler.builder()
                .authenticationHandler(authenticationHandler)
                .signingOptions(TestUtils.getSigningOptions())
                .paymentsScopes(new String[]{"a-scope"})
                .paymentsApi(new IPaymentsApi() {
                    @Override
                    public ApiCall<ApiResponse<Payment>> createPayment(String idempotencyKey, String signature, String authorization, CreatePaymentRequest body) {
                        return stubApiResponse(success(apiResponse));
                    }

                    @Override
                    public ApiCall<ApiResponse<Payment>> getPayment(String authorization, String paymentId) {
                        return stubApiResponse(success(apiResponse));
                    }
                })
                .build();
    }
}