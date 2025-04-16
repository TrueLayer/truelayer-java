package com.truelayer.java.payments.entities.paymentdetail;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.truelayer.java.entities.AuthorizationFlowWithConfiguration;
import com.truelayer.java.entities.PaymentSource;
import java.time.ZonedDateTime;
import java.util.Optional;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = false)
public class ExecutedPaymentDetail extends PaymentDetail {
    Status status = Status.EXECUTED;

    PaymentSource paymentSource;

    ZonedDateTime executedAt;

    ZonedDateTime creditableAt;

    AuthorizationFlowWithConfiguration authorizationFlow;

    SettlementRisk settlementRisk;

    @JsonGetter
    public Optional<AuthorizationFlowWithConfiguration> getAuthorizationFlow() {
        return Optional.ofNullable(authorizationFlow);
    }

    @JsonGetter
    public Optional<SettlementRisk> getSettlementRisk() {
        return Optional.ofNullable(settlementRisk);
    }
}
