package truelayer.java.payments.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include;

@Setter
@Getter
public class CreatePaymentRequest {

    @JsonProperty("amount_in_minor")
    private int amountInMinor;

    @JsonProperty("currency")
    private String currency;

    @JsonProperty("payment_method")
    private PaymentMethod paymentMethod;

    @JsonProperty("beneficiary")
    private PaymentBeneficiary beneficiary;

    @JsonProperty("user")
    private PaymentUser user;

    @Setter
    @Getter
    public static class PaymentMethod {

        @JsonProperty("type")
        private String type;

        public PaymentMethod(String type) {
            this.type = type;
        }
    }

    @Setter
    @Getter
    public static class PaymentBeneficiary {

        @JsonProperty("type")
        private String type;

        @JsonProperty("id")
        private String id;

        @JsonInclude(Include.NON_NULL)
        @JsonProperty("name")
        private String name;

        public PaymentBeneficiary(String type, String id) {
            this.type = type;
            this.id = id;
        }
    }

    @Setter
    @Getter
    public static class PaymentUser {

        @JsonProperty("type")
        private String type;

        @JsonProperty("name")
        private String name;

        @JsonProperty("email")
        @JsonInclude(Include.NON_NULL)
        private String email;

        @JsonInclude(Include.NON_NULL)
        @JsonProperty("phone")
        private String phone;

        public PaymentUser(String type, String name) {
            this.type = type;
            this.name = name;
        }
    }
}
