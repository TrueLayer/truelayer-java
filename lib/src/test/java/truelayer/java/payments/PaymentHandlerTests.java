package truelayer.java.payments;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import retrofit2.Call;
import truelayer.java.TestUtils;
import truelayer.java.auth.AuthenticationHandler;
import truelayer.java.payments.entities.CreatePaymentRequest;
import truelayer.java.payments.entities.Payment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static retrofit2.Response.success;
import static truelayer.java.TestUtils.getClientCredentialsOptions;
import static truelayer.java.TestUtils.stubApiResponse;

class PaymentHandlerTests {

    @Test
    @DisplayName("It should yield a payment object when creating a payment")
    public void itShouldYieldAPaymentWhenCreatingAPayment() {
        var payment = TestUtils.buildPayment();
        var paymentHandler = buildPaymentHandlerWithPaymentResponse(payment);

        var actualPayment = paymentHandler.createPayment(CreatePaymentRequest.builder()
                .amountInMinor(100)
                .build());

        assertEquals(payment, actualPayment);
    }

    @Test
    @DisplayName("It should yield a payment object when getting a payment by id")
    public void itShouldYieldAPaymentWhenGettingAPayment() {
        var payment = TestUtils.buildPayment();
        var paymentHandler = buildPaymentHandlerWithPaymentResponse(payment);

        var actualPayment = paymentHandler.getPayment("a-payment-id");

        assertEquals(payment, actualPayment);
    }

    private PaymentHandler buildPaymentHandlerWithPaymentResponse(Payment payment) {
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
                    public Call<Payment> createPayment(String idempotencyKey, String signature, String authorization, CreatePaymentRequest body) {
                        return stubApiResponse(success(payment));
                    }

                    @Override
                    public Call<Payment> getPayment(String authorization, String paymentId) {
                        return stubApiResponse(success(payment));
                    }
                })
                .build();
    }
}