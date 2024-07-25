package com.truelayer.java.payments.entities.paymentdetail;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonValue;
import com.truelayer.java.entities.AuthorizationFlowWithConfiguration;
import com.truelayer.java.entities.PaymentSource;
import java.time.ZonedDateTime;
import java.util.Optional;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = false)
public class AttemptFailedPaymentDetail extends PaymentDetail {

    Status status = Status.ATTEMPT_FAILED;

    PaymentSource paymentSource;

    ZonedDateTime failedAt;

    FailureStage failureStage;

    String failureReason;

    AuthorizationFlowWithConfiguration authorizationFlow;

    @JsonGetter
    public Optional<AuthorizationFlowWithConfiguration> getAuthorizationFlow() {
        return Optional.ofNullable(authorizationFlow);
    }

    @RequiredArgsConstructor
    @Getter
    public enum FailureStage {
        AUTHORIZING("authorizing"),
        AUTHORIZED("authorized");

        @JsonValue
        private final String failureStage;
    }
}
