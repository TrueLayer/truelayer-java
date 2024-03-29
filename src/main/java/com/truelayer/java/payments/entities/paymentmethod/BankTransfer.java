package com.truelayer.java.payments.entities.paymentmethod;

import static com.truelayer.java.payments.entities.paymentmethod.PaymentMethod.Type.BANK_TRANSFER;

import com.truelayer.java.payments.entities.beneficiary.Beneficiary;
import com.truelayer.java.payments.entities.providerselection.ProviderSelection;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class BankTransfer extends PaymentMethod {
    private final Type type = BANK_TRANSFER;

    private ProviderSelection providerSelection;

    private Beneficiary beneficiary;
}
