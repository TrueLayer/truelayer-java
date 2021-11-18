package truelayer.java.payments.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
@JsonIgnoreProperties(ignoreUnknown = true) //todo remove and complete the DTO
public class Payment {

    @JsonProperty("id")
    private final String paymentId;

    @JsonProperty("status")
    private final String status;

    @JsonProperty("resource_token")
    private final String resourceToken;
}
