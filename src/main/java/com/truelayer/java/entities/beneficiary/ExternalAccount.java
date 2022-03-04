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

    private String reference;
}
