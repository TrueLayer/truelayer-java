package com.truelayer.java.acceptance;

import static com.truelayer.java.TestUtils.assertNotError;
import static com.truelayer.java.TestUtils.getHttpClientInstance;
import static com.truelayer.java.mandates.entities.mandate.Mandate.Type.COMMERCIAL;
import static com.truelayer.java.mandates.entities.mandate.Mandate.Type.SWEEPING;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;

import com.truelayer.java.entities.CurrencyCode;
import com.truelayer.java.entities.ProviderFilter;
import com.truelayer.java.entities.User;
import com.truelayer.java.entities.accountidentifier.AccountIdentifier;
import com.truelayer.java.entities.providerselection.ProviderSelection;
import com.truelayer.java.http.entities.ApiResponse;
import com.truelayer.java.mandates.entities.*;
import com.truelayer.java.mandates.entities.Constraints.PeriodicLimits;
import com.truelayer.java.mandates.entities.Constraints.PeriodicLimits.Limit;
import com.truelayer.java.mandates.entities.beneficiary.Beneficiary;
import com.truelayer.java.mandates.entities.mandate.Mandate;
import com.truelayer.java.mandates.entities.mandatedetail.MandateDetail;
import com.truelayer.java.mandates.entities.mandatedetail.Status;
import com.truelayer.java.payments.entities.*;
import com.truelayer.java.payments.entities.paymentdetail.PaymentDetail;
import com.truelayer.java.payments.entities.paymentmethod.PaymentMethod;
import java.net.URI;
import java.util.Collections;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import lombok.SneakyThrows;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("acceptance")
public class MandatesAcceptanceTests extends AcceptanceTests {

    public static final String RETURN_URI = "http://localhost:3000/callback";
    public static final String PROVIDER_ID = "ob-uki-mock-bank-sbox";

    @Test
    @DisplayName("It should create a VRP sweeping mandate with preselected provider")
    @SneakyThrows
    public void itShouldCreateASweepingMandateWithPreselectedProvider() {
        // create mandate
        ProviderSelection preselectedProvider =
                ProviderSelection.preselected().providerId(PROVIDER_ID).build();
        ApiResponse<CreateMandateResponse> createMandateResponse = tlClient.mandates()
                .createMandate(createMandateRequest(SWEEPING, preselectedProvider, Limit.PeriodAlignment.CALENDAR))
                .get();
        assertNotError(createMandateResponse);

        // start auth flow
        ApiResponse<AuthorizationFlowResponse> startAuthorizationFlowResponse =
                startAuthFlowForMandate(createMandateResponse.getData().getId());
        assertNotError(startAuthorizationFlowResponse);
    }

    @Test
    @Disabled("The provider ob-uki-mock-bank-sbox does not support commercial VRP yet")
    @DisplayName("It should create a VRP commercial mandate with preselected provider")
    @SneakyThrows
    public void itShouldCreateACommercialMandateWithPreselectedProvider() {
        // create mandate
        ProviderSelection preselectedProvider =
                ProviderSelection.preselected().providerId(PROVIDER_ID).build();
        ApiResponse<CreateMandateResponse> createMandateResponse = tlClient.mandates()
                .createMandate(createMandateRequest(COMMERCIAL, preselectedProvider, Limit.PeriodAlignment.CALENDAR))
                .get();
        assertNotError(createMandateResponse);

        // start auth flow
        ApiResponse<AuthorizationFlowResponse> startAuthorizationFlowResponse =
                startAuthFlowForMandate(createMandateResponse.getData().getId());
        assertNotError(startAuthorizationFlowResponse);
    }

