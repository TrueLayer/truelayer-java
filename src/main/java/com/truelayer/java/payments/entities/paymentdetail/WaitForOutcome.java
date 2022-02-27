package com.truelayer.java.payments.entities.paymentdetail;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = false)
public class WaitForOutcome extends AuthorizationFlowAction {
    Type type = Type.WAIT;
}
