package com.truelayer.java.merchantaccounts.entities;

import com.truelayer.java.entities.PaginationMetadata;
import com.truelayer.java.merchantaccounts.entities.transactions.Transaction;
import java.util.List;
import lombok.Value;

@Value
public class ListTransactionsResponse {

    List<Transaction> items;

    PaginationMetadata pagination;
}