    @Test
    @DisplayName("It should create a mandate with user_selected provider")
    @SneakyThrows
    public void itShouldCreateAMandateWithUserSelectedProvider() {
        // create mandate
        ProviderSelection userSelectedProvider = ProviderSelection.userSelected()
                .filter(ProviderFilter.builder()
                        .releaseChannel(ReleaseChannel.PRIVATE_BETA)
                        .build())
                .build();
        ApiResponse<CreateMandateResponse> createMandateResponse = tlClient.mandates()
                .createMandate(createMandateRequest(SWEEPING, userSelectedProvider, Limit.PeriodAlignment.CALENDAR))
                .get();
        assertNotError(createMandateResponse);

        // start auth flow
        ApiResponse<AuthorizationFlowResponse> startAuthorizationFlowResponse =
                startAuthFlowForMandate(createMandateResponse.getData().getId());
        assertNotError(startAuthorizationFlowResponse);

        // submit provider selection
        SubmitProviderSelectionRequest submitProviderSelectionRequest =
                SubmitProviderSelectionRequest.builder().providerId(PROVIDER_ID).build();
        ApiResponse<AuthorizationFlowResponse> submitProviderSelectionResponse = tlClient.mandates()
                .submitProviderSelection(createMandateResponse.getData().getId(), submitProviderSelectionRequest)
                .get();
        assertNotError(submitProviderSelectionResponse);
    }

    @Test
    @DisplayName("It should get a list of mandates")
    @SneakyThrows
    public void itShouldGetAListOfMandates() {
        ApiResponse<ListMandatesResponse> listMandatesResponse =
                tlClient.mandates().listMandates().get();

        assertNotError(listMandatesResponse);
    }

    @Test
    @DisplayName("It should get confirm funds")
    @SneakyThrows
    public void itShouldGetFunds() {
        // create mandate
        ProviderSelection preselectedProvider =
                ProviderSelection.preselected().providerId(PROVIDER_ID).build();
        ApiResponse<CreateMandateResponse> createMandateResponse = tlClient.mandates()
                .createMandate(createMandateRequest(SWEEPING, preselectedProvider, Limit.PeriodAlignment.CALENDAR))
                .get();
        assertNotError(createMandateResponse);

        // start auth flow
        StartAuthorizationFlowRequest startAuthorizationFlowRequest = StartAuthorizationFlowRequest.builder()
                .withProviderSelection()
                .redirect(StartAuthorizationFlowRequest.Redirect.builder()
                        .returnUri(URI.create(RETURN_URI))
                        .build())
                .build();

        ApiResponse<AuthorizationFlowResponse> startAuthorizationFlowResponse = tlClient.mandates()
                .startAuthorizationFlow(createMandateResponse.getData().getId(), startAuthorizationFlowRequest)
                .get();

        assertNotError(startAuthorizationFlowResponse);

        // authorize the created mandate without explicit user interaction
        authorizeMandate(startAuthorizationFlowResponse.getData());
        waitForMandateToBeAuthorized(createMandateResponse.getData().getId());

        // finally make a confirmation of funds request for 1 penny
        ApiResponse<GetConfirmationOfFundsResponse> getConfirmationOfFundsResponseApiResponse = tlClient.mandates()
                .getConfirmationOfFunds(createMandateResponse.getData().getId(), "1", "GBP")
                .get();

        assertNotError(getConfirmationOfFundsResponseApiResponse);
        assertEquals(true, getConfirmationOfFundsResponseApiResponse.getData().getConfirmed());
    }

