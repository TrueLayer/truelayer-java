package com.truelayer.java.integration;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.truelayer.java.Constants.HeaderNames.X_DEVICE_USER_AGENT;
import static com.truelayer.java.Constants.HeaderNames.X_FORWARDED_FOR;
import static com.truelayer.java.Constants.Scopes.PAYMENTS;
import static com.truelayer.java.TestUtils.assertNotError;
import static com.truelayer.java.payments.entities.paymentdetail.Status.*;
import static com.truelayer.java.payments.entities.paymentmethod.PaymentMethod.Type.BANK_TRANSFER;
import static com.truelayer.java.payments.entities.paymentmethod.PaymentMethod.Type.MANDATE;
import static org.junit.jupiter.api.Assertions.*;

import com.truelayer.java.TestUtils;
import com.truelayer.java.TestUtils.RequestStub;
import com.truelayer.java.entities.CurrencyCode;
import com.truelayer.java.entities.ProviderFilter;
import com.truelayer.java.entities.RelatedProducts;
import com.truelayer.java.http.entities.ApiResponse;
import com.truelayer.java.http.entities.Headers;
import com.truelayer.java.http.entities.ProblemDetails;
import com.truelayer.java.payments.entities.*;
import com.truelayer.java.payments.entities.beneficiary.MerchantAccount;
import com.truelayer.java.payments.entities.paymentdetail.PaymentDetail;
import com.truelayer.java.payments.entities.paymentdetail.Status;
import com.truelayer.java.payments.entities.paymentmethod.PaymentMethod;
import com.truelayer.java.payments.entities.paymentrefund.PaymentRefund;
import com.truelayer.java.payments.entities.providerselection.ProviderSelection;
import com.truelayer.java.payments.entities.providerselection.UserSelectedProviderSelection;
import com.truelayer.java.payments.entities.schemeselection.userselected.SchemeSelection;
import com.truelayer.java.payments.entities.verification.AutomatedVerification;
import java.util.Collections;
import java.util.UUID;
import java.util.stream.Stream;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("Payments integration tests")
public class PaymentsIntegrationTests extends IntegrationTests {

    public static final String A_PAYMENT_ID = "a-payment-id";
    public static final String A_PAYMENT_REFUND_ID = "a-payment-refund-id";

    @DisplayName("It should create and return a payment")
    @ParameterizedTest(name = "of a payment with create response status {0}")
    @ValueSource(strings = {"AUTHORIZATION_REQUIRED", "AUTHORIZED", "FAILED"})
    @SneakyThrows
    public void shouldCreateAndReturnAPaymentMerchantAccount(Status expectedStatus) {
        String jsonResponseFile = "payments/201.create_payment." + expectedStatus.getStatus() + ".json";
        RequestStub.New()
                .method("post")
                .path(urlPathEqualTo("/connect/token"))
                .status(200)
                .bodyFile("auth/200.access_token.json")
                .build();
        RequestStub.New()
                .method("post")
                .path(urlPathEqualTo("/payments"))
                .withAuthorization()
                .withSignature()
                .withIdempotencyKey()
                .status(201)
                .bodyFile(jsonResponseFile)
                .build();
        CreatePaymentRequest paymentRequest = CreatePaymentRequest.builder().build();

        ApiResponse<CreatePaymentResponse> response =
                tlClient.payments().createPayment(paymentRequest).get();

        verifyGeneratedToken(Collections.singletonList(PAYMENTS));
        assertNotError(response);
        CreatePaymentResponse expected = TestUtils.deserializeJsonFileTo(jsonResponseFile, CreatePaymentResponse.class);
        assertEquals(expectedStatus, response.getData().getStatus());
        assertEquals(expected, response.getData());
    }

