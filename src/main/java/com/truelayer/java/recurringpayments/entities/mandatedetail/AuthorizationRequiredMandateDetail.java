package com.truelayer.java.recurringpayments.entities.mandatedetail;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = false)
public class AuthorizationRequiredMandateDetail extends MandateDetail {

    Status status = Status.AUTHORIZATION_REQUIRED;
}
