package com.truelayer.java.acceptance;

import static com.truelayer.java.TestUtils.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.truelayer.java.commonapi.entities.SubmitPaymentReturnParametersRequest;
import com.truelayer.java.commonapi.entities.SubmitPaymentReturnParametersResponse;
import com.truelayer.java.entities.ProviderFilter;
import com.truelayer.java.entities.Remitter;
import com.truelayer.java.entities.User;
import com.truelayer.java.entities.accountidentifier.SortCodeAccountNumberAccountIdentifier;
import com.truelayer.java.entities.beneficiary.Beneficiary;
import com.truelayer.java.entities.providerselection.ProviderSelection;
import com.truelayer.java.entities.providerselection.UserSelectedProviderSelection;
import com.truelayer.java.http.entities.ApiResponse;
import com.truelayer.java.merchantaccounts.entities.MerchantAccount;
import com.truelayer.java.payments.entities.*;
import com.truelayer.java.payments.entities.StartAuthorizationFlowRequest.Redirect;
import com.truelayer.java.payments.entities.paymentdetail.PaymentDetail;
import com.truelayer.java.payments.entities.paymentmethod.PaymentMethod;
import com.truelayer.java.payments.entities.providerselection.PreselectedProviderSelection;
import java.net.URI;
import java.util.Collections;
import lombok.SneakyThrows;
import okhttp3.*;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.*;

@Tag("acceptance")
public class PaymentsAcceptanceTests extends AcceptanceTests {

    public static final String RETURN_URI = "http://localhost:3000/callback";
    public static final String PROVIDER_ID = "mock-payments-gb-redirect";

    @Test
    @DisplayName("It should create and get by id a payment with user_selected provider")
    @SneakyThrows
    public void shouldCreateAPaymentWithUserSelectionProvider() {
        // create payment
        UserSelectedProviderSelection userSelectionProvider = ProviderSelection.userSelected()
                .filter(ProviderFilter.builder()
                        .countries(Collections.singletonList(CountryCode.GB))
                        .releaseChannel(ReleaseChannel.GENERAL_AVAILABILITY)
                        .customerSegments(Collections.singletonList(CustomerSegment.RETAIL))
                        .providerIds(Collections.singletonList(PROVIDER_ID))
                        .build())
                .build();
        CreatePaymentRequest paymentRequest = buildPaymentRequestWithProviderSelection(userSelectionProvider);

        ApiResponse<CreatePaymentResponse> createPaymentResponse =
                tlClient.payments().createPayment(paymentRequest).get();

        assertNotError(createPaymentResponse);

        // get it by id
        ApiResponse<PaymentDetail> getPaymentByIdResponse = tlClient.payments()
                .getPayment(createPaymentResponse.getData().getId())
                .get();

        assertNotError(getPaymentByIdResponse);
    }

    @Test
    @DisplayName("It should create and get by id a payment with preselected provider")
    @SneakyThrows
    public void shouldCreateAPaymentWithPreselectedProvider() {
        // create payment
        PreselectedProviderSelection preselectionProvider = ProviderSelection.preselected()
                .providerId(PROVIDER_ID)
                .schemeId(SchemeId.FASTER_PAYMENTS_SERVICE)
                .remitter(Remitter.builder()
                        .accountHolderName("Andrea Di Lisio")
                        .accountIdentifier(SortCodeAccountNumberAccountIdentifier.builder()
                                .accountNumber("12345678")
                                .sortCode("123456")
                                .build())
                        .build())
                .build();
        CreatePaymentRequest paymentRequest = buildPaymentRequestWithProviderSelection(preselectionProvider);

        ApiResponse<CreatePaymentResponse> createPaymentResponse =
                tlClient.payments().createPayment(paymentRequest).get();

        assertNotError(createPaymentResponse);

        // get it by id
        ApiResponse<PaymentDetail> getPaymentByIdResponse = tlClient.payments()
                .getPayment(createPaymentResponse.getData().getId())
                .get();

        assertNotError(getPaymentByIdResponse);
    }