    @Test
    @DisplayName("It should get mandate constraints")
    @SneakyThrows
    public void itShouldGetConstraints() {
        // create mandate
        ProviderSelection preselectedProvider =
                ProviderSelection.preselected().providerId(PROVIDER_ID).build();
        CreateMandateRequest createMandateRequest =
                createMandateRequest(SWEEPING, preselectedProvider, Limit.PeriodAlignment.CONSENT);
        ApiResponse<CreateMandateResponse> createMandateResponse =
                tlClient.mandates().createMandate(createMandateRequest).get();
        assertNotError(createMandateResponse);

        // save the params to be used later
        Integer paymentAmount = 1;
        Integer calculatedRemainingPerMonth = createMandateRequest
                        .getConstraints()
                        .getPeriodicLimits()
                        .getMonth()
                        .getMaximumAmount()
                - paymentAmount;

        PeriodicLimitDetail periodicLimitDetail = PeriodicLimitDetail.builder()
                .maximumAvailableAmount(createMandateRequest
                        .getConstraints()
                        .getPeriodicLimits()
                        .getMonth()
                        .getMaximumAmount())
                .currentAmount(paymentAmount)
                .periodAlignment(createMandateRequest
                        .getConstraints()
                        .getPeriodicLimits()
                        .toString())
                .build();

        PeriodicLimit periodicLimit =
                PeriodicLimit.builder().month(periodicLimitDetail).build();

        GetConstraintsResponse constraintsExpectedResponse = GetConstraintsResponse.builder()
                .validFrom(createMandateRequest.getConstraints().getValidFrom())
                .validTo(createMandateRequest.getConstraints().getValidTo())
                .maximumIndividualAmount(createMandateRequest.getConstraints().getMaximumIndividualAmount())
                .periodicLimits(periodicLimit)
                .build();

        // start auth flow
        StartAuthorizationFlowRequest startAuthorizationFlowRequest = StartAuthorizationFlowRequest.builder()
                .withProviderSelection()
                .redirect(StartAuthorizationFlowRequest.Redirect.builder()
                        .returnUri(URI.create(RETURN_URI))
                        .build())
                .build();

        ApiResponse<AuthorizationFlowResponse> startAuthorizationFlowResponse = tlClient.mandates()
                .startAuthorizationFlow(createMandateResponse.getData().getId(), startAuthorizationFlowRequest)
                .get();

        assertNotError(startAuthorizationFlowResponse);

        // authorize the created mandate without explicit user interaction
        authorizeMandate(startAuthorizationFlowResponse.getData());
        waitForMandateToBeAuthorized(createMandateResponse.getData().getId());

        // get mandate by id
        ApiResponse<MandateDetail> getMandateResponse = tlClient.mandates()
                .getMandate(createMandateResponse.getData().getId())
                .get();
        // create a payment on mandate
        CreatePaymentRequest createPaymentRequest = CreatePaymentRequest.builder()
                .amountInMinor(paymentAmount)
                .currency(getMandateResponse.getData().getCurrency())
                .paymentMethod(PaymentMethod.mandate()
                        .mandateId(getMandateResponse.getData().getId())
                        .reference("a-custom-reference")
                        .build())
                .build();

        ApiResponse<CreatePaymentResponse> createPaymentResponse =
                tlClient.payments().createPayment(createPaymentRequest).get();

        assertNotError(createPaymentResponse);

        waitForPaymentToBeExecuted(createPaymentResponse.getData().getId());

        // finally make a Get constraints request
        ApiResponse<GetConstraintsResponse> getConstraintsResponseApiResponse = tlClient.mandates()
                .getMandateConstraints(createMandateResponse.getData().getId())
                .get();

        assertNotError(getConstraintsResponseApiResponse);
        assertEquals(
                constraintsExpectedResponse.getMaximumIndividualAmount(),
                getConstraintsResponseApiResponse.getData().getMaximumIndividualAmount());
        assertEquals(
                constraintsExpectedResponse.getValidTo(),
                getConstraintsResponseApiResponse.getData().getValidTo());
        assertEquals(
                constraintsExpectedResponse.getValidFrom(),
                getConstraintsResponseApiResponse.getData().getValidFrom());
        assertEquals(
                constraintsExpectedResponse.getPeriodicLimits().getMonth().getCurrentAmount(),
                getConstraintsResponseApiResponse
                        .getData()
                        .getPeriodicLimits()
                        .getMonth()
                        .getCurrentAmount());
        assertEquals(
                constraintsExpectedResponse.getPeriodicLimits().getMonth().getMaximumAvailableAmount(),
                getConstraintsResponseApiResponse
                        .getData()
                        .getPeriodicLimits()
                        .getMonth()
                        .getMaximumAvailableAmount());
    }

    @Test
    @DisplayName("It should create and revoke a mandate")
    @SneakyThrows
    public void itShouldCreateAndRevokeAMandate() {
        // create mandate
        ProviderSelection preselectedProvider =
                ProviderSelection.preselected().providerId(PROVIDER_ID).build();
        ApiResponse<CreateMandateResponse> createMandateResponse = tlClient.mandates()
                .createMandate(createMandateRequest(SWEEPING, preselectedProvider, Limit.PeriodAlignment.CALENDAR))
                .get();
        assertNotError(createMandateResponse);

        // we must authorize the mandate before revoking it
        ApiResponse<AuthorizationFlowResponse> startAuthorizationFlowResponse =
                startAuthFlowForMandate(createMandateResponse.getData().getId());
        assertNotError(startAuthorizationFlowResponse);

        // authorize the created mandate without explicit user interaction
        authorizeMandate(startAuthorizationFlowResponse.getData());
        waitForMandateToBeAuthorized(createMandateResponse.getData().getId());

        // revoke mandate by id
        ApiResponse<Void> revokeMandateResponse = tlClient.mandates()
                .revokeMandate(createMandateResponse.getData().getId())
                .get();
        assertNotError(revokeMandateResponse);
    }

