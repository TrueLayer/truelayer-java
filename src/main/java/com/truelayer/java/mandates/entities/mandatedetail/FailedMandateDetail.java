package com.truelayer.java.mandates.entities.mandatedetail;

import com.fasterxml.jackson.annotation.JsonValue;
import com.truelayer.java.entities.AuthorizationFlowWithConfiguration;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = false)
public class FailedMandateDetail extends MandateDetail {

    Status status = Status.FAILED;

    FailureStage failureStage;

    String failureReason;

    AuthorizationFlowWithConfiguration authorizationFlow;

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
