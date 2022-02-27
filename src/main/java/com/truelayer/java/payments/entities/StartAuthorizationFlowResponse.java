package com.truelayer.java.payments.entities;

import com.truelayer.java.payments.entities.paymentdetail.Status;
import lombok.Value;

@Value
public class StartAuthorizationFlowResponse {

    AuthorizationFlow authorizationFlow;

    Status status = Status.AUTHORIZING;
}
