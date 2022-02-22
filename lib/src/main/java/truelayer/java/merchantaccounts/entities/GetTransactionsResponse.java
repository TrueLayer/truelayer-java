package truelayer.java.merchantaccounts.entities;

import lombok.Value;
import truelayer.java.merchantaccounts.entities.transactions.Transaction;

import java.util.List;

@Value
public class GetTransactionsResponse {

    List<Transaction> items;
}
