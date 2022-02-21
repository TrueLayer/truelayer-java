package truelayer.java.entities.beneficiary;

import static truelayer.java.entities.beneficiary.Beneficiary.Type.EXTERNAL_ACCOUNT;

import lombok.Builder;
import lombok.Getter;
import truelayer.java.entities.accountidentifier.AccountIdentifier;

@Builder
@Getter
public class ExternalAccount extends Beneficiary {
    private final Type type = EXTERNAL_ACCOUNT;

    private String accountHolderName;

    private AccountIdentifier accountIdentifier;

    private String reference;
}
