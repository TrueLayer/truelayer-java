package truelayer.java.payments.entities.paymentdetail;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = ExternalAccount.class)
public abstract class SourceOfFunds {

    public ExternalAccount asExternalAccount() {
        return (ExternalAccount) this;
    }
}