    @Test
    @DisplayName("It should create a payment and open it in TL HPP")
    @SneakyThrows
    public void shouldCreateAPaymentAndOpenItInHPP() {
        // create payment
        CreatePaymentRequest paymentRequest = buildPaymentRequest();

        ApiResponse<CreatePaymentResponse> createPaymentResponse =
                tlClient.payments().createPayment(paymentRequest).get();

        assertNotError(createPaymentResponse);

        // open it in HPP
        URI link = tlClient.hpp()
                .getHostedPaymentPageLink(
                        createPaymentResponse.getData().getId(),
                        createPaymentResponse.getData().getResourceToken(),
                        URI.create(RETURN_URI));

        assertCanBrowseLink(link);
    }

    @SneakyThrows
    @Test
    @DisplayName("It should complete an authorization flow for a payment")
    public void shouldCompleteAnAuthorizationFlowForAPayment() {
        // create payment
        CreatePaymentRequest paymentRequest = buildPaymentRequest();

        ApiResponse<CreatePaymentResponse> createPaymentResponse =
                tlClient.payments().createPayment(paymentRequest).get();

        assertNotError(createPaymentResponse);

        // start the auth flow
        StartAuthorizationFlowRequest startAuthorizationFlowRequest = StartAuthorizationFlowRequest.builder()
                .redirect(Redirect.builder().returnUri(URI.create(RETURN_URI)).build())
                .withProviderSelection()
                .build();
        ApiResponse<AuthorizationFlowResponse> startAuthorizationFlowResponse = tlClient.payments()
                .startAuthorizationFlow(createPaymentResponse.getData().getId(), startAuthorizationFlowRequest)
                .get();

        assertNotError(startAuthorizationFlowResponse);

        // Submit the provider selection
        ApiResponse<AuthorizationFlowResponse> submitProviderSelectionResponse = tlClient.payments()
                .submitProviderSelection(
                        createPaymentResponse.getData().getId(),
                        SubmitProviderSelectionRequest.builder()
                                .providerId(PROVIDER_ID)
                                .build())
                .get();

        assertNotError(submitProviderSelectionResponse);
    }

    @SneakyThrows
    @Test
    @DisplayName("It should complete an authorization flow for a payment with a preselected provider")
    public void shouldCompleteAnAuthorizationFlowForAPaymentWithPreselectedProvider() {
        // create payment
        PreselectedProviderSelection preselectionProvider = ProviderSelection.preselected()
                .providerId(PROVIDER_ID)
                .schemeId(SchemeId.FASTER_PAYMENTS_SERVICE)
                .remitter(Remitter.builder()
                        .accountHolderName("Andrea Di Lisio")
                        .accountIdentifier(SortCodeAccountNumberAccountIdentifier.builder()
                                .accountNumber("12345678")
                                .sortCode("123456")
                                .build())
                        .build())
                .build();
        CreatePaymentRequest paymentRequest = buildPaymentRequestWithProviderSelection(preselectionProvider);

        ApiResponse<CreatePaymentResponse> createPaymentResponse =
                tlClient.payments().createPayment(paymentRequest).get();

        assertNotError(createPaymentResponse);

        // start the auth flow
        StartAuthorizationFlowRequest startAuthorizationFlowRequest = StartAuthorizationFlowRequest.builder()
                .redirect(Redirect.builder().returnUri(URI.create(RETURN_URI)).build())
                .withProviderSelection()
                .build();
        ApiResponse<AuthorizationFlowResponse> startAuthorizationFlowResponse = tlClient.payments()
                .startAuthorizationFlow(createPaymentResponse.getData().getId(), startAuthorizationFlowRequest)
                .get();

        assertNotError(startAuthorizationFlowResponse);

        // assert that the link returned is good to be browsed
        URI bankPage = startAuthorizationFlowResponse
                .getData()
                .getAuthorizationFlow()
                .getActions()
                .getNext()
                .asRedirect()
                .getUri();
        assertCanBrowseLink(bankPage);
    }

