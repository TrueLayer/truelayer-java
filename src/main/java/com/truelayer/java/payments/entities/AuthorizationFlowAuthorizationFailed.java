package com.truelayer.java.payments.entities;

import com.truelayer.java.payments.entities.paymentdetail.Status;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = false)
public class AuthorizationFlowAuthorizationFailed extends AuthorizationFlowResponse {
    Status status = Status.FAILED;

    String failureStage;

    String failureReason;
}