    @Test
    @DisplayName("It should create a payment on mandate")
    @SneakyThrows
    public void itShouldCreateAPaymentOnMandate() {
        // create mandate
        ProviderSelection preselectedProvider =
                ProviderSelection.preselected().providerId(PROVIDER_ID).build();
        ApiResponse<CreateMandateResponse> createMandateResponse = tlClient.mandates()
                .createMandate(createMandateRequest(SWEEPING, preselectedProvider, Limit.PeriodAlignment.CALENDAR))
                .get();
        assertNotError(createMandateResponse);

        // start auth flow
        StartAuthorizationFlowRequest startAuthorizationFlowRequest = StartAuthorizationFlowRequest.builder()
                .withProviderSelection()
                .redirect(StartAuthorizationFlowRequest.Redirect.builder()
                        .returnUri(URI.create(RETURN_URI))
                        .build())
                .build();

        ApiResponse<AuthorizationFlowResponse> startAuthorizationFlowResponse = tlClient.mandates()
                .startAuthorizationFlow(createMandateResponse.getData().getId(), startAuthorizationFlowRequest)
                .get();

        assertNotError(startAuthorizationFlowResponse);

        // authorize the created mandate without explicit user interaction
        authorizeMandate(startAuthorizationFlowResponse.getData());
        waitForMandateToBeAuthorized(createMandateResponse.getData().getId());

        // get mandate by id
        ApiResponse<MandateDetail> getMandateResponse = tlClient.mandates()
                .getMandate(createMandateResponse.getData().getId())
                .get();

        assertNotError(getMandateResponse);

        // create a payment on mandate
        CreatePaymentRequest createPaymentRequest = CreatePaymentRequest.builder()
                .amountInMinor(getMandateResponse.getData().getConstraints().getMaximumIndividualAmount())
                .currency(getMandateResponse.getData().getCurrency())
                .paymentMethod(PaymentMethod.mandate()
                        .mandateId(getMandateResponse.getData().getId())
                        .reference("a-custom-reference")
                        .build())
                .build();

        ApiResponse<CreatePaymentResponse> createPaymentResponse =
                tlClient.payments().createPayment(createPaymentRequest).get();

        assertNotError(createPaymentResponse);
    }

    private CreateMandateRequest createMandateRequest(
            Mandate.Type type, ProviderSelection providerSelection, Limit.PeriodAlignment periodAlignment) {
        Mandate mandate = null;
        if (type.equals(Mandate.Type.COMMERCIAL)) {
            mandate = Mandate.vrpCommercialMandate()
                    .providerSelection(providerSelection)
                    //                    .reference("a-commercial-ref")
                    .beneficiary(Beneficiary.externalAccount()
                            .accountIdentifier(AccountIdentifier.sortCodeAccountNumber()
                                    .accountNumber("10003957")
                                    .sortCode("140662")
                                    .build())
                            .accountHolderName("Andrea Java SDK")
                            .build())
                    .build();
        } else {
            mandate = Mandate.vrpSweepingMandate()
                    .providerSelection(providerSelection)
                    //                    .reference("a-sweeping-ref")
                    .beneficiary(Beneficiary.externalAccount()
                            .accountIdentifier(AccountIdentifier.sortCodeAccountNumber()
                                    .accountNumber("10003957")
                                    .sortCode("140662")
                                    .build())
                            .accountHolderName("Andrea Java SDK")
                            .build())
                    .build();
        }

        return CreateMandateRequest.builder()
                .mandate(mandate)
                .currency(CurrencyCode.GBP)
                .user(User.builder()
                        .id(UUID.randomUUID().toString())
                        .name("John Smith")
                        .email("john@truelayer.com")
                        .build())
                .constraints(Constraints.builder()
                        .periodicLimits(PeriodicLimits.builder()
                                .month(Limit.builder()
                                        .maximumAmount(2000)
                                        .periodAlignment(periodAlignment)
                                        .build())
                                .build())
                        .maximumIndividualAmount(1000)
                        .build())
                .metadata(Collections.singletonMap("a_custom_key", "a-custom-value"))
                .build();
    }

