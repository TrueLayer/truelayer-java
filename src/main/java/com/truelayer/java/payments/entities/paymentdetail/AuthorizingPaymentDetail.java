package com.truelayer.java.payments.entities.paymentdetail;

import com.truelayer.java.entities.AuthorizationFlowWithConfiguration;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = false)
public class AuthorizingPaymentDetail extends PaymentDetail {

    Status status = Status.AUTHORIZING;

    AuthorizationFlowWithConfiguration authorizationFlow;
}
