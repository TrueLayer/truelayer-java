package truelayer.java.payments.entities.paymentmethod;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

// Unlike beneficiaries, this union currently comes with one
// type only, that is BankTransfer to which Jackson will deserialize
// every BasePaymentMethod instance
@JsonDeserialize(as = BankTransfer.class)
@ToString
@EqualsAndHashCode
@Getter
public abstract class PaymentMethod {

    protected Type type;

    public BankTransfer asBankTransfer() {
        return (BankTransfer) this;
    }

    @RequiredArgsConstructor
    @Getter
    public enum Type {
        BANK_TRANSFER("bank_transfer");

        @JsonValue
        private final String type;
    }
}
