package com.truelayer.java.mandates.entities.beneficiary;

import static com.truelayer.java.mandates.entities.beneficiary.Beneficiary.Type.EXTERNAL_ACCOUNT;

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
     * Deprecated since 2.7.0. See the <code>reference</code> field in Mandate's root object instead.
     * @see com.truelayer.java.mandates.entities.mandate.VRPSweepingMandate
     * @see com.truelayer.java.mandates.entities.mandate.VRPCommercialMandate
     */
    @Deprecated
    private String reference;
}
