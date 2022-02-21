package truelayer.java.payments.entities.paymentmethod;

import static truelayer.java.payments.entities.paymentmethod.PaymentMethod.Type.BANK_TRANSFER;

import lombok.Builder;
import lombok.Getter;
import truelayer.java.entities.beneficiary.Beneficiary;
import truelayer.java.payments.entities.paymentmethod.provider.ProviderSelection;

@Builder
@Getter
public class BankTransfer extends PaymentMethod {
    private final Type type = BANK_TRANSFER;

    private ProviderSelection providerSelection;

    private Beneficiary beneficiary;
}
