package truelayer.java.merchantaccounts.entities;

import java.util.List;
import lombok.Value;
import truelayer.java.merchantaccounts.entities.paymentsources.PaymentSource;

@Value
public class GetPaymentSourcesResponse {

    List<PaymentSource> items;
}
