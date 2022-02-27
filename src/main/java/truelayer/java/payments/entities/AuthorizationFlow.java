package truelayer.java.payments.entities;

import lombok.*;
import truelayer.java.payments.entities.paymentdetail.AuthorizationFlowAction;

@EqualsAndHashCode
@ToString
@Getter
public class AuthorizationFlow {
    Actions actions;

    @Value
    public static class Actions {

        AuthorizationFlowAction next;
    }
}
