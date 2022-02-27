package com.truelayer.java.payments.entities;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = false)
public class PaymentAuthorizationFlowAuthorizationFailed extends SubmitProviderSelectionResponse {
    Status status = Status.FAILED;

    String failureStage;

    String failureReason;
}
