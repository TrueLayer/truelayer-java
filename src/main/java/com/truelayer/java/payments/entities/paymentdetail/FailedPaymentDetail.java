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
public class FailedPaymentDetail extends PaymentDetail {

    Status status = Status.FAILED;

    PaymentSource paymentSource;

    ZonedDateTime failedAt;

    ZonedDateTime creditableAt;

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
        AUTHORIZATION_REQUIRED("authorization_required"),
        AUTHORIZING("authorizing"),
        AUTHORIZED("authorized");

        @JsonValue
        private final String failureStage;
    }
}
