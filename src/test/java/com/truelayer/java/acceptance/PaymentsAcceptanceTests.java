package com.truelayer.java.acceptance;

import static com.truelayer.java.Constants.Scopes.PAYMENTS;
import static com.truelayer.java.TestUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.truelayer.java.*;
import com.truelayer.java.auth.AuthenticationHandler;
import com.truelayer.java.auth.IAuthenticationHandler;
import com.truelayer.java.entities.*;
import com.truelayer.java.entities.Address;
import com.truelayer.java.entities.accountidentifier.AccountIdentifier;
import com.truelayer.java.entities.accountidentifier.SortCodeAccountNumberAccountIdentifier;
import com.truelayer.java.http.RetrofitFactory;
import com.truelayer.java.http.auth.AccessTokenManager;
import com.truelayer.java.http.entities.ApiResponse;
import com.truelayer.java.http.interceptors.AuthenticationInterceptor;
import com.truelayer.java.http.interceptors.IdempotencyKeyGeneratorInterceptor;
import com.truelayer.java.http.interceptors.SignatureGeneratorInterceptor;
import com.truelayer.java.http.interceptors.TrueLayerAgentInterceptor;
import com.truelayer.java.merchantaccounts.entities.MerchantAccount;
import com.truelayer.java.payments.entities.*;
import com.truelayer.java.payments.entities.StartAuthorizationFlowRequest.Redirect;
import com.truelayer.java.payments.entities.beneficiary.Beneficiary;
import com.truelayer.java.payments.entities.paymentdetail.PaymentDetail;
import com.truelayer.java.payments.entities.paymentdetail.Status;
import com.truelayer.java.payments.entities.paymentdetail.forminput.Input;
import com.truelayer.java.payments.entities.paymentmethod.PaymentMethod;
import com.truelayer.java.payments.entities.paymentrefund.PaymentRefund;
import com.truelayer.java.payments.entities.providerselection.PreselectedProviderSelection;
import com.truelayer.java.payments.entities.providerselection.ProviderSelection;
import com.truelayer.java.payments.entities.providerselection.UserSelectedProviderSelection;
import com.truelayer.java.payments.entities.schemeselection.preselected.SchemeSelection;
import com.truelayer.java.payments.entities.verification.AutomatedVerification;
import com.truelayer.java.payments.entities.verification.Verification;
import com.truelayer.java.versioninfo.LibraryInfoLoader;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;
import lombok.*;
import okhttp3.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@Tag("acceptance")
public class PaymentsAcceptanceTests extends AcceptanceTests {

    public static final String RETURN_URI = "http://localhost:3000/callback";
    public static final String PROVIDER_ID = "mock-payments-gb-redirect";

    public static final String PROVIDER_ID_EMBEDDED = "mock-payments-de-embedded";

    private static Stream<Arguments> provideCurrencyScenarios() {
        return Stream.of(
                Arguments.of(CurrencyCode.GBP, CountryCode.GB),
                Arguments.of(CurrencyCode.EUR, CountryCode.FR),
                Arguments.of(CurrencyCode.PLN, CountryCode.PL),
                Arguments.of(CurrencyCode.NOK, CountryCode.NO));
    }

    @ParameterizedTest
    @MethodSource("provideCurrencyScenarios")
    @DisplayName("It should create and get by id a payment with user_selected provider")
    @SneakyThrows
    public void shouldCreateAPaymentWithUserSelectionProvider(CurrencyCode currency, CountryCode country) {
        // create payment
        UserSelectedProviderSelection userSelectionProvider = ProviderSelection.userSelected()
                .filter(ProviderFilter.builder()
                        .countries(Collections.singletonList(country))
                        .releaseChannel(ReleaseChannel.GENERAL_AVAILABILITY)
                        .customerSegments(Collections.singletonList(CustomerSegment.RETAIL))
                        .providerIds(Collections.singletonList(PROVIDER_ID))
                        .build())
                .schemeSelection(
                        com.truelayer.java.payments.entities.schemeselection.userselected.SchemeSelection.instantOnly()
                                .allowRemitterFee(true)
                                .build())
                .build();
        CreatePaymentRequest paymentRequest = buildPaymentRequestWithProviderSelection(userSelectionProvider, currency);

        ApiResponse<CreatePaymentResponse> createPaymentResponse =
                tlClient.payments().createPayment(paymentRequest).get();

        assertNotError(createPaymentResponse);
        assertTrue(createPaymentResponse.getData().isAuthorizationRequired());

        // get it by id
        ApiResponse<PaymentDetail> getPaymentByIdResponse = tlClient.payments()
                .getPayment(createPaymentResponse.getData().getId())
                .get();

        assertNotError(getPaymentByIdResponse);
        assertEquals(getPaymentByIdResponse.getData().getPaymentMethod(), paymentRequest.getPaymentMethod());
    }