    @SneakyThrows
    private ApiResponse<AuthorizationFlowResponse> startAuthFlowForMandate(String mandateId) {
        // start auth flow
        StartAuthorizationFlowRequest startAuthorizationFlowRequest = StartAuthorizationFlowRequest.builder()
                .withProviderSelection()
                .redirect(StartAuthorizationFlowRequest.Redirect.builder()
                        .returnUri(URI.create(RETURN_URI))
                        .build())
                .build();

        return tlClient.mandates()
                .startAuthorizationFlow(mandateId, startAuthorizationFlowRequest)
                .get();
    }

    @SneakyThrows
    private void authorizeMandate(AuthorizationFlowResponse authorizationFlowResponse) {
        // first we check the state of the mandate returned by the gateway
        assertTrue(
                authorizationFlowResponse.isAuthorizing(),
                "Mandate status is" + authorizationFlowResponse.getStatus().getStatus());

        // follow the redirect uri and parse its Location HTTP response header
        URI redirectUri = authorizationFlowResponse
                .asAuthorizing()
                .getAuthorizationFlow()
                .getActions()
                .getNext()
                .asRedirect()
                .getUri();
        Response redirectResponse = getHttpClientInstance()
                .newCall(new Request.Builder().url(redirectUri.toURL()).get().build())
                .execute();
        assertTrue(redirectResponse.isRedirect());
        String location = Objects.requireNonNull(redirectResponse.header("location"));
        assertFalse(location.isEmpty());

        // Parse the header returned and build the next provider parameters submission request
        URI paymentsSpaRedirectUrl = URI.create(location);
        boolean isQuery = paymentsSpaRedirectUrl.getQuery() != null
                && paymentsSpaRedirectUrl.getQuery().isEmpty();
        String rawParameters = isQuery ? paymentsSpaRedirectUrl.getQuery() : paymentsSpaRedirectUrl.getRawFragment();
        assertTrue(
                rawParameters.contains("state=mandate-"),
                "query/fragment parameters do not contain expected mandate state");
        String sanitizedParameters = rawParameters.replaceFirst("state=mandate-", "state=");
        String jsonPayload = isQuery
                ? "{\"query\": \"?" + sanitizedParameters + "\"}"
                : "{\"fragment\": \"#" + sanitizedParameters + "\"}";

        // Execute the SPA provider submission parameters request
        Response submitProviderParamsResponse = getHttpClientInstance()
                .newCall(new Request.Builder()
                        .url(environment.getPaymentsApiUri().toString() + "/spa/payments-provider-return")
                        .post(RequestBody.create(MediaType.get("application/json; charset=utf-8"), jsonPayload))
                        .build())
                .execute();
        assertTrue(submitProviderParamsResponse.isSuccessful());
    }

    private void waitForMandateToBeAuthorized(String mandateId) {
        await().with()
                .pollInterval(1, TimeUnit.SECONDS)
                .atMost(5, TimeUnit.SECONDS)
                .until(() -> {
                    // get mandate by id
                    ApiResponse<MandateDetail> getMandateResponse =
                            tlClient.mandates().getMandate(mandateId).get();
                    assertNotError(getMandateResponse);
                    return getMandateResponse.getData().getStatus().equals(Status.AUTHORIZED);
                });
    }

    private void waitForPaymentToBeExecuted(String paymentId) {
        await().with()
                .pollInterval(1, TimeUnit.SECONDS)
                .atMost(15, TimeUnit.SECONDS)
                .until(() -> {
                    // get payment by id
                    ApiResponse<PaymentDetail> getPaymentResponse =
                            tlClient.payments().getPayment(paymentId).get();
                    assertNotError(getPaymentResponse);
                    return getPaymentResponse
                            .getData()
                            .getStatus()
                            .equals(com.truelayer.java.payments.entities.paymentdetail.Status.EXECUTED);
                });
    }
}