    @SneakyThrows
    @Test
    @DisplayName("It should create a payment with additional product intention")
    public void shouldCreateAPaymentWithAdditionalProductIntention() {
        RequestStub.New()
                .method("post")
                .path(urlPathEqualTo("/connect/token"))
                .status(200)
                .bodyFile("auth/200.access_token.json")
                .build();
        CreatePaymentRequest paymentRequest = CreatePaymentRequest.builder()
                .paymentMethod(PaymentMethod.bankTransfer().build())
                .amountInMinor(100)
                .relatedProducts(RelatedProducts.builder()
                        .signupPlus(Collections.emptyMap())
                        .build())
                .build();

        tlClient.payments().createPayment(paymentRequest).get();

        verifyGeneratedToken(Collections.singletonList(PAYMENTS));
        verify(postRequestedFor(urlPathEqualTo("/payments"))
                .withRequestBody(matchingJsonPath("$.related_products", equalToJson("{\"signup_plus\": {}}"))));
    }

    @DisplayName("It should create a payment with automated verification")
    @MethodSource("provideAutomatedVerifications")
    @ParameterizedTest()
    @SneakyThrows
    public void shouldCreateAPaymentWithAutomatedVerification(AutomatedVerification verification) {
        RequestStub.New()
                .method("post")
                .path(urlPathEqualTo("/connect/token"))
                .status(200)
                .bodyFile("auth/200.access_token.json")
                .build();

        CreatePaymentRequest paymentRequest = CreatePaymentRequest.builder()
                .paymentMethod(PaymentMethod.bankTransfer()
                        .beneficiary(MerchantAccount.merchantAccount()
                                .verification(verification)
                                .merchantAccountId(UUID.randomUUID().toString())
                                .build())
                        .build())
                .amountInMinor(100)
                .relatedProducts(RelatedProducts.builder()
                        .signupPlus(Collections.emptyMap())
                        .build())
                .build();

        tlClient.payments().createPayment(paymentRequest).get();

        verifyGeneratedToken(Collections.singletonList(PAYMENTS));
        verify(postRequestedFor(urlPathEqualTo("/payments"))
                .withRequestBody(matchingJsonPath(
                        "$.payment_method.beneficiary.verification",
                        equalToJson("{\"type\": \"automated\", \"remitter_name\": " + verification.isRemitterName()
                                + ", \"remitter_date_of_birth\": " + verification.isRemitterDateOfBirth() + "}"))));
    }

    @DisplayName("It should create payment with")
    @ParameterizedTest(name = "scheme_selection={0} and expected allow_remitter_fee={1}")
    @MethodSource("provideUserSelectedSchemeSelectionTestParameters")
    @SneakyThrows
    public void shouldCreateAPaymentWithSchemeSelection(
            SchemeSelection schemeSelection, boolean expectedAllowRemitterFee) {
        RequestStub.New()
                .method("post")
                .path(urlPathEqualTo("/connect/token"))
                .status(200)
                .bodyFile("auth/200.access_token.json")
                .build();

        UserSelectedProviderSelection userSelectedProviderSelection = ProviderSelection.userSelected()
                .filter(ProviderFilter.builder()
                        .countries(Collections.singletonList(CountryCode.GB))
                        .releaseChannel(ReleaseChannel.GENERAL_AVAILABILITY)
                        .customerSegments(Collections.singletonList(CustomerSegment.RETAIL))
                        .build())
                .schemeSelection(schemeSelection)
                .build();
        CreatePaymentRequest paymentRequest = CreatePaymentRequest.builder()
                .amountInMinor(50)
                .currency(CurrencyCode.GBP)
                .paymentMethod(PaymentMethod.bankTransfer()
                        .providerSelection(userSelectedProviderSelection)
                        .build())
                .build();

        tlClient.payments().createPayment(paymentRequest).get();

        verifyGeneratedToken(Collections.singletonList(PAYMENTS));

        verify(postRequestedFor(urlPathEqualTo("/payments"))
                .withRequestBody(matchingJsonPath(
                        "$.payment_method.provider_selection.scheme_selection",
                        equalToJson(String.format(
                                "{\"type\": \"%s\", \"allow_remitter_fee\": %s}",
                                schemeSelection.getType().getType(), expectedAllowRemitterFee)))));
    }

