package com.truelayer.java.payments.entities;

import com.truelayer.java.payments.entities.paymentdetail.AuthorizationFlowAction;
import lombok.*;

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
