package truelayer.java.payments.entities.paymentmethod;

import lombok.Builder;
import lombok.Getter;
import truelayer.java.payments.entities.paymentmethod.provider.BaseProvider;

@Builder
@Getter
public class BankTransfer extends BasePaymentMethod {
    private final String type = "bank_transfer";

    private BaseProvider provider;
}