    @Test
    @DisplayName("It should return an error if the signature is not valid")
    @SneakyThrows
    public void shouldReturnErrorIfSignatureIsInvalid() {
        String jsonResponseFile = "payments/401.invalid_signature.json";
        RequestStub.New()
                .method("post")
                .path(urlPathEqualTo("/connect/token"))
                .status(200)
                .bodyFile("auth/200.access_token.json")
                .build();
        RequestStub.New()
                .method("post")
                .path(urlPathEqualTo("/payments"))
                .withAuthorization()
                .withSignature()
                .withIdempotencyKey()
                .status(401)
                .bodyFile(jsonResponseFile)
                .build();
        CreatePaymentRequest paymentRequest = CreatePaymentRequest.builder().build();

        ApiResponse<CreatePaymentResponse> paymentResponse =
                tlClient.payments().createPayment(paymentRequest).get();

        verifyGeneratedToken(Collections.singletonList(PAYMENTS));
        assertTrue(paymentResponse.isError());
        ProblemDetails expected = TestUtils.deserializeJsonFileTo(jsonResponseFile, ProblemDetails.class);
        assertEquals(expected, paymentResponse.getError());
    }

    @DisplayName("It should get the payment details")
    @ParameterizedTest(name = "of a payment of type {0} with status {1}")
    @MethodSource("provideShouldReturnAPaymentDetailTestParameters")
    @SneakyThrows
    public void shouldReturnAPaymentDetail(PaymentMethod.Type paymentMethodType, Status expectedStatus) {
        String jsonResponseFile = new StringBuilder("payments/200.get_payment_by_id.")
                .append(paymentMethodType.getType().toLowerCase())
                .append(".")
                .append(expectedStatus.getStatus())
                .append(".json")
                .toString();
        RequestStub.New()
                .method("post")
                .path(urlPathEqualTo("/connect/token"))
                .status(200)
                .bodyFile("auth/200.access_token.json")
                .build();
        RequestStub.New()
                .method("get")
                .path(urlPathEqualTo("/payments/" + A_PAYMENT_ID))
                .withAuthorization()
                .status(200)
                .bodyFile(jsonResponseFile)
                .build();

        ApiResponse<PaymentDetail> response =
                tlClient.payments().getPayment(A_PAYMENT_ID).get();

        verifyGeneratedToken(Collections.singletonList(PAYMENTS));
        assertNotError(response);
        PaymentDetail expected = TestUtils.deserializeJsonFileTo(jsonResponseFile, PaymentDetail.class);
        assertEquals(expectedStatus, response.getData().getStatus());
        assertEquals(expected, response.getData());
    }

    @Test
    @DisplayName("It should cancel a payment")
    @SneakyThrows
    public void shouldCancelPayment() {
        RequestStub.New()
                .method("post")
                .path(urlPathEqualTo("/connect/token"))
                .status(200)
                .bodyFile("auth/200.access_token.json")
                .build();
        RequestStub.New()
                .method("post")
                .path(urlPathEqualTo("/payments/" + A_PAYMENT_ID + "/actions/cancel"))
                .withAuthorization()
                .status(202)
                .build();

        ApiResponse<Void> response =
                tlClient.payments().cancelPayment(A_PAYMENT_ID).get();

        verifyGeneratedToken(Collections.singletonList(PAYMENTS));
        assertNotError(response);
    }

    @Test
    @DisplayName("It should return an error if a payment is not found")
    @SneakyThrows
    public void shouldThrowIfPaymentNotFound() {
        String jsonResponseFile = "payments/404.payment_not_found.json";
        RequestStub.New()
                .method("post")
                .path(urlPathEqualTo("/connect/token"))
                .status(200)
                .bodyFile("auth/200.access_token.json")
                .build();
        RequestStub.New()
                .method("post")
                .path(urlPathEqualTo("/payments"))
                .withAuthorization()
                .withSignature()
                .withIdempotencyKey()
                .status(404)
                .bodyFile(jsonResponseFile)
                .build();
        CreatePaymentRequest paymentRequest = CreatePaymentRequest.builder().build();

        ApiResponse<CreatePaymentResponse> paymentResponse =
                tlClient.payments().createPayment(paymentRequest).get();

        verifyGeneratedToken(Collections.singletonList(PAYMENTS));
        assertTrue(paymentResponse.isError());
        ProblemDetails expected = TestUtils.deserializeJsonFileTo(jsonResponseFile, ProblemDetails.class);
        assertEquals(expected, paymentResponse.getError());
    }

