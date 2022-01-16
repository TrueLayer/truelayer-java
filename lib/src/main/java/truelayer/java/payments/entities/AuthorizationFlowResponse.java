package truelayer.java.payments.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Value;

@Value
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthorizationFlowResponse {

    @JsonProperty("authorization_flow")
    private AuthorizationFlow authorizationFlow;

    @JsonProperty("status")
    private Status status;

    @Value
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class AuthorizationFlow {

        @JsonProperty("actions")
        private Actions actions;

        @Value
        @JsonInclude(JsonInclude.Include.NON_NULL)
        public static class Actions {

            @JsonProperty("next")
            private BaseAuthorizationFlowAction next;
        }
    }

    public enum Status {
        AUTHORIZING("authorizing");

        private final String status;

        Status(String status) {
            this.status = status;
        }

        @JsonValue
        public String getStatus() {
            return this.status;
        }
    }
}
