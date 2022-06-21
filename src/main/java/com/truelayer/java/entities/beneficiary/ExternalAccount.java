package com.truelayer.java.entities.beneficiary;

import static com.truelayer.java.entities.beneficiary.Beneficiary.Type.EXTERNAL_ACCOUNT;

import com.truelayer.java.entities.accountidentifier.AccountIdentifier;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ExternalAccount extends Beneficiary {
    private final Type type = EXTERNAL_ACCOUNT;

    private String accountHolderName;

    private AccountIdentifier accountIdentifier;

    /**
     * Reference for the payments.
     * Ignored when creating a Recurring payment mandate
     */
    private String reference;
}
