package com.truelayer.java.payouts.entities.beneficiary;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.truelayer.java.entities.CurrencyCode;
import java.time.LocalDate;
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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonDeserialize(using = LocalDateFlexibleDeserializer.class)
    private LocalDate createdAt;
}
