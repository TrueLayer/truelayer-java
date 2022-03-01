package com.truelayer.java.merchantaccounts.entities;

import com.truelayer.java.merchantaccounts.entities.transactions.Transaction;
import java.util.List;
import lombok.Value;

@Value
public class GetTransactionsResponse {

    List<Transaction> items;
}
