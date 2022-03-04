package com.truelayer.java.payments.entities.paymentdetail;

import lombok.*;

@Value
@EqualsAndHashCode(callSuper = false)
public class AuthorizationRequiredPaymentDetail extends PaymentDetail {

    Status status = Status.AUTHORIZATION_REQUIRED;
}
