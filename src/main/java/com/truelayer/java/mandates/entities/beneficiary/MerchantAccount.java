package com.truelayer.java.mandates.entities.beneficiary;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MerchantAccount extends Beneficiary {
    private final Type type = Type.MERCHANT_ACCOUNT;

    private String merchantAccountId;

    private String accountHolderName;

    /**
     * Deprecated since 2.7.0. See the <code>reference</code> field in Mandate's root object instead.
     * @see com.truelayer.java.mandates.entities.mandate.VRPSweepingMandate
     * @see com.truelayer.java.mandates.entities.mandate.VRPCommercialMandate
     */
    private String reference;
}
