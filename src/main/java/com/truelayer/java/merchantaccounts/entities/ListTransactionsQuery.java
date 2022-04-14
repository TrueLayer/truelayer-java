package com.truelayer.java.merchantaccounts.entities;

import com.truelayer.java.merchantaccounts.entities.transactions.TransactionTypeQuery;
import java.util.Date;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;

@Builder
@Getter
@Accessors(fluent = true)
public class ListTransactionsQuery {
    private Date from;

    private Date to;

    private TransactionTypeQuery type;
}
