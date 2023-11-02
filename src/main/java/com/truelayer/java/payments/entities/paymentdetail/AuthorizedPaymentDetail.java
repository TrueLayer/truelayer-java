package com.truelayer.java.payments.entities.paymentdetail;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.truelayer.java.entities.AuthorizationFlowWithConfiguration;
import java.util.Optional;

import com.truelayer.java.entities.PaymentSource;
import lombok.*;

@Value
@EqualsAndHashCode(callSuper = false)
public class AuthorizedPaymentDetail extends PaymentDetail {

    Status status = Status.AUTHORIZED;

    PaymentSource paymentSource;

    AuthorizationFlowWithConfiguration authorizationFlow;

    @JsonGetter
    public Optional<AuthorizationFlowWithConfiguration> getAuthorizationFlow() {
        return Optional.ofNullable(authorizationFlow);
    }
}
