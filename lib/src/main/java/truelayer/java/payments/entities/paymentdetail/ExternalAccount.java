package truelayer.java.payments.entities.paymentdetail;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Value;
import truelayer.java.payments.entities.paymentdetail.schemeidentifier.SchemeIdentifier;

@Value
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExternalAccount extends SourceOfFunds {
    String type = "external_account";

    List<SchemeIdentifier> schemeIdentifiers;

    String externalAccountId;

    String accountHolderName;
}
