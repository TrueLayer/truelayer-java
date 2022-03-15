package com.truelayer.java.recurringpayments.entities.mandatedetail;

import com.truelayer.java.entities.AuthorizationFlowWithConfiguration;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = false)
public class AuthorizingMandateDetail extends MandateDetail {

    Status status = Status.AUTHORIZING;

    AuthorizationFlowWithConfiguration authorizationFlow;
}
