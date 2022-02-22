package truelayer.java.merchantaccounts.entities;

import java.util.List;
import lombok.Value;
import truelayer.java.merchantaccounts.entities.transactions.Transaction;

@Value
public class GetTransactionsResponse {

    List<Transaction> items;
}
