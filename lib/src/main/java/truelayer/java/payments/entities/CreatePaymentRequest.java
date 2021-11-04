package truelayer.java.payments.entities;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;

@Builder
public class CreatePaymentRequest {

    @SerializedName("amount_in_minor")
    private int amountInMinor;

    private Currency currency;

    @SerializedName("payment_method")
    private PaymentMethod paymentMethod;

    @Builder
    public static class PaymentMethod {

        private Type type;

        private String statement_reference;

        enum Type {
            @SerializedName("bank_transfer")
            BANK_TRANSFER
        }

    }

    public enum Currency {
        @SerializedName("GBP")
        GBP,
        @SerializedName("EUR")
        EUR
    }
}
