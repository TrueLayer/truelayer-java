package truelayer.java.payments.entities;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.Optional;

// Unlike beneficiaries, this union currently comes with one
// type only, that is BankTransfer to which Jackson will deserialize
// every BasePaymentMethod instance
@JsonDeserialize(as = BankTransfer.class)
public abstract class BasePaymentMethod {
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
