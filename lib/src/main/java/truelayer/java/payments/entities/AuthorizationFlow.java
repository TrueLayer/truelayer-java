package truelayer.java.payments.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.Value;
import truelayer.java.payments.entities.paymentdetail.AuthorizationFlowAction;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthorizationFlow {
    Actions actions;

    @Value
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Actions {

        private AuthorizationFlowAction next;
    }
}
