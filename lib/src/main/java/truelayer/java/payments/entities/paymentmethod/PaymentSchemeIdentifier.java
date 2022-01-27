package truelayer.java.payments.entities.paymentmethod;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@JsonDeserialize(as = BankTransfer.class)
@ToString
@EqualsAndHashCode
@Getter
public abstract class PaymentSchemeIdentifier {

    protected Type type;

    @RequiredArgsConstructor
    @Getter
    public enum Type {
        SORT_CODE_ACCOUNT_NUMBER("sort_code_account_number");

        @JsonValue
        private final String type;
    }
}
