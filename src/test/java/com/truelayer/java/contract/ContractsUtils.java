package com.truelayer.java.contract;

import com.truelayer.java.entities.CurrencyCode;
import com.truelayer.java.entities.ProviderFilter;
import com.truelayer.java.entities.User;
import com.truelayer.java.entities.beneficiary.Beneficiary;
import com.truelayer.java.entities.providerselection.ProviderSelection;
import com.truelayer.java.payments.entities.*;
import com.truelayer.java.payments.entities.paymentmethod.PaymentMethod;
import lombok.SneakyThrows;

import java.net.URI;
import java.util.Collections;

import static com.truelayer.java.Utils.getObjectMapper;
import static com.truelayer.java.contract.Constant.*;

public class ContractsUtils {

    public static CreatePaymentRequest buildCreatePaymentRequest() {
        return buildCreatePaymentRequest(ProviderSelection.Type.USER_SELECTED);
    }

    public static CreatePaymentRequest buildCreatePaymentRequest(ProviderSelection.Type providerSelectionType) {

        ProviderSelection providerSelection = buildProviderSelection(providerSelectionType);
        return CreatePaymentRequest.builder()
                .amountInMinor(50)
                .currency(CurrencyCode.GBP)
                .paymentMethod(PaymentMethod.bankTransfer()
                        .providerSelection(providerSelection)
                        .beneficiary(Beneficiary.merchantAccount()
                                .merchantAccountId(MERCHANT_ACCOUNT_ID)
                                .build())
                        .build())
                .user(User.builder()
                        .name("Contract test user")
                        .email("contract-test@truelayer.com")
                        .build())
                .build();
    }

    private static ProviderSelection buildProviderSelection(ProviderSelection.Type providerSelectionType){
        switch (providerSelectionType) {
            case USER_SELECTED:
                return ProviderSelection.userSelected()
                        .filter(ProviderFilter.builder()
                                .countries(Collections.singletonList(CountryCode.GB))
                                .releaseChannel(ReleaseChannel.GENERAL_AVAILABILITY)
                                .customerSegments(Collections.singletonList(CustomerSegment.RETAIL))
                                .providerIds(Collections.singletonList(PROVIDER_ID_GB))
                                .build())
                        .build();
            case PRESELECTED:
                return ProviderSelection.preselected()
                        .providerId(PROVIDER_ID_GB)
                        .schemeId(SchemeId.FASTER_PAYMENTS_SERVICE)
                        .build();
            default:
                throw new IllegalStateException("Unexpected value: " + providerSelectionType);
        }
    }

    public static StartAuthorizationFlowRequest buildStartAuthFlowRequest() {
        return StartAuthorizationFlowRequest.builder()
                .redirect(StartAuthorizationFlowRequest.Redirect.builder()
                        .returnUri(URI.create(RETURN_URI))
                        .build())
                .withProviderSelection()
                .build();
    }

    public static SubmitProviderSelectionRequest buildProviderSelectionRequest() {
        return SubmitProviderSelectionRequest.builder()
                .providerId(PROVIDER_ID_GB)
                .build();
    }

    @SneakyThrows
    public static String SerializeContractBody(Object body) {
        return getObjectMapper().writeValueAsString(body);
    }

}
