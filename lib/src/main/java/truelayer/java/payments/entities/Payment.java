package truelayer.java.payments.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class Payment {
    @JsonProperty("id")
    private final String id;

    @JsonProperty("amount_in_minor")
    private int amountInMinor;

    @JsonProperty("currency")
    private String currency;

    @JsonProperty("beneficiary")
    private BaseBeneficiary beneficiary;

    @JsonProperty("user")
    private User user;

    @JsonProperty("payment_method")
    private BasePaymentMethod paymentMethod;

    @JsonProperty("created_at")
    private String createdAt;

    @JsonProperty("status")
    private String status;

    @JsonProperty("resource_token")
    private String resourceToken;
}
