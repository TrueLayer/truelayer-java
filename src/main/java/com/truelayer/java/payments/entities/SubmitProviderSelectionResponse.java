package com.truelayer.java.payments.entities;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@JsonTypeInfo(
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        use = JsonTypeInfo.Id.NAME,
        property = "status",
        defaultImpl = PaymentAuthorizationFlowAuthorizing.class)
@JsonSubTypes({
    @JsonSubTypes.Type(value = PaymentAuthorizationFlowAuthorizing.class, name = "authorizing"),
    @JsonSubTypes.Type(value = PaymentAuthorizationFlowAuthorizationFailed.class, name = "failed")
})
@Getter
@ToString
@EqualsAndHashCode
public abstract class SubmitProviderSelectionResponse {

    protected Status status;

    AuthorizationFlow authorizationFlow;

    @RequiredArgsConstructor
    @Getter
    public enum Status {
        AUTHORIZING("authorizing"),
        FAILED("failed");

        @JsonValue
        private final String status;
    }
}
