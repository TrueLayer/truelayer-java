package truelayer.java.payments.entities.beneficiary;

import static truelayer.java.payments.entities.beneficiary.Beneficiary.Type.EXTERNAL_ACCOUNT;

import lombok.Builder;
import lombok.Getter;
import truelayer.java.payments.entities.paymentmethod.AccountIdentifier;

@Builder
@Getter
public class ExternalAccount extends Beneficiary {
    private final Type type = EXTERNAL_ACCOUNT;

    private String accountHolderName;

    private AccountIdentifier accountIdentifier;

    private String reference;
}
