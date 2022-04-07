package com.truelayer.java.mandates.entities.mandatedetail;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = false)
public class AuthorizationRequiredMandateDetail extends MandateDetail {

    Status status = Status.AUTHORIZATION_REQUIRED;
}
