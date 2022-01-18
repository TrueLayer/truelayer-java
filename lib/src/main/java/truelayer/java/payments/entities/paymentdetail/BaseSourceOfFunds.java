package truelayer.java.payments.entities.paymentdetail;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = ExternalAccount.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class BaseSourceOfFunds {

    public ExternalAccount asExternalAccount() {
        return (ExternalAccount) this;
    }
}
