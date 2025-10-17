package com.truelayer.java.payouts.entities.beneficiary;

import com.truelayer.java.entities.User;
import com.truelayer.java.payouts.entities.beneficiary.providerselection.ProviderSelection;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString(callSuper = false)
@EqualsAndHashCode(callSuper = false)
public class UserDetermined extends Beneficiary {
    private final Type type = Type.USER_DETERMINED;

    private String reference;

    private User user;

    private Verification verification;

    private ProviderSelection providerSelection;
}
