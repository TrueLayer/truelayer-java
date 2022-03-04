package com.truelayer.java.entities;

import com.truelayer.java.entities.accountidentifier.AccountIdentifier;
import java.util.List;
import lombok.Value;

@Value
public class PaymentSource {
    String id;

    List<AccountIdentifier> accountIdentifiers;

    String accountHolderName;
}
