package truelayer.java.payments.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("amount_in_minor")
    private int amountInMinor;

    @JsonProperty("currency")
    private String currency;

    @JsonProperty("payment_method")
    private BasePaymentMethod paymentMethod;

    @JsonProperty("beneficiary")
    private BaseBeneficiary beneficiary;

    @JsonProperty("user")
    private User user;

    @Builder
    @Getter
    @ToString
    @EqualsAndHashCode
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class User {
        @JsonProperty("id")
        private String id;

        @JsonProperty("type")
        private User.Type type;

        @JsonProperty("name")
        private String name;

        @JsonProperty("email")
        private String email;

        @JsonProperty("phone")
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
