package truelayer.java.payments.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import truelayer.java.payments.entities.paymentdetail.AuthorizationFlowAction;

@EqualsAndHashCode
@ToString
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthorizationFlow {
    Actions actions;

    @Value
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Actions {

        private AuthorizationFlowAction next;
    }
}
