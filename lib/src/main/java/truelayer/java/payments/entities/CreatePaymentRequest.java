package truelayer.java.payments.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import truelayer.java.payments.entities.beneficiary.BaseBeneficiary;
import truelayer.java.payments.entities.paymentmethod.BasePaymentMethod;

@Builder
@ToString
@EqualsAndHashCode
@JsonInclude(Include.NON_NULL)
public class CreatePaymentRequest {
    private int amountInMinor;

    private String currency;

    private BasePaymentMethod paymentMethod;

    private BaseBeneficiary beneficiary;

    private User user;

    @Builder
    @Getter
    @ToString
    @EqualsAndHashCode
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class User {
        private String id;

        private User.Type type;

        private String name;

        private String email;

        private String phone;

        public enum Type {
            EXISTING("existing"),
            NEW("new");

            private final String type;

            Type(String type) {
                this.type = type;
            }

            @JsonValue
            public String getType() {
                return type;
            }
        }
    }
}
