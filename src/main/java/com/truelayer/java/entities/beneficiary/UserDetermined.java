package com.truelayer.java.entities.beneficiary;

import static com.truelayer.java.entities.beneficiary.Beneficiary.Type.USER_DETERMINED;

import com.truelayer.java.merchantaccounts.entities.transactions.accountidentifier.AccountIdentifier;
import com.truelayer.java.payouts.entities.PayoutUser;
import com.truelayer.java.payouts.entities.beneficiary.Verification;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = false)
public class UserDetermined extends Beneficiary {
    Type type = USER_DETERMINED;

    String reference;

    String accountHolderName;

    List<AccountIdentifier> accountIdentifiers;

    PayoutUser user;

    Verification verification;
}
