package com.truelayer.java.merchantaccounts.entities;

import com.truelayer.java.merchantaccounts.entities.transactions.TransactionTypeQuery;
import java.time.ZonedDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;

@Builder
@Getter
@Accessors(fluent = true)
public class ListTransactionsQuery {
    private ZonedDateTime from;

    private ZonedDateTime to;

    private TransactionTypeQuery type;
}
