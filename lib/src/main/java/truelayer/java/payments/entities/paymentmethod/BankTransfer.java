package truelayer.java.payments.entities.paymentmethod;

import static truelayer.java.payments.entities.paymentmethod.PaymentMethod.Type.BANK_TRANSFER;

import lombok.Builder;
import lombok.Getter;
import truelayer.java.payments.entities.paymentmethod.provider.Provider;

@Builder
@Getter
public class BankTransfer extends PaymentMethod {
    private final Type type = BANK_TRANSFER;

    private Provider provider;
}
