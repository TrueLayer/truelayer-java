package com.truelayer.java.entities;

import com.truelayer.java.entities.accountidentifier.AccountIdentifier;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@EqualsAndHashCode
@ToString
public class Remitter {
    private AccountIdentifier accountIdentifier;

    private String accountHolderName;
}
