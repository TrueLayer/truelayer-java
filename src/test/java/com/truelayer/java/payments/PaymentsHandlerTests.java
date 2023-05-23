package com.truelayer.java.payments;

import static com.truelayer.java.TestUtils.buildTestHeaders;
import static com.truelayer.java.http.mappers.HeadersMapper.toMap;
import static java.util.Collections.emptyMap;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.truelayer.java.TestUtils;
import com.truelayer.java.entities.EmptyRequestBody;
import com.truelayer.java.http.entities.Headers;
import com.truelayer.java.payments.entities.*;
import java.util.Collections;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class PaymentsHandlerTests {

    private static final String A_PAYMENT_ID = "a-payment-id";
    private static final String A_REFUND_ID = "a-refund-id";

    @Test
    @DisplayName("It should call the create payment endpoint")
    public void shouldCallCreatePayment() {
        IPaymentsApi paymentsApi = Mockito.mock(IPaymentsApi.class);
        PaymentsHandler sut = new PaymentsHandler(paymentsApi);
        CreatePaymentRequest request = CreatePaymentRequest.builder().build();

        sut.createPayment(request);

        verify(paymentsApi, times(1)).createPayment(emptyMap(), request);
    }

    @Test
    @DisplayName("It should call the create payment endpoint with additional headers")
    public void shouldCallCreatePaymentWithAdditionalHeaders() {
        IPaymentsApi paymentsApi = Mockito.mock(IPaymentsApi.class);
        PaymentsHandler sut = new PaymentsHandler(paymentsApi);
        Headers customHeaders = buildTestHeaders();
        CreatePaymentRequest request = CreatePaymentRequest.builder().build();

        sut.createPayment(customHeaders, request);

        verify(paymentsApi, times(1)).createPayment(toMap(customHeaders), request);
    }

    @Test
    @DisplayName("It should call the get payment by id endpoint")
    public void shouldCallGetPaymentById() {
        IPaymentsApi paymentsApi = Mockito.mock(IPaymentsApi.class);
        PaymentsHandler sut = new PaymentsHandler(paymentsApi);

        sut.getPayment(A_PAYMENT_ID);

        verify(paymentsApi, times(1)).getPayment(A_PAYMENT_ID);
    }

    @Test
    @DisplayName("It should call the start authorization flow endpoint")
    public void shouldCallStartAuthorizationFlow() {
        IPaymentsApi paymentsApi = Mockito.mock(IPaymentsApi.class);
        PaymentsHandler sut = new PaymentsHandler(paymentsApi);
        StartAuthorizationFlowRequest request =
                StartAuthorizationFlowRequest.builder().withProviderSelection().build();

        sut.startAuthorizationFlow(A_PAYMENT_ID, request);

        verify(paymentsApi, times(1)).startAuthorizationFlow(emptyMap(), A_PAYMENT_ID, request);
    }

    @Test
    @DisplayName("It should call the start authorization flow endpoint with additional headers")
    public void shouldCallStartAuthorizationFlowWithCustomHeaders() {
        IPaymentsApi paymentsApi = Mockito.mock(IPaymentsApi.class);
        PaymentsHandler sut = new PaymentsHandler(paymentsApi);
        Headers customHeaders = buildTestHeaders();
        StartAuthorizationFlowRequest request =
                StartAuthorizationFlowRequest.builder().withProviderSelection().build();

        sut.startAuthorizationFlow(customHeaders, A_PAYMENT_ID, request);

        verify(paymentsApi, times(1)).startAuthorizationFlow(toMap(customHeaders), A_PAYMENT_ID, request);
    }

    @Test
    @DisplayName("It should call the submit provider selection endpoint")
    public void shouldCallSubmitProviderSelection() {
        IPaymentsApi paymentsApi = Mockito.mock(IPaymentsApi.class);
        PaymentsHandler sut = new PaymentsHandler(paymentsApi);
        SubmitProviderSelectionRequest request = SubmitProviderSelectionRequest.builder()
                .providerId("a-provider-id")
                .build();

        sut.submitProviderSelection(A_PAYMENT_ID, request);

        verify(paymentsApi, times(1)).submitProviderSelection(emptyMap(), A_PAYMENT_ID, request);
    }

    @Test
    @DisplayName("It should call the submit provider selection endpoint with additional headers")
    public void shouldCallSubmitProviderSelectionWithCustomHeaders() {
        IPaymentsApi paymentsApi = Mockito.mock(IPaymentsApi.class);
        PaymentsHandler sut = new PaymentsHandler(paymentsApi);
        Headers customHeaders = TestUtils.buildTestHeaders();
        SubmitProviderSelectionRequest request = SubmitProviderSelectionRequest.builder()
                .providerId("a-provider-id")
                .build();

        sut.submitProviderSelection(customHeaders, A_PAYMENT_ID, request);

        verify(paymentsApi, times(1)).submitProviderSelection(toMap(customHeaders), A_PAYMENT_ID, request);
    }

    @Test
    @DisplayName("It should call the submit consent endpoint")
    public void shouldCallSubmitConsent() {
        IPaymentsApi paymentsApi = Mockito.mock(IPaymentsApi.class);
        PaymentsHandler sut = new PaymentsHandler(paymentsApi);

        sut.submitConsent(A_PAYMENT_ID);

        verify(paymentsApi, times(1)).submitConsent(emptyMap(), A_PAYMENT_ID, new EmptyRequestBody());
    }

    @Test
    @DisplayName("It should call the submit consent endpoint with additional headers")
    public void shouldCallSubmitConsentWithCustomHeaders() {
        IPaymentsApi paymentsApi = Mockito.mock(IPaymentsApi.class);
        PaymentsHandler sut = new PaymentsHandler(paymentsApi);
        Headers customHeaders = TestUtils.buildTestHeaders();

        sut.submitConsent(customHeaders, A_PAYMENT_ID);

        verify(paymentsApi, times(1)).submitConsent(toMap(customHeaders), A_PAYMENT_ID, new EmptyRequestBody());
    }

    @Test
    @DisplayName("It should call the submit form endpoint")
    public void shouldCallSubmitForm() {
        IPaymentsApi paymentsApi = Mockito.mock(IPaymentsApi.class);
        PaymentsHandler sut = new PaymentsHandler(paymentsApi);
        SubmitFormRequest request =
                SubmitFormRequest.builder().inputs(Collections.emptyMap()).build();

        sut.submitForm(A_PAYMENT_ID, request);

        verify(paymentsApi, times(1)).submitForm(emptyMap(), A_PAYMENT_ID, request);
    }

    @Test
    @DisplayName("It should call the submit form endpoint with additional headers")
    public void shouldCallSubmitFormWithCustomHeaders() {
        IPaymentsApi paymentsApi = Mockito.mock(IPaymentsApi.class);
        PaymentsHandler sut = new PaymentsHandler(paymentsApi);
        Headers customHeaders = TestUtils.buildTestHeaders();
        SubmitFormRequest request =
                SubmitFormRequest.builder().inputs(Collections.emptyMap()).build();

        sut.submitForm(customHeaders, A_PAYMENT_ID, request);

        verify(paymentsApi, times(1)).submitForm(toMap(customHeaders), A_PAYMENT_ID, request);
    }

    @Test
    @DisplayName("It should call the create payment refund endpoint")
    public void shouldCallCreatePaymentRefund() {
        IPaymentsApi paymentsApi = Mockito.mock(IPaymentsApi.class);
        PaymentsHandler sut = new PaymentsHandler(paymentsApi);
        CreatePaymentRefundRequest request =
                CreatePaymentRefundRequest.builder().amountInMinor(100).build();

        sut.createPaymentRefund(A_PAYMENT_ID, request);

        verify(paymentsApi, times(1)).createPaymentRefund(emptyMap(), A_PAYMENT_ID, request);
    }

    @Test
    @DisplayName("It should call the create payment refund endpoint with additional headers")
    public void shouldCallCreatePaymentRefundWithCustomHeaders() {
        IPaymentsApi paymentsApi = Mockito.mock(IPaymentsApi.class);
        PaymentsHandler sut = new PaymentsHandler(paymentsApi);
        Headers customHeaders = TestUtils.buildTestHeaders();
        CreatePaymentRefundRequest request =
                CreatePaymentRefundRequest.builder().amountInMinor(100).build();

        sut.createPaymentRefund(customHeaders, A_PAYMENT_ID, request);

        verify(paymentsApi, times(1)).createPaymentRefund(toMap(customHeaders), A_PAYMENT_ID, request);
    }

    @Test
    @DisplayName("It should call the list payment refunds endpoint")
    public void shouldCallListPaymentRefunds() {
        IPaymentsApi paymentsApi = Mockito.mock(IPaymentsApi.class);
        PaymentsHandler sut = new PaymentsHandler(paymentsApi);

        sut.listPaymentRefunds(A_PAYMENT_ID);

        verify(paymentsApi, times(1)).listPaymentRefunds(A_PAYMENT_ID);
    }

    @Test
    @DisplayName("It should call the get payment refund by id endpoint")
    public void shouldCallGetPaymentRefundById() {
        IPaymentsApi paymentsApi = Mockito.mock(IPaymentsApi.class);
        PaymentsHandler sut = new PaymentsHandler(paymentsApi);

        sut.getPaymentRefundById(A_PAYMENT_ID, A_REFUND_ID);

        verify(paymentsApi, times(1)).getPaymentRefundById(A_PAYMENT_ID, A_REFUND_ID);
    }
}
