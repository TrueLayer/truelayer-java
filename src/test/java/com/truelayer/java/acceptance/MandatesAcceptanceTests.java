package com.truelayer.java.acceptance;

import static com.truelayer.java.TestUtils.*;
import static com.truelayer.java.mandates.entities.Constraints.PeriodicLimits.Limit.PeriodAlignment.CALENDAR;
import static com.truelayer.java.mandates.entities.mandate.Mandate.Type.COMMERCIAL;
import static com.truelayer.java.mandates.entities.mandate.Mandate.Type.SWEEPING;
import static org.junit.jupiter.api.Assertions.*;

import com.truelayer.java.entities.CurrencyCode;
import com.truelayer.java.entities.ProviderFilter;
import com.truelayer.java.entities.User;
import com.truelayer.java.entities.accountidentifier.AccountIdentifier;
import com.truelayer.java.entities.beneficiary.Beneficiary;
import com.truelayer.java.entities.providerselection.ProviderSelection;
import com.truelayer.java.http.entities.ApiResponse;
import com.truelayer.java.mandates.entities.*;
import com.truelayer.java.mandates.entities.Constraints.PeriodicLimits;
import com.truelayer.java.mandates.entities.Constraints.PeriodicLimits.Limit;
import com.truelayer.java.mandates.entities.mandate.Mandate;
import com.truelayer.java.mandates.entities.mandatedetail.MandateDetail;
import com.truelayer.java.payments.entities.*;
import com.truelayer.java.payments.entities.paymentmethod.PaymentMethod;
import java.net.URI;
import java.util.Collections;
import java.util.Objects;
import java.util.UUID;
import lombok.SneakyThrows;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.junit.jupiter.api.*;

@Tag("acceptance")
public class MandatesAcceptanceTests extends AcceptanceTests {

    public static final String RETURN_URI = "http://localhost:3000/callback";
    public static final String PROVIDER_ID = "ob-uki-mock-bank-sbox";

    @Test
    @DisplayName("It should create a sweeping mandate with preselected provider")
    @SneakyThrows
    public void itShouldCreateASweepingMandateWithPreselectedProvider() {
        // create mandate
        ProviderSelection preselectedProvider =
                ProviderSelection.preselected().providerId(PROVIDER_ID).build();
        ApiResponse<CreateMandateResponse> createMandateResponse = tlClient.mandates()
                .createMandate(createMandateRequest(SWEEPING, preselectedProvider))
                .get();
        assertNotError(createMandateResponse);

        // start auth flow
        ApiResponse<AuthorizationFlowResponse> startAuthorizationFlowResponse =
                startAuthFlowForMandate(createMandateResponse.getData().getId());
        assertNotError(startAuthorizationFlowResponse);
    }

    @Test
    @DisplayName("It should create a commercial mandate with preselected provider")
    @SneakyThrows
    public void itShouldCreateACommercialMandateWithPreselectedProvider() {
        // create mandate
        ProviderSelection preselectedProvider =
                ProviderSelection.preselected().providerId(PROVIDER_ID).build();
        ApiResponse<CreateMandateResponse> createMandateResponse = tlClient.mandates()
                .createMandate(createMandateRequest(COMMERCIAL, preselectedProvider))
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
                .createMandate(createMandateRequest(SWEEPING, userSelectedProvider))
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
                .createMandate(createMandateRequest(SWEEPING, preselectedProvider))
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

        // get mandate by id
        ApiResponse<MandateDetail> getMandateResponse = tlClient.mandates()
                .getMandate(createMandateResponse.getData().getId())
                .get();

        assertNotError(getMandateResponse);

        // finally make a confirmation of funds request for 1 penny
        ApiResponse<GetConfirmationOfFundsResponse> getConfirmationOfFundsResponseApiResponse = tlClient.mandates()
                .getConfirmationOfFunds(getMandateResponse.getData().getId(), "1", "GBP")
                .get();

        assertNotError(getConfirmationOfFundsResponseApiResponse);
        assertEquals(true, getConfirmationOfFundsResponseApiResponse.getData().getConfirmed());
    }

    @Test
    @DisplayName("It should create and revoke a mandate")
    @SneakyThrows
    public void itShouldCreateAndRevokeAMandate() {
        // create mandate
        ProviderSelection preselectedProvider =
                ProviderSelection.preselected().providerId(PROVIDER_ID).build();
        ApiResponse<CreateMandateResponse> createMandateResponse = tlClient.mandates()
                .createMandate(createMandateRequest(SWEEPING, preselectedProvider))
                .get();
        assertNotError(createMandateResponse);

        // we must authorize the mandate before revoking it
        ApiResponse<AuthorizationFlowResponse> startAuthorizationFlowResponse =
                startAuthFlowForMandate(createMandateResponse.getData().getId());
        assertNotError(startAuthorizationFlowResponse);

        // authorize the created mandate without explicit user interaction
        authorizeMandate(startAuthorizationFlowResponse.getData());

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
                .createMandate(createMandateRequest(SWEEPING, preselectedProvider))
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
                        .build())
                .build();

        ApiResponse<CreatePaymentResponse> createPaymentResponse =
                tlClient.payments().createPayment(createPaymentRequest).get();

        assertNotError(createPaymentResponse);
    }

    private CreateMandateRequest createMandateRequest(Mandate.Type type, ProviderSelection providerSelection) {
        Mandate mandate = null;
        if (type.equals(Mandate.Type.COMMERCIAL)) {
            mandate = Mandate.vrpCommercialMandate()
                    .providerSelection(providerSelection)
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
                .mandate(Mandate.vrpSweepingMandate()
                        .providerSelection(providerSelection)
                        .beneficiary(Beneficiary.externalAccount()
                                .accountIdentifier(AccountIdentifier.sortCodeAccountNumber()
                                        .accountNumber("10003957")
                                        .sortCode("140662")
                                        .build())
                                .accountHolderName("Andrea Java SDK")
                                .reference("a reference")
                                .build())
                        .build())
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
                                        .periodAlignment(CALENDAR)
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
}
