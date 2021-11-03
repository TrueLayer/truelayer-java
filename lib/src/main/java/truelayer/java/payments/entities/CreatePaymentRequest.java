package truelayer.java.payments.entities;

import com.google.gson.annotations.SerializedName;

public class CreatePaymentRequest {

    @SerializedName("amount_in_minor")
    private int amountInMinor;

    @SerializedName("currency")
    private String currency;

    @SerializedName("payment_method")
    private PaymentMethod paymentMethod;


    static class PaymentMethod {

        @SerializedName("type")
        private String type;

        public PaymentMethod(String type) {
            this.type = type;
        }
    }
}