    @Test
    @DisplayName("It should create and get by id a payment with preselected provider and preselected scheme_selection")
    @SneakyThrows
    public void shouldCreateAPaymentWithPreselectedProviderAndPreselectedSchemeSelection() {
        // create payment
        PreselectedProviderSelection preselectedProvider = ProviderSelection.preselected()
                .providerId(PROVIDER_ID)
                .schemeSelection(
                        com.truelayer.java.payments.entities.schemeselection.preselected.SchemeSelection.preselected()
                                .schemeId("faster_payments_service")
                                .build())
                .build();
        CreatePaymentRequest paymentRequest =
                buildPaymentRequestWithProviderSelection(preselectedProvider, CurrencyCode.GBP);

        ApiResponse<CreatePaymentResponse> createPaymentResponse =
                tlClient.payments().createPayment(paymentRequest).get();

        assertNotError(createPaymentResponse);
        assertTrue(createPaymentResponse.getData().isAuthorizationRequired());

        // get it by id
        ApiResponse<PaymentDetail> getPaymentByIdResponse = tlClient.payments()
                .getPayment(createPaymentResponse.getData().getId())
                .get();

        assertNotError(getPaymentByIdResponse);

        ProviderSelection providerSelection = getPaymentByIdResponse
                .getData()
                .getPaymentMethod()
                .asBankTransfer()
                .getProviderSelection();
        assertTrue(providerSelection.asPreselected().getSchemeSelection().isPreselected());
        assertEquals(
                providerSelection
                        .asPreselected()
                        .getSchemeSelection()
                        .asPreselected()
                        .getSchemeId(),
                "faster_payments_service");
    }

    @Test
    @DisplayName("It should create a payment to be used with Signup+")
    @SneakyThrows
    public void itShouldCreateAPaymentWithSignupPlusIntention() {
        ApiResponse<CreatePaymentResponse> createPaymentResponse = tlClient.payments()
                .createPayment(buildPaymentRequestWithProviderSelection(
                        buildPreselectedProviderSelection(),
                        CurrencyCode.GBP,
                        RelatedProducts.builder()
                                .signupPlus(Collections.emptyMap())
                                .build(),
                        null,
                        null))
                .get();

        assertNotError(createPaymentResponse);
    }

