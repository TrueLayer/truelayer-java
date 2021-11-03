package truelayer.java.payments.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Payment {

    @SerializedName("id")
    private final String paymentId;

    @SerializedName("status")
    private final String status;

    @JsonCreator
    public Payment(@JsonProperty("id") String paymentId, @JsonProperty("status") String status) {
        this.paymentId = paymentId;
        this.status = status;
    }
}
