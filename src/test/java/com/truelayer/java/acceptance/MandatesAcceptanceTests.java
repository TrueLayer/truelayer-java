package com.truelayer.java.acceptance;

import static com.truelayer.java.TestUtils.*;
import static com.truelayer.java.mandates.entities.Constraints.PeriodicLimits.Limit.PeriodAlignment.CALENDAR;
import static com.truelayer.java.mandates.entities.mandate.Mandate.Type.COMMERCIAL;
import static com.truelayer.java.mandates.entities.mandate.Mandate.Type.SWEEPING;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.truelayer.java.entities.CurrencyCode;
import com.truelayer.java.entities.User;
import com.truelayer.java.entities.accountidentifier.AccountIdentifier;
import com.truelayer.java.entities.beneficiary.Beneficiary;
import com.truelayer.java.entities.providerselection.ProviderSelection;
import com.truelayer.java.http.entities.ApiResponse;
import com.truelayer.java.mandates.entities.Constraints;
import com.truelayer.java.mandates.entities.Constraints.PeriodicLimits;
import com.truelayer.java.mandates.entities.Constraints.PeriodicLimits.Limit;
import com.truelayer.java.mandates.entities.CreateMandateRequest;
import com.truelayer.java.mandates.entities.CreateMandateResponse;
import com.truelayer.java.mandates.entities.ListMandatesResponse;
import com.truelayer.java.mandates.entities.mandate.Mandate;
import com.truelayer.java.mandates.entities.mandatedetail.MandateDetail;
import com.truelayer.java.payments.entities.*;
import com.truelayer.java.payments.entities.paymentmethod.PaymentMethod;
import java.net.URI;
import java.util.UUID;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;

@Tag("acceptance")
public class MandatesAcceptanceTests extends AcceptanceTests {

    public static final String RETURN_URI = "http://localhost:3000/callback";
    public static final String PROVIDER_ID = "ob-uki-mock-bank";

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
    }

    @Test
    @DisplayName("It should create a mandate with user_selected provider")
    @SneakyThrows
    public void itShouldCreateAMandateWithUserSelectedProvider() {
        // create mandate
        ProviderSelection userSelectedProvider =
                ProviderSelection.userSelected().build();
        ApiResponse<CreateMandateResponse> createMandateResponse = tlClient.mandates()
                .createMandate(createMandateRequest(SWEEPING, userSelectedProvider))
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
    @DisplayName("It should create and get a mandate by id")
    @SneakyThrows
    public void itShouldCreateAndGetAMandate() {
        // create mandate
        ProviderSelection preselectedProvider =
                ProviderSelection.preselected().providerId(PROVIDER_ID).build();
        ApiResponse<CreateMandateResponse> createMandateResponse = tlClient.mandates()
                .createMandate(createMandateRequest(SWEEPING, preselectedProvider))
                .get();

        assertNotError(createMandateResponse);

        // get mandate by id
        ApiResponse<MandateDetail> getMandateResponse = tlClient.mandates()
                .getMandate(createMandateResponse.getData().getId())
                .get();

        assertNotError(getMandateResponse);
    }

    @Test
    @DisplayName("It should create and revoke a mandate and get an invalid state validation error")
    @SneakyThrows
    public void itShouldCreateAndRevokeAMandate() {
        // create mandate
        ProviderSelection preselectedProvider =
                ProviderSelection.preselected().providerId(PROVIDER_ID).build();
        ApiResponse<CreateMandateResponse> createMandateResponse = tlClient.mandates()
                .createMandate(createMandateRequest(SWEEPING, preselectedProvider))
                .get();

        assertNotError(createMandateResponse);

        // revoke mandate by id
        ApiResponse<Void> revokeMandateResponse = tlClient.mandates()
                .revokeMandate(createMandateResponse.getData().getId())
                .get();

        // this is the best we can do as of now. there's no way of forcing an authorized state, which is the only that
        // can transition to the revoked state.
        // todo: improve this with a mock provider
        assertTrue(revokeMandateResponse.isError()
                && revokeMandateResponse.getError().getTitle().equalsIgnoreCase("invalid state"));
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

        // get mandate by id
        ApiResponse<MandateDetail> getMandateResponse = tlClient.mandates()
                .getMandate(createMandateResponse.getData().getId())
                .get();

        assertNotError(getMandateResponse);

        // create a payment on mandate
        CreatePaymentRequest createPaymentRequest = CreatePaymentRequest.builder()
                .amountInMinor(getMandateResponse.getData().getConstraints().getMaximumIndividualAmount())
                .currency(CurrencyCode.EUR) // todo: validate this with the team
                .paymentMethod(PaymentMethod.mandate()
                        .mandateId(getMandateResponse.getData().getId())
                        .build())
                .build();

        ApiResponse<CreatePaymentResponse> createPaymentResponse =
                tlClient.payments().createPayment(createPaymentRequest).get();

        // this is the best we can do as of now. there's no way of forcing an authorized state, which is required for
        // creating
        // a payment
        // todo: improve this with a mock provider
        assertTrue(createPaymentResponse.isError()
                && createPaymentResponse
                        .getError()
                        .getErrors()
                        .toString()
                        .contains("Mandate must be in Authorized state"));
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
                            .reference("a reference")
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
                            .reference("a reference")
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
                .build();
    }
}
