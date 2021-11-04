package truelayer.java.payments.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include;

@Setter
@Getter
public class CreatePaymentRequest {

    @SerializedName("amount_in_minor")
    private int amountInMinor;

    @SerializedName("currency")
    private String currency;

    @SerializedName("payment_method")
    private PaymentMethod paymentMethod;

    @SerializedName("beneficiary")
    private PaymentBeneficiary beneficiary;

    @SerializedName("user")
    private PaymentUser user;

    @Setter
    @Getter
    public static class PaymentMethod {

        @SerializedName("type")
        private String type;

        public PaymentMethod(String type) {
            this.type = type;
        }
    }

    @Setter
    @Getter
    public static class PaymentBeneficiary {

        @SerializedName("type")
        private String type;

        @SerializedName("id")
        private String id;

        @JsonInclude(Include.NON_NULL)
        @SerializedName("name")
        private String name;

        public PaymentBeneficiary(String type, String id) {
            this.type = type;
            this.id = id;
        }
    }

    @Setter
    @Getter
    public static class PaymentUser {

        @SerializedName("type")
        private String type;

        @SerializedName("name")
        private String name;

        @SerializedName("email")
        @JsonInclude(Include.NON_NULL)
        private String email;

        @JsonInclude(Include.NON_NULL)
        @SerializedName("phone")
        private String phone;

        public PaymentUser(String type, String name) {
            this.type = type;
            this.name = name;
        }
    }
}
