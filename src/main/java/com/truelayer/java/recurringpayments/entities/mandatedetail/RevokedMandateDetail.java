package com.truelayer.java.recurringpayments.entities.mandatedetail;

import com.truelayer.java.entities.AuthorizationFlowWithConfiguration;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = false)
public class RevokedMandateDetail extends MandateDetail {

    Status status = Status.REVOKED;

    String revocationSource;

    AuthorizationFlowWithConfiguration authorizationFlow;
}