    @ParameterizedTest
    @DisplayName("It should create a payment with automated verification")
    @MethodSource("provideAutomatedVerifications")
    @SneakyThrows
    public void itShouldCreateAPaymentWithAutomatedVerification(Verification verification) {
        CurrencyCode currency = CurrencyCode.GBP;
        MerchantAccount account = getMerchantAccount(currency);
        CreatePaymentRequest.CreatePaymentRequestBuilder createPaymentRequestBuilder = CreatePaymentRequest.builder()
                .amountInMinor(100)
                .currency(currency)
                .paymentMethod(PaymentMethod.bankTransfer()
                        .providerSelection(ProviderSelection.preselected()
                                .providerId(PROVIDER_ID)
                                .schemeSelection(com.truelayer.java.payments.entities.schemeselection.preselected
                                        .SchemeSelection.instantPreferred()
                                        .build())
                                .build())
                        .beneficiary(Beneficiary.merchantAccount()
                                .merchantAccountId(account.getId())
                                .reference(UUID.randomUUID().toString())
                                .verification(verification)
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
                        .build());

        ApiResponse<CreatePaymentResponse> createPaymentResponse = tlClient.payments()
                .createPayment(createPaymentRequestBuilder.build())
                .get();

        assertNotError(createPaymentResponse);
    }

    @Test
    @DisplayName("It should create a payment with risk assessment")
    @SneakyThrows
    public void itShouldCreateAPaymentWithRiskAssessment() {
        ApiResponse<CreatePaymentResponse> createPaymentResponse = tlClient.payments()
                .createPayment(buildPaymentRequestWithProviderSelection(
                        buildPreselectedProviderSelection(),
                        CurrencyCode.GBP,
                        null,
                        RiskAssessment.builder().segment("a-custom-segment").build(),
                        null))
                .get();

        assertNotError(createPaymentResponse);
    }

    @Test
    @DisplayName("It should create and get by id a payment with preselected provider")
    @SneakyThrows
    public void shouldCreateAPaymentWithPreselectedProvider() {
        // create payment
        CreatePaymentRequest paymentRequest =
                buildPaymentRequestWithProviderSelection(buildPreselectedProviderSelection(), CurrencyCode.GBP);

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
        URI link = tlClient.hppLinkBuilder()
                .resourceId(createPaymentResponse.getData().getId())
                .resourceToken(createPaymentResponse.getData().getResourceToken())
                .returnUri(URI.create(RETURN_URI))
                .build();

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
                .submitConsent(createPaymentResponse.getData().getId())
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
                .submitConsent(createPaymentResponse.getData().getId())
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
        CreatePaymentRequest paymentRequest =
                buildPaymentRequestWithProviderSelection(buildPreselectedProviderSelection(), CurrencyCode.GBP);

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
        CreatePaymentRequest paymentRequest =
                buildPaymentRequestWithProviderSelection(buildPreselectedProviderSelection(), CurrencyCode.GBP);

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

        URI redirectUri = startAuthorizationFlowResponse
                .getData()
                .asAuthorizing()
                .getAuthorizationFlow()
                .getActions()
                .getNext()
                .asRedirect()
                .getUri();
        runAndAssertHeadlessResourceAuthorisation(
                tlClient,
                redirectUri,
                HeadlessResourceAuthorization.builder()
                        .action(HeadlessResourceAction.EXECUTE)
                        .resource(ResourceType.PAYMENT)
                        .build());
    }

    @SneakyThrows
    @Test
    @DisplayName("It should return attempt failed payment status if authorization is rejected with retry")
    public void shouldReturnAttemptFailedPaymentStatusIfAuthorizationIsRejectedWithRetry() {
        // create payment
        CreatePaymentRequest paymentRequest = buildPaymentRequestWithProviderSelectionWithRetry(
                buildPreselectedProviderSelection(), CurrencyCode.GBP);

        ApiResponse<CreatePaymentResponse> createPaymentResponse =
                tlClient.payments().createPayment(paymentRequest).get();

        assertNotError(createPaymentResponse);
        assertTrue(createPaymentResponse.getData().isAuthorizationRequired());

        // start the auth flow
        AuthorizationFlowResponse startAuthorizationFlowResponse =
                startAuthorizationFlowWithRetry(createPaymentResponse.getData().getId());
        URI redirectUri = startAuthorizationFlowResponse
                .asAuthorizing()
                .getAuthorizationFlow()
                .getActions()
                .getNext()
                .asRedirect()
                .getUri();

        // use specific action to get the authorization to be rejected
        runAndAssertHeadlessResourceAuthorisation(
                tlClient,
                redirectUri,
                HeadlessResourceAuthorization.builder()
                        .action(HeadlessResourceAction.REJECT_AUTHORISATION)
                        .resource(ResourceType.PAYMENT)
                        .build());

        // sometimes status change event has a bit of delay
        waitForPaymentStatusUpdate(tlClient, createPaymentResponse.getData().getId(), Status.ATTEMPT_FAILED);

        // get by id
        ApiResponse<PaymentDetail> getPaymentByIdResponse = tlClient.payments()
                .getPayment(createPaymentResponse.getData().getId())
                .get();

        assertNotError(getPaymentByIdResponse);
        assertTrue(getPaymentByIdResponse.getData().isAttemptFailed());
    }

    @SneakyThrows
    @Test
    @DisplayName("It should create a payment refund and get refund details")
    public void shouldCreateAPaymentRefundAndGetRefundDetails() {
        // create payment
        CreatePaymentRequest paymentRequest =
                buildPaymentRequestWithProviderSelection(buildPreselectedProviderSelection(), CurrencyCode.GBP);

        ApiResponse<CreatePaymentResponse> createPaymentResponse =
                tlClient.payments().createPayment(paymentRequest).get();

        assertNotError(createPaymentResponse);
        assertTrue(createPaymentResponse.getData().isAuthorizationRequired());

        String paymentId = createPaymentResponse.getData().getId();

        // start the auth flow
        StartAuthorizationFlowRequest startAuthorizationFlowRequest = StartAuthorizationFlowRequest.builder()
                .redirect(Redirect.builder()
                        .returnUri(URI.create(RETURN_URI))
                        .directReturnUri(URI.create(RETURN_URI))
                        .build())
                .withProviderSelection()
                .build();
        ApiResponse<AuthorizationFlowResponse> startAuthorizationFlowResponse = tlClient.payments()
                .startAuthorizationFlow(paymentId, startAuthorizationFlowRequest)
                .get();
        assertNotError(startAuthorizationFlowResponse);

        URI redirectUri = startAuthorizationFlowResponse
                .getData()
                .asAuthorizing()
                .getAuthorizationFlow()
                .getActions()
                .getNext()
                .asRedirect()
                .getUri();
        runAndAssertHeadlessResourceAuthorisation(
                tlClient,
                redirectUri,
                HeadlessResourceAuthorization.builder()
                        .action(HeadlessResourceAction.EXECUTE)
                        .resource(ResourceType.PAYMENT)
                        .build());

        waitForPaymentStatusUpdate(tlClient, paymentId, Status.SETTLED);

        // Create full payment refund
        CreatePaymentRefundRequest createPaymentRefundRequest = CreatePaymentRefundRequest.builder()
                .amountInMinor(paymentRequest.getAmountInMinor())
                .reference("Payment refund request")
                .metadata(Collections.singletonMap("a_custom_key", "a-custom-value"))
                .build();
        ApiResponse<CreatePaymentRefundResponse> createPaymentRefundResponseApiResponse = tlClient.payments()
                .createPaymentRefund(paymentId, createPaymentRefundRequest)
                .get();

        assertNotError(createPaymentRefundResponseApiResponse);

        String refundId = createPaymentRefundResponseApiResponse.getData().getId();

        // Get list of refunds
        ApiResponse<ListPaymentRefundsResponse> listPaymentRefundsResponseApiResponse =
                tlClient.payments().listPaymentRefunds(paymentId).get();

        assertNotError(listPaymentRefundsResponseApiResponse);
        assertEquals(
                1, listPaymentRefundsResponseApiResponse.getData().getItems().size());
        assertEquals(
                refundId,
                listPaymentRefundsResponseApiResponse
                        .getData()
                        .getItems()
                        .get(0)
                        .getId());

        // Get refund details
        ApiResponse<PaymentRefund> paymentRefundApiResponse =
                tlClient.payments().getPaymentRefundById(paymentId, refundId).get();

        assertNotError(paymentRefundApiResponse);
        assertEquals(refundId, paymentRefundApiResponse.getData().getId());
        assertEquals(
                createPaymentRefundRequest.getAmountInMinor(),
                paymentRefundApiResponse.getData().getAmountInMinor());
        assertEquals(
                createPaymentRefundRequest.getMetadata(),
                paymentRefundApiResponse.getData().getMetadata());
    }

    @SneakyThrows
    @Test
    @DisplayName("It should create a payment and cancel it")
    public void shouldCreateAPaymentAndCancelIt() {
        // create payment
        CreatePaymentRequest paymentRequest =
                buildPaymentRequestWithProviderSelection(buildPreselectedProviderSelection(), CurrencyCode.GBP);

        ApiResponse<CreatePaymentResponse> createPaymentResponse =
                tlClient.payments().createPayment(paymentRequest).get();

        assertNotError(createPaymentResponse);
        assertTrue(createPaymentResponse.getData().isAuthorizationRequired());

        // Cancel the payment
        ApiResponse<Void> cancelPaymentResponse = tlClient.payments()
                .cancelPayment(createPaymentResponse.getData().getId())
                .get();
        assertNotError(cancelPaymentResponse);
    }

    private PreselectedProviderSelection buildPreselectedProviderSelection() {
        return ProviderSelection.preselected()
                .providerId(PROVIDER_ID)
                .schemeSelection(SchemeSelection.instantPreferred().build())
                .remitter(Remitter.builder()
                        .accountHolderName("Andrea Di Lisio")
                        .accountIdentifier(SortCodeAccountNumberAccountIdentifier.builder()
                                .accountNumber("12345678")
                                .sortCode("123456")
                                .build())
                        .build())
                .build();
    }

    @SneakyThrows
    private Beneficiary buildBeneficiary(CurrencyCode currencyCode) {
        switch (currencyCode) {
            case GBP:
                MerchantAccount gbpAccount = getMerchantAccount(currencyCode);
                return Beneficiary.merchantAccount()
                        .merchantAccountId(gbpAccount.getId())
                        .reference(UUID.randomUUID().toString())
                        .statementReference(RandomStringUtils.randomAlphanumeric(18))
                        .build();
            case EUR:
                MerchantAccount eurAccount = getMerchantAccount(currencyCode);
                return Beneficiary.merchantAccount()
                        .merchantAccountId(eurAccount.getId())
                        .reference(UUID.randomUUID().toString())
                        .build();
            case PLN:
                return Beneficiary.externalAccount()
                        .accountHolderName("Ben Eficiary")
                        .accountIdentifier(AccountIdentifier.nrb()
                                .nrb("12345678901234567890123456")
                                .build())
                        .reference("some reference")
                        .build();
            case NOK:
                return Beneficiary.externalAccount()
                        .accountHolderName("Ben Eficiary")
                        .accountIdentifier(AccountIdentifier.bban()
                                .bban("DE09500105171333647892")
                                .build())
                        .reference("some reference")
                        .build();
            default:
                throw new RuntimeException("currency not supported");
        }
    }

    private CreatePaymentRequest buildPaymentRequestWithProviderSelection(
            ProviderSelection providerSelection, CurrencyCode currencyCode) {
        return buildPaymentRequestWithProviderSelection(providerSelection, currencyCode, null, null, null);
    }

    private CreatePaymentRequest buildPaymentRequestWithProviderSelectionWithRetry(
            ProviderSelection providerSelection, CurrencyCode currencyCode) {
        return buildPaymentRequestWithProviderSelection(providerSelection, currencyCode, null, null, new Retry());
    }

    @SneakyThrows
    private CreatePaymentRequest buildPaymentRequestWithProviderSelection(
            ProviderSelection providerSelection,
            CurrencyCode currencyCode,
            RelatedProducts relatedProducts,
            RiskAssessment riskAssessment,
            Retry retry) {
        CreatePaymentRequest.CreatePaymentRequestBuilder builder = CreatePaymentRequest.builder()
                .amountInMinor(ThreadLocalRandom.current().nextInt(50, 500))
                .currency(currencyCode)
                .paymentMethod(PaymentMethod.bankTransfer()
                        .providerSelection(providerSelection)
                        .beneficiary(buildBeneficiary(currencyCode))
                        .retry(retry)
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
                .metadata(Collections.singletonMap("a_custom_key", "a-custom-value"));

        if (relatedProducts != null) {
            builder.relatedProducts(relatedProducts);
        }

        if (riskAssessment != null) {
            builder.riskAssessment(riskAssessment);
        }

        return builder.build();
    }

    private CreatePaymentRequest buildPaymentRequest(CurrencyCode currencyCode) {
        UserSelectedProviderSelection userSelectionProvider = UserSelectedProviderSelection.builder()
                .filter(ProviderFilter.builder()
                        .countries(Arrays.asList(
                                CountryCode.AT,
                                CountryCode.BE,
                                CountryCode.DE,
                                CountryCode.DK,
                                CountryCode.ES,
                                CountryCode.FI,
                                CountryCode.FR,
                                CountryCode.GB,
                                CountryCode.IE,
                                CountryCode.IT,
                                CountryCode.LT,
                                CountryCode.NL,
                                CountryCode.NO,
                                CountryCode.PL,
                                CountryCode.PT,
                                CountryCode.RO))
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

    // since the StartAuthorizationFlowRequest object does not support retry property
    // we need to do a raw HTTP call with a JSON string request body
    @SneakyThrows
    private static AuthorizationFlowResponse startAuthorizationFlowWithRetry(String paymentId) {

        // build HTTP Client with required interceptors for auth token, signature etc.
        OkHttpClient baseHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new IdempotencyKeyGeneratorInterceptor())
                .build();

        SigningOptions signingOptions = SigningOptions.builder()
                .keyId(System.getenv("TL_SIGNING_KEY_ID"))
                .privateKey(System.getenv("TL_SIGNING_PRIVATE_KEY").getBytes(StandardCharsets.UTF_8))
                .build();

        ClientCredentials clientCredentials = ClientCredentials.builder()
                .clientId(System.getenv("TL_CLIENT_ID"))
                .clientSecret(System.getenv("TL_CLIENT_SECRET"))
                .build();

        IAuthenticationHandler authenticationHandler = AuthenticationHandler.New()
                .clientCredentials(clientCredentials)
                .httpClient(RetrofitFactory.build(baseHttpClient, environment.getAuthApiUri()))
                .build();

        AccessTokenManager.AccessTokenManagerBuilder accessTokenManagerBuilder =
                AccessTokenManager.builder().authenticationHandler(authenticationHandler);
        AccessTokenManager accessTokenManager = accessTokenManagerBuilder.build();

        OkHttpClient httpClient = baseHttpClient
                .newBuilder()
                .addInterceptor(new TrueLayerAgentInterceptor(new LibraryInfoLoader().load()))
                .addInterceptor(new IdempotencyKeyGeneratorInterceptor())
                .addInterceptor(new SignatureGeneratorInterceptor(signingOptions))
                .addInterceptor(new AuthenticationInterceptor(accessTokenManager))
                .build();

        // build base StartAuthorizationFlowRequest object
        StartAuthorizationFlowRequest startAuthorizationFlowRequest = StartAuthorizationFlowRequest.builder()
                .redirect(Redirect.builder()
                        .returnUri(URI.create(RETURN_URI))
                        .directReturnUri(URI.create(RETURN_URI))
                        .build())
                .withProviderSelection()
                .build();

        // serialize the base object to json and append retry object
        ObjectMapper mapper = Utils.getObjectMapper();
        String baseRequestJsonString = mapper.writeValueAsString(startAuthorizationFlowRequest);
        JsonNode jsonNode = mapper.readTree(baseRequestJsonString);
        ((ObjectNode) jsonNode).set("retry", mapper.createObjectNode());
        String requestJsonString = mapper.writeValueAsString(jsonNode);

        // build url
        HttpUrl url = HttpUrl.parse(MessageFormat.format(
                "{0}payments/{1}/authorization-flow",
                Environment.sandbox().getPaymentsApiUri().toString(), paymentId));

        RequestBody body = RequestBody.create(requestJsonString, MediaType.parse("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(Objects.requireNonNull(url))
                .tag(
                        RequestScopes.class,
                        RequestScopes.builder().scope(PAYMENTS).build())
                .post(body)
                .build();

        String responseBody;
        try (Response response = httpClient.newCall(request).execute()) {
            assertTrue(response.isSuccessful());
            assertNotNull(response.body());
            responseBody = response.body().string();
        }
        return mapper.readValue(responseBody, AuthorizationFlowResponse.class);
    }

    private static Stream<Arguments> provideAutomatedVerifications() {
        return Stream.of(
                Arguments.of(AutomatedVerification.builder().withRemitterName().build()),
                Arguments.of(AutomatedVerification.builder()
                        .withRemitterDateOfBirth()
                        .build()));
    }
}
