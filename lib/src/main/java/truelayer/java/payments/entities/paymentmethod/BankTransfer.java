package truelayer.java.payments.entities.paymentmethod;

import lombok.Builder;
import lombok.Getter;
import truelayer.java.payments.entities.paymentmethod.provider.Provider;

@Builder
@Getter
public class BankTransfer extends PaymentMethod {
    private final String type = "bank_transfer";

    private Provider provider;
}
