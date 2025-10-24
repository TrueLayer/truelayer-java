package com.truelayer.java.payouts.entities.beneficiary;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
@EqualsAndHashCode
public class Verification {
    private Boolean verifyName;

    private TransactionSearchCriteria transactionSearchCriteria;
}
