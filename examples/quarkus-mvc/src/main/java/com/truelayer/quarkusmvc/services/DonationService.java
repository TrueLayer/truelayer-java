package com.truelayer.quarkusmvc.services;

import com.truelayer.java.ITrueLayerClient;
import com.truelayer.java.entities.CurrencyCode;
import com.truelayer.java.entities.accountidentifier.IbanAccountIdentifier;
import com.truelayer.java.entities.accountidentifier.SortCodeAccountNumberAccountIdentifier;
import com.truelayer.java.entities.beneficiary.ExternalAccount;
import com.truelayer.java.payments.entities.User;
import com.truelayer.java.payments.entities.paymentmethod.BankTransfer;
import com.truelayer.java.payments.entities.paymentmethod.provider.UserSelectedProviderSelection;
import com.truelayer.quarkusmvc.models.DonationRequest;
import com.truelayer.quarkusmvc.models.DonationResult;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import javax.enterprise.context.ApplicationScoped;
import java.net.URI;

import static com.truelayer.java.payments.entities.CreatePaymentRequest.builder;


@ApplicationScoped
@RequiredArgsConstructor
public class DonationService implements IDonationService {

    private final ITrueLayerClient tlClient;

    @SneakyThrows
    @Override
    public DonationResult getDonationById(String id) {
        var payment = tlClient.payments().getPayment(id).get();

        if(payment.isError()){
            throw new RuntimeException(String.format("get payment by id error: %s", payment.getError()));
        }

        return DonationResult.builder()
                .id(payment.getData().getId())
                .status(payment.getData().getStatus().toString())
                .build();
    }

    @SneakyThrows
    @Override
    public URI createDonationLink(DonationRequest req) {
        var paymentRequest = builder()
                .amountInMinor(req.getAmount())
                .currency(CurrencyCode.GBP)
                .paymentMethod(BankTransfer.builder()
                        .providerSelection(UserSelectedProviderSelection.builder().build())
                        .beneficiary(ExternalAccount.builder()
                                .accountHolderName("A test donation account")
                                .accountIdentifier(SortCodeAccountNumberAccountIdentifier.builder()
                                        .accountNumber("11223344")
                                        .sortCode("012345")
                                        .build())
                                .reference("test donation")
                                .build())
                        .build())
                .user(User.builder()
                        .name(req.getName())
                        .email(req.getEmail())
                        .build())
                .build();

        var paymentResponse = tlClient.payments()
                .createPayment(paymentRequest)
                .get();

        if(paymentResponse.isError()){
            throw new RuntimeException(String.format("create payment error: %s", paymentResponse.getError()));
        }

        return tlClient.hpp().getHostedPaymentPageLink(paymentResponse.getData().getId(), paymentResponse.getData().getResourceToken(),
                URI.create("http://localhost:8080/callback"));
    }
}
