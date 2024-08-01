package com.truelayer.java.payments.entities.paymentmethod;

import static com.truelayer.java.payments.entities.paymentmethod.PaymentMethod.Type.MANDATE;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.truelayer.java.payments.entities.retry.Retry;
import java.util.Optional;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@EqualsAndHashCode(callSuper = false)
public class Mandate extends PaymentMethod {
    private final Type type = MANDATE;

    private String mandateId;

    /**
     * Optional reference used for reconciliation, considered only if beneficiary is of type external_account.
     * We recommend one that is 18 alphanumeric characters or shorter to ensure banks don't reject the payment.
     */
    private String reference;

    private Retry retry;

    @JsonGetter
    private Optional<String> getReference() {
        return Optional.ofNullable(reference);
    }
}
