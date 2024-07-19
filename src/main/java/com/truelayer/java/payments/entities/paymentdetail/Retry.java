package com.truelayer.java.payments.entities.paymentdetail;

import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = false)
public class Retry extends AuthorizationFlowAction {
    Type type = Type.RETRY;

    List<String> retryOptions;
}
