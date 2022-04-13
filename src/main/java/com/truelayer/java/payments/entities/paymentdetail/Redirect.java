package com.truelayer.java.payments.entities.paymentdetail;

import java.net.URI;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = false)
public class Redirect extends AuthorizationFlowAction {
    Type type = Type.REDIRECT;

    URI uri;
}
