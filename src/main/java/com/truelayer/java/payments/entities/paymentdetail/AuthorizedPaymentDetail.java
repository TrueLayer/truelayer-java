package com.truelayer.java.payments.entities.paymentdetail;

import com.fasterxml.jackson.annotation.JsonGetter;
import java.util.Optional;
import lombok.*;

@Value
@EqualsAndHashCode(callSuper = false)
public class AuthorizedPaymentDetail extends PaymentDetail {

    Status status = Status.AUTHORIZED;

    AuthorizationFlowWithConfiguration authorizationFlow;

    @JsonGetter
    public Optional<AuthorizationFlowWithConfiguration> getAuthorizationFlow() {
        return Optional.ofNullable(authorizationFlow);
    }
}
