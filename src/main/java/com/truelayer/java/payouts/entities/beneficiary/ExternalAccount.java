package com.truelayer.java.payouts.entities.beneficiary;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.truelayer.java.entities.Address;
import com.truelayer.java.payouts.entities.accountidentifier.AccountIdentifier;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ExternalAccount extends Beneficiary {
    private final Type type = Type.EXTERNAL_ACCOUNT;
    private String reference;
    private String accountHolderName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    private Address address;
    private AccountIdentifier accountIdentifier;
}
