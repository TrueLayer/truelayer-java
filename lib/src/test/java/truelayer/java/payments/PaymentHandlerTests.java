package truelayer.java.payments;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import truelayer.java.TestUtils;
import truelayer.java.auth.AuthenticationHandler;
import truelayer.java.http.adapters.ApiCall;
import truelayer.java.http.entities.ApiResponse;
import truelayer.java.payments.entities.CreatePaymentRequest;
import truelayer.java.payments.entities.CreatePaymentResponse;
import truelayer.java.payments.entities.GetPaymentByIdResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static retrofit2.Response.success;
import static truelayer.java.TestUtils.getClientCredentials;
import static truelayer.java.TestUtils.stubApiResponse;

class PaymentHandlerTests {

    @Test
    @DisplayName("It should yield a payment object when creating a payment")
    public void itShouldYieldAPaymentWhenCreatingAPayment() {
        var createPaymentResponse = TestUtils.buildCreatePaymentResponse();
        var paymentHandler = stubCreatePaymentResponse(createPaymentResponse);

        var response = paymentHandler.createPayment(CreatePaymentRequest.builder()
                .amountInMinor(100)
                .build());

        assertFalse(response.isError());
        assertEquals(createPaymentResponse, response.getData());
    }

    @Test
    @DisplayName("It should yield a payment object when getting a payment by id")
    public void itShouldYieldAPaymentWhenGettingAPayment() {
        var payment = TestUtils.buildGetPaymentByIdResponse();
        var paymentHandler = stubGetPaymentByIdResponse(payment);

        var response = paymentHandler.getPayment("a-payment-id");

        assertFalse(response.isError());
        assertEquals(payment, response.getData());
    }

    private PaymentHandler stubCreatePaymentResponse(CreatePaymentResponse paymentResponse) {
        var apiResponse = ApiResponse.<CreatePaymentResponse>builder()
                .data(paymentResponse)
                .build();

        return createStub(new IPaymentsApi() {
            @Override
            public ApiCall<ApiResponse<CreatePaymentResponse>> createPayment(String authorization, CreatePaymentRequest body) {
                return stubApiResponse(success(apiResponse));
            }

            @Override
            public ApiCall<ApiResponse<GetPaymentByIdResponse>> getPayment(String authorization, String paymentId) {
                return null;
            }
        });
    }

    private PaymentHandler stubGetPaymentByIdResponse(GetPaymentByIdResponse paymentResponse) {
        var apiResponse = ApiResponse.<GetPaymentByIdResponse>builder()
                .data(paymentResponse)
                .build();

        return createStub(new IPaymentsApi() {
            @Override
            public ApiCall<ApiResponse<CreatePaymentResponse>> createPayment(String authorization, CreatePaymentRequest body) {
                return null;
            }

            @Override
            public ApiCall<ApiResponse<GetPaymentByIdResponse>> getPayment(String authorization, String paymentId) {
                return stubApiResponse(success(apiResponse));
            }
        });
    }

    private PaymentHandler createStub(IPaymentsApi paymentsApi){
        var authenticationHandler = AuthenticationHandler.builder()
                .authenticationApi((clientId, clientSecret, grantType, scopes) ->
                        stubApiResponse(success(TestUtils.buildAccessToken())))
                .clientCredentials(getClientCredentials())
                .build();
        return PaymentHandler.builder()
                .authenticationHandler(authenticationHandler)
                .paymentsScopes(new String[]{"a-scope"})
                .paymentsApi(paymentsApi)
                .build();
    }
}