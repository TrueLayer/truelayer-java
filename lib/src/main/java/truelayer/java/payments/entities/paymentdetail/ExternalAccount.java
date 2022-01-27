package truelayer.java.payments.entities.paymentdetail;

import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Value;
import truelayer.java.payments.entities.paymentdetail.schemeidentifier.SchemeIdentifier;

@Value
@EqualsAndHashCode(callSuper = false)
public class ExternalAccount extends SourceOfFunds {
    String type = "external_account";

    List<SchemeIdentifier> schemeIdentifiers;

    String externalAccountId;

    String accountHolderName;
}
