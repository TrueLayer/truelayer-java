package com.truelayer.java.acceptance;

import static com.truelayer.java.TestUtils.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.truelayer.java.commonapi.entities.SubmitPaymentReturnParametersRequest;
import com.truelayer.java.commonapi.entities.SubmitPaymentReturnParametersResponse;
import com.truelayer.java.entities.*;
import com.truelayer.java.entities.Address;
import com.truelayer.java.entities.accountidentifier.SortCodeAccountNumberAccountIdentifier;
import com.truelayer.java.http.entities.ApiResponse;
import com.truelayer.java.merchantaccounts.entities.MerchantAccount;
import com.truelayer.java.payments.entities.*;
import com.truelayer.java.payments.entities.StartAuthorizationFlowRequest.Redirect;
import com.truelayer.java.payments.entities.beneficiary.Beneficiary;
import com.truelayer.java.payments.entities.paymentdetail.PaymentDetail;
import com.truelayer.java.payments.entities.paymentdetail.forminput.Input;
import com.truelayer.java.payments.entities.paymentmethod.PaymentMethod;
import com.truelayer.java.payments.entities.providerselection.PreselectedProviderSelection;
import com.truelayer.java.payments.entities.providerselection.ProviderSelection;
import com.truelayer.java.payments.entities.providerselection.UserSelectedProviderSelection;
import com.truelayer.java.payments.entities.schemeselection.SchemeSelection;
import java.net.URI;
import java.time.LocalDate;
import java.util.*;
import lombok.SneakyThrows;
import okhttp3.*;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.*;

@Tag("acceptance")
public class PaymentsAcceptanceTests extends AcceptanceTests {

    public static final String RETURN_URI = "http://localhost:3000/callback";
    public static final String PROVIDER_ID = "mock-payments-gb-redirect";

    public static final String PROVIDER_ID_EMBEDDED = "mock-payments-de-embedded";

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
                .schemeSelection(
                        SchemeSelection.instantOnly().allowRemitterFee(true).build())
                .build();
        CreatePaymentRequest paymentRequest =
                buildPaymentRequestWithProviderSelection(userSelectionProvider, CurrencyCode.GBP);

        ApiResponse<CreatePaymentResponse> createPaymentResponse =
                tlClient.payments().createPayment(paymentRequest).get();

        assertNotError(createPaymentResponse);
        assertTrue(createPaymentResponse.getData().isAuthorizationRequired());

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
        CreatePaymentRequest paymentRequest =
                buildPaymentRequestWithProviderSelection(preselectionProvider, CurrencyCode.GBP);

        ApiResponse<CreatePaymentResponse> createPaymentResponse =
                tlClient.payments().createPayment(paymentRequest).get();

        assertNotError(createPaymentResponse);
        assertTrue(createPaymentResponse.getData().isAuthorizationRequired());

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
        CreatePaymentRequest paymentRequest = buildPaymentRequest(CurrencyCode.GBP);

        ApiResponse<CreatePaymentResponse> createPaymentResponse =
                tlClient.payments().createPayment(paymentRequest).get();

        assertNotError(createPaymentResponse);
        assertTrue(createPaymentResponse.getData().isAuthorizationRequired());

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
    @DisplayName("It should complete a redirect authorization flow for a payment")
    public void shouldCompleteARedirectAuthorizationFlowForAPayment() {
        // create payment
        CreatePaymentRequest paymentRequest = buildPaymentRequest(CurrencyCode.GBP);

        ApiResponse<CreatePaymentResponse> createPaymentResponse =
                tlClient.payments().createPayment(paymentRequest).get();

        assertNotError(createPaymentResponse);
        assertTrue(createPaymentResponse.getData().isAuthorizationRequired());

        // start the auth flow
        StartAuthorizationFlowRequest startAuthorizationFlowRequest = StartAuthorizationFlowRequest.builder()
                .redirect(Redirect.builder().returnUri(URI.create(RETURN_URI)).build())
                .withProviderSelection()
                .consent(StartAuthorizationFlowRequest.Consent.builder().build())
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

        // Submit consent
        ApiResponse<AuthorizationFlowResponse> submitConsentResponse = tlClient.payments()
                .submitConsent(
                        createPaymentResponse.getData().getId(),
                        SubmitConsentRequest.builder().build())
                .get();

        assertNotError(submitConsentResponse);
    }

    @SneakyThrows
    @Test
    @DisplayName("It should complete an embedded authorization flow for a payment")
    public void shouldCompleteAnEmbeddedAuthorizationFlowForAPayment() {
        // create payment
        CreatePaymentRequest paymentRequest = buildPaymentRequest(CurrencyCode.EUR);

        ApiResponse<CreatePaymentResponse> createPaymentResponse =
                tlClient.payments().createPayment(paymentRequest).get();

        assertNotError(createPaymentResponse);
        assertTrue(createPaymentResponse.getData().isAuthorizationRequired());

        // start the auth flow
        StartAuthorizationFlowRequest startAuthorizationFlowRequest = StartAuthorizationFlowRequest.builder()
                .redirect(Redirect.builder().returnUri(URI.create(RETURN_URI)).build())
                .withProviderSelection()
                .consent(StartAuthorizationFlowRequest.Consent.builder().build())
                .form(StartAuthorizationFlowRequest.Form.builder()
                        .inputTypes(Arrays.asList(Input.Type.TEXT, Input.Type.TEXT_WITH_IMAGE, Input.Type.SELECT))
                        .build())
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
                                .providerId(PROVIDER_ID_EMBEDDED)
                                .build())
                .get();

