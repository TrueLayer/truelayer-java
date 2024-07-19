package com.truelayer.java.payments.entities.paymentdetail;

import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.List;

@Value
@EqualsAndHashCode(callSuper = false)
public class Retry extends AuthorizationFlowAction {
    Type type = Type.RETRY;

    List<String> retryOptions;
}