    @SneakyThrows
    @Test
    @DisplayName("It should return a request invalid error")
    public void shouldThrowARequestInvalidError() {
        String jsonResponseFile = "payments/400.request_invalid.json";
        RequestStub.New()
                .method("post")
                .path(urlPathEqualTo("/connect/token"))
                .status(200)
                .bodyFile("auth/200.access_token.json")
                .build();
        RequestStub.New()
                .method("post")
                .path(urlPathEqualTo("/payments"))
                .withAuthorization()
                .withSignature()
                .withIdempotencyKey()
                .status(400)
                .bodyFile(jsonResponseFile)
                .build();
        CreatePaymentRequest paymentRequest = CreatePaymentRequest.builder().build();

        ApiResponse<CreatePaymentResponse> paymentResponse =
                tlClient.payments().createPayment(paymentRequest).get();

        verifyGeneratedToken(Collections.singletonList(PAYMENTS));
        assertTrue(paymentResponse.isError());
        ProblemDetails expected = TestUtils.deserializeJsonFileTo(jsonResponseFile, ProblemDetails.class);
        assertEquals(expected, paymentResponse.getError());
    }

    @SneakyThrows
    @ParameterizedTest(name = "and get a response of type {0}")
    @ValueSource(
            strings = {
                "authorizing.provider_selection",
                "authorizing.redirect",
                "authorizing.consent",
                "authorizing.form",
                "authorizing.wait",
                "failed"
            })
    @DisplayName("It should start an authorization flow")
    public void shouldStartAnAuthorizationFlow(String status) {
        String jsonResponseFile = "payments/200.start_authorization_flow." + status + ".json";
        RequestStub.New()
                .method("post")
                .path(urlPathEqualTo("/connect/token"))
                .status(200)
                .bodyFile("auth/200.access_token.json")
                .build();
        RequestStub.New()
                .method("post")
                .path(urlPathEqualTo("/payments/" + A_PAYMENT_ID + "/authorization-flow"))
                .withAuthorization()
                .withIdempotencyKey()
                .status(200)
                .bodyFile(jsonResponseFile)
                .build();
        StartAuthorizationFlowRequest request =
                StartAuthorizationFlowRequest.builder().build();

        ApiResponse<AuthorizationFlowResponse> response = tlClient.payments()
                .startAuthorizationFlow(A_PAYMENT_ID, request)
                .get();

        verifyGeneratedToken(Collections.singletonList(PAYMENTS));
        assertNotError(response);
        AuthorizationFlowResponse expected =
                TestUtils.deserializeJsonFileTo(jsonResponseFile, AuthorizationFlowResponse.class);
        assertEquals(expected, response.getData());
    }

