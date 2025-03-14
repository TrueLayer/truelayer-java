package com.truelayer.java.entities.beneficiary;

import static com.truelayer.java.entities.beneficiary.Beneficiary.Type.PAYMENT_SOURCE;

import com.truelayer.java.merchantaccounts.entities.transactions.accountidentifier.AccountIdentifier;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = false)
public class PaymentSource extends Beneficiary {
    Type type = PAYMENT_SOURCE;

    String paymentSourceId;

    String userId;

    String reference;

    String accountHolderName;

    private List<AccountIdentifier> accountIdentifiers;
}
