package truelayer.java.payments.entities.paymentmethod;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Optional;

// Unlike beneficiaries, this union currently comes with one
// type only, that is BankTransfer to which Jackson will deserialize
// every BasePaymentMethod instance
@JsonDeserialize(as = BankTransfer.class)
@ToString
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class BasePaymentMethod {
    @JsonIgnore
    public boolean isBankTransfer(){
        return this instanceof BankTransfer;
    }

    public Optional<BankTransfer> bankTransfer(){
        if(isBankTransfer()){
            return Optional.of((BankTransfer) this);
        }
        return Optional.empty();
    }
}
