package truelayer.java.payments.entities.paymentdetail;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Value;

@Value
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Configuration {
    ProviderSelection providerSelection;

    Redirect redirect;

    public enum Status {
        SUPPORTED("supported"),
        NOT_SUPPORTED("not_supported");

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
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ProviderSelection {
        Configuration.Status status;
    }

    @Value
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Redirect {
        Configuration.Status status;
    }
}