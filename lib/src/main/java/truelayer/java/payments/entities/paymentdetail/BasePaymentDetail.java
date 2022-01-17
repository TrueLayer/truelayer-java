package truelayer.java.payments.entities.paymentdetail;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

import java.util.Optional;

@JsonTypeInfo(
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        use = JsonTypeInfo.Id.NAME,
        property = "status",
        defaultImpl = AuthorizationRequired.class
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = AuthorizationRequired.class, name = "authorization_required")
})
@Getter
@ToString
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class BasePaymentDetail {

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

    @Value
    @Builder
    @Getter
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

    @Builder
    @Getter
    @Value
    public static class AuthorizationFlow {

    }
}
