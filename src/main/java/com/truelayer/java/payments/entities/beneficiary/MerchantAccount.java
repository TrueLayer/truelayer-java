package com.truelayer.java.payments.entities.beneficiary;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@EqualsAndHashCode(callSuper = false)
public class MerchantAccount extends Beneficiary {
    private final Type type = Type.MERCHANT_ACCOUNT;

    private String merchantAccountId;

    private String accountHolderName;

    private String reference;
}
