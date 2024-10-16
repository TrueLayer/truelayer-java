package com.truelayer.java.payouts.entities;

import com.truelayer.java.entities.CurrencyCode;
import com.truelayer.java.payouts.entities.beneficiary.Beneficiary;
import com.truelayer.java.payouts.entities.schemeselection.SchemeSelection;
import com.truelayer.java.payouts.entities.submerchants.SubMerchants;
import java.util.Map;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
@EqualsAndHashCode
public class CreatePayoutRequest {
    private String merchantAccountId;
    private int amountInMinor;
    private CurrencyCode currency;
    private Beneficiary beneficiary;
    private SubMerchants subMerchants;
    private SchemeSelection schemeSelection;
    private Map<String, String> metadata;
}
