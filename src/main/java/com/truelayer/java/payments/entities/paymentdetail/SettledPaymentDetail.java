package com.truelayer.java.payments.entities.paymentdetail;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.truelayer.java.entities.AuthorizationFlowWithConfiguration;
import com.truelayer.java.entities.PaymentSource;
import java.util.Date;
import java.util.Optional;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = false)
public class SettledPaymentDetail extends PaymentDetail {

    Status status = Status.SETTLED;

    PaymentSource paymentSource;

    Date succeededAt;

    Date settledAt;

    Date executedAt;

    AuthorizationFlowWithConfiguration authorizationFlow;

    @JsonGetter
    public Optional<AuthorizationFlowWithConfiguration> getAuthorizationFlow() {
        return Optional.ofNullable(authorizationFlow);
    }
}
