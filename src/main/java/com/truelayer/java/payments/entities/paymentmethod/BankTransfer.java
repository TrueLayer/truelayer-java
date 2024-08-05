package com.truelayer.java.payments.entities.paymentmethod;

import static com.truelayer.java.payments.entities.paymentmethod.PaymentMethod.Type.BANK_TRANSFER;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.truelayer.java.entities.Retry;
import com.truelayer.java.payments.entities.beneficiary.Beneficiary;
import com.truelayer.java.payments.entities.providerselection.ProviderSelection;
import java.util.Optional;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@EqualsAndHashCode(callSuper = false)
public class BankTransfer extends PaymentMethod {
    private final Type type = BANK_TRANSFER;

    private ProviderSelection providerSelection;

    private Beneficiary beneficiary;

    private Retry retry;

    @JsonGetter
    public Optional<Retry> getRetry() {
        return Optional.ofNullable(retry);
    }
}
