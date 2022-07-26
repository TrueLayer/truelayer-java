package com.truelayer.java.payments.entities;

import com.truelayer.java.payments.entities.paymentdetail.Status;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = false)
public class AuthorizationFlowAuthorizing extends AuthorizationFlowResponse {

    AuthorizationFlow authorizationFlow;

    Status status = Status.AUTHORIZING;
}