    @SneakyThrows
    @Test
    @DisplayName("It should complete an authorization flow for a payment with provider return")
    public void shouldCompleteAnAuthorizationFlowForAPaymentWithProviderReturn() {
        // create payment
        PreselectedProviderSelection preselectionProvider = ProviderSelection.preselected()
                .providerId(PROVIDER_ID)
                .schemeId(SchemeId.FASTER_PAYMENTS_SERVICE)
                .remitter(Remitter.builder()
                        .accountHolderName("Andrea Di Lisio")
                        .accountIdentifier(SortCodeAccountNumberAccountIdentifier.builder()
                                .accountNumber("12345678")
                                .sortCode("123456")
                                .build())
                        .build())
                .build();
        CreatePaymentRequest paymentRequest = buildPaymentRequestWithProviderSelection(preselectionProvider);

        ApiResponse<CreatePaymentResponse> createPaymentResponse =
                tlClient.payments().createPayment(paymentRequest).get();

        assertNotError(createPaymentResponse);

        // start the auth flow
        StartAuthorizationFlowRequest startAuthorizationFlowRequest = StartAuthorizationFlowRequest.builder()
                .redirect(Redirect.builder()
                        .returnUri(URI.create(RETURN_URI))
                        .directReturnUri(URI.create(RETURN_URI))
                        .build())
                .withProviderSelection()
                .build();
        ApiResponse<AuthorizationFlowResponse> startAuthorizationFlowResponse = tlClient.payments()
                .startAuthorizationFlow(createPaymentResponse.getData().getId(), startAuthorizationFlowRequest)
                .get();
        assertNotError(startAuthorizationFlowResponse);

        // Call the mock payment api response to trigger the executed state on the payment just created
        URI redirectUri = startAuthorizationFlowResponse
                .getData()
                .getAuthorizationFlow()
                .getActions()
                .getNext()
                .asRedirect()
                .getUri();
        assertNotNull(redirectUri);
        String protocol = redirectUri.getScheme();
        String host = redirectUri.getHost();
        String paymentId = redirectUri.getPath().replaceFirst("/login/", "");
        RequestBody body =
                RequestBody.create(MediaType.get("application/json"), "{\"action\":\"Execute\", \"redirect\": false}");
        Request request = new Request.Builder()
                .url(String.format("%s://%s/api/single-immediate-payments/%s/action", protocol, host, paymentId))
                .post(body)
                .addHeader(
                        "Authorization",
                        String.format("Bearer %s", redirectUri.getFragment().replaceFirst("token=", "")))
                .build();
        Response paymentResponse = getHttpClientInstance().newCall(request).execute();
        assertTrue(paymentResponse.isSuccessful());
        String responseString = paymentResponse.body().string();
        assertNotNull(responseString);

        // Grab the provider return query and fragment from the mock payment api response
        URI responseUrl = URI.create(responseString);
        SubmitPaymentReturnParametersRequest submitProviderReturn = SubmitPaymentReturnParametersRequest.builder()
                .query(responseUrl.getQuery())
                .fragment(responseUrl.getFragment())
                .build();
        ApiResponse<SubmitPaymentReturnParametersResponse> submitPaymentReturnParametersResponse =
                tlClient.submitPaymentReturnParameters(submitProviderReturn).get();
        assertNotError(submitPaymentReturnParametersResponse);
    }

    @SneakyThrows
    private CreatePaymentRequest buildPaymentRequestWithProviderSelection(ProviderSelection providerSelection) {
        MerchantAccount merchantAccount = getMerchantAccount();

        return CreatePaymentRequest.builder()
                .amountInMinor(RandomUtils.nextInt(50, 500))
                .currency(merchantAccount.getCurrency())
                .paymentMethod(PaymentMethod.bankTransfer()
                        .providerSelection(providerSelection)
                        .beneficiary(Beneficiary.merchantAccount()
                                .merchantAccountId(merchantAccount.getId())
                                .build())
                        .build())
                .user(User.builder()
                        .name("Andrea Di Lisio")
                        .email("andrea@truelayer.com")
                        .build())
                .build();
    }

    private CreatePaymentRequest buildPaymentRequest() {
        UserSelectedProviderSelection userSelectionProvider = UserSelectedProviderSelection.builder()
                .filter(ProviderFilter.builder()
                        .countries(Collections.singletonList(CountryCode.GB))
                        .releaseChannel(ReleaseChannel.GENERAL_AVAILABILITY)
                        .customerSegments(Collections.singletonList(CustomerSegment.RETAIL))
                        .providerIds(Collections.singletonList(PROVIDER_ID))
                        .build())
                .build();
        return buildPaymentRequestWithProviderSelection(userSelectionProvider);
    }

    @SneakyThrows
    private void assertCanBrowseLink(URI link) {
        Request hppRequest = new Request.Builder().url(link.toURL()).build();
        Response hppResponse = getHttpClientInstance().newCall(hppRequest).execute();
        assertTrue(hppResponse.isSuccessful());
    }
}
