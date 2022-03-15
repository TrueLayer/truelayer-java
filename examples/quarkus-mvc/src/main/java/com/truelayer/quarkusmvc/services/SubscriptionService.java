package com.truelayer.quarkusmvc.services;

import com.truelayer.java.ITrueLayerClient;
import com.truelayer.java.entities.CurrencyCode;
import com.truelayer.java.entities.ProviderFilter;
import com.truelayer.java.entities.User;
import com.truelayer.java.entities.accountidentifier.AccountIdentifier;
import com.truelayer.java.entities.beneficiary.Beneficiary;
import com.truelayer.java.http.entities.ApiResponse;
import com.truelayer.java.payments.entities.*;
import com.truelayer.java.recurringpayments.entities.CreateMandateRequest;
import com.truelayer.java.recurringpayments.entities.mandate.Constraints;
import com.truelayer.java.recurringpayments.entities.mandate.Mandate;
import com.truelayer.java.recurringpayments.entities.mandatedetail.MandateDetail;
import com.truelayer.quarkusmvc.models.SubscriptionRequest;
import com.truelayer.quarkusmvc.models.SubscriptionResult;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.jboss.resteasy.spi.NotImplementedYetException;

import javax.enterprise.context.ApplicationScoped;
import java.net.URI;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static com.truelayer.java.recurringpayments.entities.mandate.Constraints.PeriodicLimit.PeriodAlignment.CALENDAR;
import static java.util.Calendar.MONTH;

@ApplicationScoped
@RequiredArgsConstructor
public class SubscriptionService implements ISubscriptionService{

    private final ITrueLayerClient trueLayerClient;

    @SneakyThrows
    @Override
    public URI createSubscriptionLink(SubscriptionRequest req) {
        // create mandate
        var createMandateReq = CreateMandateRequest.builder()
                .mandate(Mandate.vrpSweepingMandate()
                        .providerFilter(ProviderFilter.builder()
                                .countries(Collections.singletonList(CountryCode.GB))
                                .releaseChannel(ReleaseChannel.PRIVATE_BETA)
                                .build())
                        .beneficiary(Beneficiary.externalAccount()
                                .accountIdentifier(AccountIdentifier.sortCodeAccountNumber()
                                        //replace below
                                        .accountNumber("10003957")
                                        .sortCode("140662")
                                        .build())
                                .accountHolderName("Andrei Sorbun")
                                .reference("a reference")
                                .build())
                        .build())
                .currency(CurrencyCode.GBP)
                .user(User.builder()
                        .id(UUID.randomUUID().toString())
                        .name("Andrea Di Lisio")
                        .email("andrea.dilisio@truelayer.com")
                        .build())
                .constraints(Constraints.builder()
                        .maximumIndividualAmount(req.getAmount())
                        .periodicLimits(Collections.singletonList(Constraints.PeriodicLimit.builder()
                                .periodAlignment(CALENDAR)
                                .periodType(Constraints.PeriodicLimit.PeriodType.MONTH)
                                .maximumAmount(req.getAmount())
                                .build()))
                        .build())
                .build();
        var mandate = trueLayerClient.mandates().createMandate(createMandateReq).get();

        if(mandate.isError()){
            throw new RuntimeException(String.format("create mandate error: %s", mandate.getError()));
        }

        // start auth flow
        StartAuthorizationFlowRequest startAuthorizationFlowRequest = StartAuthorizationFlowRequest.builder()
                .withProviderSelection()
                .redirect(StartAuthorizationFlowRequest.Redirect.builder()
                        .returnUri(URI.create("http://localhost:8080/subscriptions/callback"))
                        .build())
                .build();
        ApiResponse<StartAuthorizationFlowResponse> startAuthorizationFlowResponse = trueLayerClient.mandates()
                .startAuthorizationFlow(mandate.getData().getId(), startAuthorizationFlowRequest)
                .get();

        if(startAuthorizationFlowResponse.isError()){
            throw new RuntimeException(String.format("start auth flow error: %s",
                    startAuthorizationFlowResponse.getError()));
        }

        var providerId = startAuthorizationFlowResponse.getData().getAuthorizationFlow()
                .getActions().getNext().asProviderSelection().getProviders().stream()
                .findFirst()
                .orElseThrow().getProviderId();

        // submit provider selection
        SubmitProviderSelectionRequest submitProviderSelectionRequest =
                SubmitProviderSelectionRequest.builder().providerId(providerId).build();

        ApiResponse<SubmitProviderSelectionResponse> submitProviderSelectionResponse = trueLayerClient.mandates()
                .submitProviderSelection(mandate.getData().getId(), submitProviderSelectionRequest)
                .get();

        if(submitProviderSelectionResponse.isError()){
            throw new RuntimeException(String.format("submit provider selection error: %s",
                    submitProviderSelectionResponse.getError()));
        }

        return submitProviderSelectionResponse.getData().getAuthorizationFlow()
                .getActions().getNext().asRedirect().getUri();
    }

    @SneakyThrows
    @Override
    public SubscriptionResult getSubscriptionById(String id) {
        ApiResponse<MandateDetail> getMandateById = trueLayerClient.mandates().getMandate(id).get();

        return SubscriptionResult.builder()
                .status(getMandateById.getData().getStatus().getStatus())
                .id(getMandateById.getData().getId())
                .build();
    }
}
