package truelayer.java.payments.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class Payment {

    @JsonProperty("id")
    private final String paymentId;

    @JsonProperty("status")
    private final String status;
}
