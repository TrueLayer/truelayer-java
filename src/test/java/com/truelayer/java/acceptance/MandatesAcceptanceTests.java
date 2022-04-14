package com.truelayer.java.acceptance;

import static com.truelayer.java.TestUtils.*;
import static com.truelayer.java.mandates.entities.mandate.Constraints.PeriodicLimit.PeriodAlignment.CALENDAR;
import static com.truelayer.java.mandates.entities.mandate.Constraints.PeriodicLimit.PeriodType.MONTH;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.truelayer.java.entities.CurrencyCode;
import com.truelayer.java.entities.ProviderFilter;
import com.truelayer.java.entities.User;
import com.truelayer.java.entities.accountidentifier.AccountIdentifier;
import com.truelayer.java.entities.beneficiary.Beneficiary;
import com.truelayer.java.http.entities.ApiResponse;
import com.truelayer.java.mandates.entities.CreateMandateRequest;
import com.truelayer.java.mandates.entities.CreateMandateResponse;
import com.truelayer.java.mandates.entities.mandate.Constraints;
import com.truelayer.java.mandates.entities.mandate.Mandate;
import com.truelayer.java.mandates.entities.mandatedetail.MandateDetail;
import com.truelayer.java.payments.entities.*;
import java.net.URI;
import java.util.Collections;
import java.util.UUID;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;

@Tag("acceptance")
@Disabled
public class MandatesAcceptanceTests extends AcceptanceTests {

    public static final String RETURN_URI = "http://localhost:3000/callback";
    public static final String PROVIDER_ID = "ob-natwest-vrp-sandbox";

    @Test
    @DisplayName("It should create a mandate")
    @SneakyThrows
    public void itShouldCreateAMandate() {
        // create mandate
        ApiResponse<CreateMandateResponse> createMandateResponse =
                tlClient.mandates().createMandate(createMandateRequest()).get();

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
    @DisplayName("It should create and get a mandate by id")
    @SneakyThrows
    public void itShouldCreateAndGetAMandate() {
        // create mandate
        ApiResponse<CreateMandateResponse> createMandateResponse =
                tlClient.mandates().createMandate(createMandateRequest()).get();

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
        ApiResponse<CreateMandateResponse> createMandateResponse =
                tlClient.mandates().createMandate(createMandateRequest()).get();

        assertNotError(createMandateResponse);

        // revoke mandate by id
        ApiResponse<Void> revokeMandateResponse = tlClient.mandates()
                .revokeMandate(createMandateResponse.getData().getId())
                .get();

        // this is the best we can do as of now. there's no way of forcing an authorized state, which is the only that
        // can transition to the revoked state.
        assertTrue(revokeMandateResponse.isError()
                && revokeMandateResponse.getError().getTitle().equalsIgnoreCase("invalid state"));
    }

    private CreateMandateRequest createMandateRequest() {
        return CreateMandateRequest.builder()
                .mandate(Mandate.vrpSweepingMandate()
                        .providerFilter(ProviderFilter.builder()
                                .countries(Collections.singletonList(CountryCode.GB))
                                .releaseChannel(ReleaseChannel.PRIVATE_BETA)
                                .build())
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
                        .periodicLimits(Collections.singletonList(Constraints.PeriodicLimit.builder()
                                .periodAlignment(CALENDAR)
                                .periodType(MONTH)
                                .maximumAmount(2000)
                                .build()))
                        .maximumIndividualAmount(1000)
                        .build())
                .build();
    }
}
