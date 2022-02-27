package truelayer.java.entities;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import truelayer.java.entities.accountidentifier.AccountIdentifier;

@Builder
@Getter
@EqualsAndHashCode
@ToString
public class Remitter {
    private AccountIdentifier accountIdentifier;

    private String accountHolderName;
}
