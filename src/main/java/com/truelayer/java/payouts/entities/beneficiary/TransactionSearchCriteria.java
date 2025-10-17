package com.truelayer.java.payouts.entities.beneficiary;

import com.truelayer.java.entities.CurrencyCode;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
@EqualsAndHashCode
public class TransactionSearchCriteria {
    private List<String> tokens;

    private Integer amountInMinor;

    private CurrencyCode currency;

    private ZonedDateTime createdAt;
}
