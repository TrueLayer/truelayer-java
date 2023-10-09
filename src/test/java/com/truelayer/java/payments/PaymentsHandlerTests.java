package com.truelayer.java.payments;

import static com.truelayer.java.TestUtils.buildTestHeaders;
import static com.truelayer.java.http.mappers.HeadersMapper.toMap;
import static java.util.Collections.emptyMap;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.truelayer.java.Constants;
import com.truelayer.java.TestUtils;
import com.truelayer.java.entities.EmptyRequestBody;
import com.truelayer.java.entities.RequestScopes;
import com.truelayer.java.http.entities.Headers;
import com.truelayer.java.payments.entities.*;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class PaymentsHandlerTests {

    private static final String A_PAYMENT_ID = "a-payment-id";
    private static final String A_REFUND_ID = "a-refund-id";
    private static final RequestScopes SCOPES =
            RequestScopes.builder().scope("a-custom-scope").build();

    private PaymentsHandler sut;
    private IPaymentsApi paymentsApiMock;

    @BeforeEach
    public void setup() {
        paymentsApiMock = Mockito.mock(IPaymentsApi.class);
        sut = PaymentsHandler.builder()
                .paymentsApi(paymentsApiMock)
                .scopes(SCOPES)
                .build();
    }

    @Test
    @DisplayName("It should call the create payment endpoint with the default scopes")
    public void shouldCallCreatePaymentWithDefaultScopes() {
        PaymentsHandler sut =
                PaymentsHandler.builder().paymentsApi(paymentsApiMock).build();
        CreatePaymentRequest request = CreatePaymentRequest.builder().build();

        sut.createPayment(request);

        RequestScopes expectedDefaultScopes =
                RequestScopes.builder().scope(Constants.Scopes.PAYMENTS).build();
        verify(paymentsApiMock, times(1)).createPayment(expectedDefaultScopes, emptyMap(), request);
    }

    @Test
    @DisplayName("It should call the create payment endpoint")
    public void shouldCallCreatePayment() {
        CreatePaymentRequest request = CreatePaymentRequest.builder().build();

        sut.createPayment(request);

        verify(paymentsApiMock, times(1)).createPayment(SCOPES, emptyMap(), request);
    }

    @Test
    @DisplayName("It should call the create payment endpoint with additional headers")
    public void shouldCallCreatePaymentWithAdditionalHeaders() {
        Headers customHeaders = buildTestHeaders();
        CreatePaymentRequest request = CreatePaymentRequest.builder().build();

        sut.createPayment(customHeaders, request);

        verify(paymentsApiMock, times(1)).createPayment(SCOPES, toMap(customHeaders), request);
    }

    @Test
    @DisplayName("It should call the get payment by id endpoint")
    public void shouldCallGetPaymentById() {
        sut.getPayment(A_PAYMENT_ID);

        verify(paymentsApiMock, times(1)).getPayment(SCOPES, A_PAYMENT_ID);
    }

    @Test
    @DisplayName("It should call the start authorization flow endpoint")
    public void shouldCallStartAuthorizationFlow() {
        StartAuthorizationFlowRequest request =
                StartAuthorizationFlowRequest.builder().withProviderSelection().build();

        sut.startAuthorizationFlow(A_PAYMENT_ID, request);

        verify(paymentsApiMock, times(1)).startAuthorizationFlow(SCOPES, emptyMap(), A_PAYMENT_ID, request);
    }

    @Test
    @DisplayName("It should call the start authorization flow endpoint with additional headers")
    public void shouldCallStartAuthorizationFlowWithCustomHeaders() {
        Headers customHeaders = buildTestHeaders();
        StartAuthorizationFlowRequest request =
                StartAuthorizationFlowRequest.builder().withProviderSelection().build();

        sut.startAuthorizationFlow(customHeaders, A_PAYMENT_ID, request);

        verify(paymentsApiMock, times(1)).startAuthorizationFlow(SCOPES, toMap(customHeaders), A_PAYMENT_ID, request);
    }

    @Test
    @DisplayName("It should call the submit provider selection endpoint")
    public void shouldCallSubmitProviderSelection() {
        SubmitProviderSelectionRequest request = SubmitProviderSelectionRequest.builder()
                .providerId("a-provider-id")
                .build();

        sut.submitProviderSelection(A_PAYMENT_ID, request);

        verify(paymentsApiMock, times(1)).submitProviderSelection(SCOPES, emptyMap(), A_PAYMENT_ID, request);
    }

    @Test
    @DisplayName("It should call the submit provider selection endpoint with additional headers")
    public void shouldCallSubmitProviderSelectionWithCustomHeaders() {
        Headers customHeaders = TestUtils.buildTestHeaders();
        SubmitProviderSelectionRequest request = SubmitProviderSelectionRequest.builder()
                .providerId("a-provider-id")
                .build();

        sut.submitProviderSelection(customHeaders, A_PAYMENT_ID, request);

        verify(paymentsApiMock, times(1)).submitProviderSelection(SCOPES, toMap(customHeaders), A_PAYMENT_ID, request);
    }

    @Test
    @DisplayName("It should call the submit consent endpoint")
    public void shouldCallSubmitConsent() {
        sut.submitConsent(A_PAYMENT_ID);

        verify(paymentsApiMock, times(1)).submitConsent(SCOPES, emptyMap(), A_PAYMENT_ID, new EmptyRequestBody());
    }

    @Test
    @DisplayName("It should call the submit consent endpoint with additional headers")
    public void shouldCallSubmitConsentWithCustomHeaders() {
        Headers customHeaders = TestUtils.buildTestHeaders();

        sut.submitConsent(customHeaders, A_PAYMENT_ID);

        verify(paymentsApiMock, times(1))
                .submitConsent(SCOPES, toMap(customHeaders), A_PAYMENT_ID, new EmptyRequestBody());
    }

    @Test
    @DisplayName("It should call the submit form endpoint")
    public void shouldCallSubmitForm() {
        SubmitFormRequest request =
                SubmitFormRequest.builder().inputs(Collections.emptyMap()).build();

        sut.submitForm(A_PAYMENT_ID, request);

        verify(paymentsApiMock, times(1)).submitForm(SCOPES, emptyMap(), A_PAYMENT_ID, request);
    }

    @Test
    @DisplayName("It should call the submit form endpoint with additional headers")
    public void shouldCallSubmitFormWithCustomHeaders() {
        Headers customHeaders = TestUtils.buildTestHeaders();
        SubmitFormRequest request =
                SubmitFormRequest.builder().inputs(Collections.emptyMap()).build();

        sut.submitForm(customHeaders, A_PAYMENT_ID, request);

        verify(paymentsApiMock, times(1)).submitForm(SCOPES, toMap(customHeaders), A_PAYMENT_ID, request);
    }

    @Test
    @DisplayName("It should call the create payment refund endpoint")
    public void shouldCallCreatePaymentRefund() {
        CreatePaymentRefundRequest request =
                CreatePaymentRefundRequest.builder().amountInMinor(100).build();

        sut.createPaymentRefund(A_PAYMENT_ID, request);

        verify(paymentsApiMock, times(1)).createPaymentRefund(SCOPES, emptyMap(), A_PAYMENT_ID, request);
    }

    @Test
    @DisplayName("It should call the create payment refund endpoint with additional headers")
    public void shouldCallCreatePaymentRefundWithCustomHeaders() {
        Headers customHeaders = TestUtils.buildTestHeaders();
        CreatePaymentRefundRequest request =
                CreatePaymentRefundRequest.builder().amountInMinor(100).build();

        sut.createPaymentRefund(customHeaders, A_PAYMENT_ID, request);

        verify(paymentsApiMock, times(1)).createPaymentRefund(SCOPES, toMap(customHeaders), A_PAYMENT_ID, request);
    }

    @Test
    @DisplayName("It should call the list payment refunds endpoint")
    public void shouldCallListPaymentRefunds() {
        sut.listPaymentRefunds(A_PAYMENT_ID);

        verify(paymentsApiMock, times(1)).listPaymentRefunds(SCOPES, A_PAYMENT_ID);
    }

    @Test
    @DisplayName("It should call the get payment refund by id endpoint")
    public void shouldCallGetPaymentRefundById() {
        sut.getPaymentRefundById(A_PAYMENT_ID, A_REFUND_ID);

        verify(paymentsApiMock, times(1)).getPaymentRefundById(SCOPES, A_PAYMENT_ID, A_REFUND_ID);
    }
}