        assertNotError(submitProviderSelectionResponse);

        // Submit consent
        ApiResponse<AuthorizationFlowResponse> submitConsentResponse = tlClient.payments()
                .submitConsent(
                        createPaymentResponse.getData().getId(),
                        SubmitConsentRequest.builder().build())
                .get();

        assertNotError(submitConsentResponse);

        // Submit form 1
        Map<String, String> inputsStep1 = new HashMap<>();
        inputsStep1.put("remitter-name", "Andrea Di Lisio");
        inputsStep1.put("remitter-iban", "DE09500105171333647892");
        inputsStep1.put("psu-id", "test_username");
        inputsStep1.put("psu-password", "test_password");
        ApiResponse<AuthorizationFlowResponse> submitForm1Response = tlClient.payments()
                .submitForm(
                        createPaymentResponse.getData().getId(),
                        SubmitFormRequest.builder().inputs(inputsStep1).build())
                .get();

        assertNotError(submitForm1Response);

        // Submit form 2
        Map<String, String> inputsStep2 = new HashMap<>();
        inputsStep2.put("embedded-auth-flow-sca-select", "Sms");
        ApiResponse<AuthorizationFlowResponse> submitForm2Response = tlClient.payments()
                .submitForm(
                        createPaymentResponse.getData().getId(),
                        SubmitFormRequest.builder().inputs(inputsStep2).build())
                .get();

        assertNotError(submitForm2Response);

        // Submit form 3
        Map<String, String> inputsStep3 = new HashMap<>();
        inputsStep3.put("embedded-auth-flow-sms-input", "test_executed");
        ApiResponse<AuthorizationFlowResponse> submitForm3Response = tlClient.payments()
                .submitForm(
                        createPaymentResponse.getData().getId(),
                        SubmitFormRequest.builder().inputs(inputsStep3).build())
                .get();

        assertNotError(submitForm3Response);

        assertTrue(submitForm3Response
                .getData()
                .asAuthorizing()
                .getAuthorizationFlow()
                .getActions()
                .getNext()
                .isWaitForOutcome());
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
        CreatePaymentRequest paymentRequest =
                buildPaymentRequestWithProviderSelection(preselectionProvider, CurrencyCode.GBP);

        ApiResponse<CreatePaymentResponse> createPaymentResponse =
                tlClient.payments().createPayment(paymentRequest).get();

        assertNotError(createPaymentResponse);
        assertTrue(createPaymentResponse.getData().isAuthorizationRequired());

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
                .asAuthorizing()
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
        CreatePaymentRequest paymentRequest =
                buildPaymentRequestWithProviderSelection(preselectionProvider, CurrencyCode.GBP);

        ApiResponse<CreatePaymentResponse> createPaymentResponse =
                tlClient.payments().createPayment(paymentRequest).get();

        assertNotError(createPaymentResponse);
        assertTrue(createPaymentResponse.getData().isAuthorizationRequired());

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
                .asAuthorizing()
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
    private CreatePaymentRequest buildPaymentRequestWithProviderSelection(
            ProviderSelection providerSelection, CurrencyCode currencyCode) {
        MerchantAccount merchantAccount = getMerchantAccount(currencyCode);

        return CreatePaymentRequest.builder()
                .amountInMinor(RandomUtils.nextInt(50, 500))
                .currency(currencyCode)
                .paymentMethod(PaymentMethod.bankTransfer()
                        .providerSelection(providerSelection)
                        .beneficiary(Beneficiary.merchantAccount()
                                .merchantAccountId(merchantAccount.getId())
                                .reference(UUID.randomUUID().toString())
                                .build())
                        .build())
                .user(User.builder()
                        .name("Andrea Di Lisio")
                        .email("andrea@truelayer.com")
                        .dateOfBirth(LocalDate.now())
                        .address(Address.builder()
                                .addressLine1("1 Hardwick Street")
                                .city("London")
                                .state("Greater London")
                                .zip("EC1R 4RB")
                                .countryCode("GB")
                                .build())
                        .build())
                .metadata(Collections.singletonMap("a_custom_key", "a-custom-value"))
                .build();
    }

    private CreatePaymentRequest buildPaymentRequest(CurrencyCode currencyCode) {
        UserSelectedProviderSelection userSelectionProvider = UserSelectedProviderSelection.builder()
                .filter(ProviderFilter.builder()
                        .countries(Arrays.asList(CountryCode.GB, CountryCode.DE))
                        .releaseChannel(ReleaseChannel.GENERAL_AVAILABILITY)
                        .customerSegments(Collections.singletonList(CustomerSegment.RETAIL))
                        .providerIds(Arrays.asList(PROVIDER_ID, PROVIDER_ID_EMBEDDED))
                        .build())
                .build();
        return buildPaymentRequestWithProviderSelection(userSelectionProvider, currencyCode);
    }

    @SneakyThrows
    private void assertCanBrowseLink(URI link) {
        Request hppRequest = new Request.Builder().url(link.toURL()).build();
        Response hppResponse = getHttpClientInstance().newCall(hppRequest).execute();
        assertTrue(hppResponse.isSuccessful());
    }
}