    @SneakyThrows
    @Test
    @DisplayName("It should start an authorization flow with custom headers")
    public void shouldStartAnAuthorizationFlowWithCustomHeaders() {
        String jsonResponseFile = "payments/200.start_authorization_flow.authorizing.wait.json";
        String endUserIpAddress = "11.1.2.3";
        String deviceUserAgent = "DummyUserAgent";
        RequestStub.New()
                .method("post")
                .path(urlPathEqualTo("/connect/token"))
                .status(200)
                .bodyFile("auth/200.access_token.json")
                .build();
        RequestStub.New()
                .method("post")
                .path(urlPathEqualTo("/payments/" + A_PAYMENT_ID + "/authorization-flow"))
                .withAuthorization()
                .withIdempotencyKey()
                .withXForwardedForHeader(endUserIpAddress)
                .withXDeviceUserAgent(deviceUserAgent)
                .status(200)
                .bodyFile(jsonResponseFile)
                .build();
        StartAuthorizationFlowRequest request =
                StartAuthorizationFlowRequest.builder().build();

        ApiResponse<AuthorizationFlowResponse> response = tlClient.payments()
                .startAuthorizationFlow(
                        Headers.builder()
                                .xForwardedFor(endUserIpAddress)
                                .xDeviceUserAgent(deviceUserAgent)
                                .build(),
                        A_PAYMENT_ID,
                        request)
                .get();

        verifyGeneratedToken(Collections.singletonList(PAYMENTS));
        verify(postRequestedFor(urlPathEqualTo("/payments/" + A_PAYMENT_ID + "/authorization-flow"))
                .withHeader(X_FORWARDED_FOR, equalTo(endUserIpAddress))
                .withHeader(X_DEVICE_USER_AGENT, equalTo(deviceUserAgent)));
        assertNotError(response);
        AuthorizationFlowResponse expected =
                TestUtils.deserializeJsonFileTo(jsonResponseFile, AuthorizationFlowResponse.class);
        assertEquals(expected, response.getData());
    }

    @SneakyThrows
    @ParameterizedTest(name = "and get a response of type {0}")
    @ValueSource(strings = {"AUTHORIZING", "FAILED"})
    @DisplayName("It should submit a provider selection")
    public void shouldSubmitProviderSelection(Status status) {
        String jsonResponseFile = "payments/200.submit_provider_selection." + status.getStatus() + ".json";
        RequestStub.New()
                .method("post")
                .path(urlPathEqualTo("/connect/token"))
                .status(200)
                .bodyFile("auth/200.access_token.json")
                .build();
        RequestStub.New()
                .method("post")
                .path(urlPathEqualTo("/payments/" + A_PAYMENT_ID + "/authorization-flow/actions/provider-selection"))
                .withAuthorization()
                .withIdempotencyKey()
                .status(200)
                .bodyFile(jsonResponseFile)
                .build();

        SubmitProviderSelectionRequest submitProviderSelectionRequest =
                SubmitProviderSelectionRequest.builder().build();
        ApiResponse<AuthorizationFlowResponse> response = tlClient.payments()
                .submitProviderSelection(A_PAYMENT_ID, submitProviderSelectionRequest)
                .get();

        verifyGeneratedToken(Collections.singletonList(PAYMENTS));
        assertNotError(response);
        AuthorizationFlowResponse expected =
                TestUtils.deserializeJsonFileTo(jsonResponseFile, AuthorizationFlowResponse.class);
        assertEquals(status, response.getData().getStatus());
        assertEquals(expected, response.getData());
    }

    @Test
    @DisplayName("It should create a payment refund")
    @SneakyThrows
    public void shouldCreateAPaymentRefund() {
        String jsonResponseFile = "payments/202.create_payment_refund.json";
        RequestStub.New()
                .method("post")
                .path(urlPathEqualTo("/connect/token"))
                .status(200)
                .bodyFile("auth/200.access_token.json")
                .build();
        RequestStub.New()
                .method("post")
                .path(urlPathEqualTo("/payments/" + A_PAYMENT_ID + "/refunds"))
                .withAuthorization()
                .withSignature()
                .withIdempotencyKey()
                .status(202)
                .bodyFile(jsonResponseFile)
                .build();
        CreatePaymentRefundRequest createPaymentRefundRequest =
                CreatePaymentRefundRequest.builder().build();

        ApiResponse<CreatePaymentRefundResponse> response = tlClient.payments()
                .createPaymentRefund(A_PAYMENT_ID, createPaymentRefundRequest)
                .get();

        verifyGeneratedToken(Collections.singletonList(PAYMENTS));
        assertNotError(response);
        CreatePaymentRefundResponse expected =
                TestUtils.deserializeJsonFileTo(jsonResponseFile, CreatePaymentRefundResponse.class);
        assertEquals(expected, response.getData());
    }

