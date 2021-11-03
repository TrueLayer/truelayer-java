package truelayer.java.payments.entities;

import com.google.gson.annotations.SerializedName;

public class Payment {

    @SerializedName("id")
    private final String paymentId;

    @SerializedName("status")
    private final String status;

    public Payment(String paymentId, String status) {
        this.paymentId = paymentId;
        this.status = status;
    }
}
