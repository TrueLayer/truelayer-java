package truelayer.java.payments.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Value;

import java.util.Optional;

@Value
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetPaymentByIdResponse {
    @JsonProperty("id")
    private String id;

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
    private Status status;

    @Value
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class User {
        @JsonProperty("id")
        private String id;

        @JsonProperty("name")
        private String name;

        @JsonProperty("email")
        private Optional<String> email;

        @JsonProperty("phone")
        private Optional<String> phone;
    }

    public enum Status {
        AUTHORIZATION_REQUIRED("authorization_required"),
        AUTHORIZING("authorizing"),
        AUTHORIZED("authorized"),
        EXECUTED("executed"),
        FAILED("failed"),
        SETTLED("settled");

        private final String status;

        Status(String status) {
            this.status = status;
        }

        @JsonValue
        public String getStatus() {
            return status;
        }
    }
}