    @Test
    @DisplayName("It should return a payment refunds list")
    @SneakyThrows
    public void shouldReturnAPaymentRefundsList() {
        String jsonResponseFile = "payments/200.list_payment_refunds.json";
        RequestStub.New()
                .method("post")
                .path(urlPathEqualTo("/connect/token"))
                .status(200)
                .bodyFile("auth/200.access_token.json")
                .build();
        RequestStub.New()
                .method("get")
                .path(urlPathEqualTo("/payments/" + A_PAYMENT_ID + "/refunds"))
                .withAuthorization()
                .status(200)
                .bodyFile(jsonResponseFile)
                .build();

        ApiResponse<ListPaymentRefundsResponse> response =
                tlClient.payments().listPaymentRefunds(A_PAYMENT_ID).get();

        verifyGeneratedToken(Collections.singletonList(PAYMENTS));
        assertNotError(response);
        ListPaymentRefundsResponse expected =
                TestUtils.deserializeJsonFileTo(jsonResponseFile, ListPaymentRefundsResponse.class);
        assertEquals(expected, response.getData());
    }

    @DisplayName("It should get payment refund details")
    @ParameterizedTest(name = "of a payment refund with status {0}")
    @ValueSource(strings = {"PENDING", "AUTHORIZED", "EXECUTED", "FAILED"})
    @SneakyThrows
    public void shouldReturnPaymentRefundDetails(
            com.truelayer.java.payments.entities.paymentrefund.Status expectedStatus) {
        String jsonResponseFile = "payments/200.get_payment_refund_by_id." + expectedStatus.getStatus() + ".json";
        RequestStub.New()
                .method("post")
                .path(urlPathEqualTo("/connect/token"))
                .status(200)
                .bodyFile("auth/200.access_token.json")
                .build();
        RequestStub.New()
                .method("get")
                .path(urlPathEqualTo("/payments/" + A_PAYMENT_ID + "/refunds/" + A_PAYMENT_REFUND_ID))
                .withAuthorization()
                .status(200)
                .bodyFile(jsonResponseFile)
                .build();

        ApiResponse<PaymentRefund> response = tlClient.payments()
                .getPaymentRefundById(A_PAYMENT_ID, A_PAYMENT_REFUND_ID)
                .get();

        verifyGeneratedToken(Collections.singletonList(PAYMENTS));
        assertNotError(response);
        PaymentRefund expected = TestUtils.deserializeJsonFileTo(jsonResponseFile, PaymentRefund.class);
        assertEquals(expectedStatus, response.getData().getStatus());
        assertEquals(expected, response.getData());
    }

    private static Stream<Arguments> provideShouldReturnAPaymentDetailTestParameters() {
        return Stream.of(
                Arguments.of(BANK_TRANSFER, AUTHORIZATION_REQUIRED),
                Arguments.of(BANK_TRANSFER, AUTHORIZING),
                Arguments.of(BANK_TRANSFER, AUTHORIZED),
                Arguments.of(BANK_TRANSFER, EXECUTED),
                Arguments.of(BANK_TRANSFER, SETTLED),
                Arguments.of(BANK_TRANSFER, FAILED),
                Arguments.of(BANK_TRANSFER, ATTEMPT_FAILED),
                Arguments.of(MANDATE, EXECUTED),
                Arguments.of(MANDATE, FAILED));
    }

    private static Stream<Arguments> provideUserSelectedSchemeSelectionTestParameters() {
        return Stream.of(
                Arguments.of(
                        SchemeSelection.instantOnly().allowRemitterFee(true).build(), true),
                Arguments.of(
                        SchemeSelection.instantOnly().allowRemitterFee(false).build(), false),
                Arguments.of(
                        SchemeSelection.instantPreferred()
                                .allowRemitterFee(true)
                                .build(),
                        true),
                Arguments.of(
                        SchemeSelection.instantPreferred()
                                .allowRemitterFee(false)
                                .build(),
                        false));
    }

    private static Stream<Arguments> provideAutomatedVerifications() {
        return Stream.of(
                Arguments.of(AutomatedVerification.builder().withRemitterName().build()),
                Arguments.of(AutomatedVerification.builder()
                        .withRemitterDateOfBirth()
                        .build()));
    }
}
