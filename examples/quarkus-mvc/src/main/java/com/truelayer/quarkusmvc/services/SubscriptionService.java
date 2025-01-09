package com.truelayer.quarkusmvc.services;

import com.truelayer.java.ITrueLayerClient;
import com.truelayer.java.entities.CurrencyCode;
import com.truelayer.java.entities.User;
import com.truelayer.java.entities.accountidentifier.AccountIdentifier;
import com.truelayer.java.http.entities.ApiResponse;
import com.truelayer.java.mandates.entities.Constraints;
import com.truelayer.java.mandates.entities.Constraints.PeriodicLimits.Limit;
import com.truelayer.java.mandates.entities.CreateMandateRequest;
import com.truelayer.java.mandates.entities.beneficiary.Beneficiary;
import com.truelayer.java.mandates.entities.mandate.Mandate;
import com.truelayer.java.mandates.entities.mandatedetail.MandateDetail;
import com.truelayer.java.payments.entities.*;
import com.truelayer.java.payments.entities.providerselection.ProviderSelection;
import com.truelayer.quarkusmvc.models.SubscriptionRequest;
import com.truelayer.quarkusmvc.models.SubscriptionResult;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.net.URI;
import java.util.UUID;

import static com.truelayer.java.mandates.entities.Constraints.PeriodicLimits.Limit.PeriodAlignment.CALENDAR;

@ApplicationScoped
@RequiredArgsConstructor
public class SubscriptionService implements ISubscriptionService{

    public static final String PROVIDER_ID = "ob-uki-mock-bank";

    private final ITrueLayerClient trueLayerClient;

    @SneakyThrows
    @Override
    public URI createSubscriptionLink(SubscriptionRequest req) {
        // create mandate
        var createMandateReq = CreateMandateRequest.builder()
                .mandate(Mandate.vrpSweepingMandate()
                        .providerSelection(ProviderSelection.preselected()
                                .providerId(PROVIDER_ID)
                                .build())
                        .beneficiary(Beneficiary.externalAccount()
                                .accountIdentifier(AccountIdentifier.sortCodeAccountNumber()
                                        .accountNumber("10003957")
                                        .sortCode("140662")
                                        .build())
                                .accountHolderName("Andrea Java SDK")
                                .build())
                        .build())
                .currency(CurrencyCode.GBP)
                .user(User.builder()
                        .id(UUID.randomUUID().toString())
                        .name("John Smith")
                        .email("john@truelayer.com")
                        .build())
                .constraints(Constraints.builder()
                        .periodicLimits(Constraints.PeriodicLimits.builder()
                                .month(Limit.builder()
                                        .maximumAmount(2000)
                                        .periodAlignment(CALENDAR)
                                        .build())
                                .build())
                        .maximumIndividualAmount(1000)
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

        ApiResponse<AuthorizationFlowResponse> startAuthorizationFlowResponse = trueLayerClient.mandates()
                .startAuthorizationFlow(mandate.getData().getId(), startAuthorizationFlowRequest)
                .get();

        if(startAuthorizationFlowResponse.isError()){
            throw new RuntimeException(String.format("start auth flow error: %s",
                    startAuthorizationFlowResponse.getError()));
        }

        if(startAuthorizationFlowResponse.getData().isAuthorizationFailed()){
            throw new RuntimeException(String.format("start auth flow failed: %s",
                    startAuthorizationFlowResponse.getData().asAuthorizationFailed().getFailureReason()));
        }

        return startAuthorizationFlowResponse.getData()
                .asAuthorizing()
                .getAuthorizationFlow()
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
