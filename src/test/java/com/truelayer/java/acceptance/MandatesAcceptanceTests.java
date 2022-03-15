package com.truelayer.java.acceptance;

import static com.truelayer.java.TestUtils.*;

import com.truelayer.java.entities.CurrencyCode;
import com.truelayer.java.entities.ProviderFilter;
import com.truelayer.java.entities.User;
import com.truelayer.java.entities.accountidentifier.AccountIdentifier;
import com.truelayer.java.entities.beneficiary.Beneficiary;
import com.truelayer.java.http.entities.ApiResponse;
import com.truelayer.java.payments.entities.*;
import com.truelayer.java.recurringpayments.entities.CreateMandateRequest;
import com.truelayer.java.recurringpayments.entities.CreateMandateResponse;
import com.truelayer.java.recurringpayments.entities.mandate.Constraints;
import com.truelayer.java.recurringpayments.entities.mandate.Mandate;
import java.net.URI;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.UUID;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("acceptance")
public class MandatesAcceptanceTests extends AcceptanceTests {

    public static final URI A_REDIRECT_URI = URI.create("https://48941d32f8bd7826841d8224f9390525.m.pipedream.net");
    public static final String PROVIDER_ID = "ob-natwest-vrp-sandbox";

    @Test
    @DisplayName("It should create a mandate")
    @SneakyThrows
    public void itShouldCreateAMandate() {
        // create mandate
        CreateMandateRequest createMandateRequest = CreateMandateRequest.builder()
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
                        .validFrom(ZonedDateTime.now().plusDays(1).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME))
                        .validTo(ZonedDateTime.now().plusDays(25).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME))
                        .maximumIndividualAmount(100)
                        .build())
                .build();

        ApiResponse<CreateMandateResponse> createMandateResponse =
                tlClient.mandates().createMandate(createMandateRequest).get();

        assertNotError(createMandateResponse);

        // start auth flow
        StartAuthorizationFlowRequest startAuthorizationFlowRequest = StartAuthorizationFlowRequest.builder()
                .withProviderSelection()
                .redirect(StartAuthorizationFlowRequest.Redirect.builder()
                        .returnUri(A_REDIRECT_URI)
                        .build())
                .build();

        ApiResponse<StartAuthorizationFlowResponse> startAuthorizationFlowResponse = tlClient.mandates()
                .startAuthorizationFlow(createMandateResponse.getData().getId(), startAuthorizationFlowRequest)
                .get();

        assertNotError(startAuthorizationFlowResponse);

        // submit provider selection
        SubmitProviderSelectionRequest submitProviderSelectionRequest =
                SubmitProviderSelectionRequest.builder().providerId(PROVIDER_ID).build();

        ApiResponse<SubmitProviderSelectionResponse> submitProviderSelectionResponse = tlClient.mandates()
                .submitProviderSelection(createMandateResponse.getData().getId(), submitProviderSelectionRequest)
                .get();

        assertNotError(submitProviderSelectionResponse);
    }
}
