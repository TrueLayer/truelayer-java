package com.truelayer.java.payments.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonValue;
import com.truelayer.java.TrueLayerException;
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

    @JsonIgnore
    public boolean isAuthorizing() {
        return this instanceof PaymentAuthorizationFlowAuthorizing;
    }

    @JsonIgnore
    public boolean isAuthorizationFailed() {
        return this instanceof PaymentAuthorizationFlowAuthorizationFailed;
    }

    public PaymentAuthorizationFlowAuthorizing asAuthorizing() {
        if (!isAuthorizing()) {
            throw new TrueLayerException(buildErrorMessage());
        }
        return (PaymentAuthorizationFlowAuthorizing) this;
    }

    public PaymentAuthorizationFlowAuthorizationFailed asFailed() {
        if (!isAuthorizationFailed()) {
            throw new TrueLayerException(buildErrorMessage());
        }
        return (PaymentAuthorizationFlowAuthorizationFailed) this;
    }

    private String buildErrorMessage() {
        return String.format(
                "Response type is of type %s. Consider using as%s.",
                this.getClass().getSimpleName(), getStatus().getStatus());
    }

    @RequiredArgsConstructor
    @Getter
    public enum Status {
        AUTHORIZING("authorizing"),
        FAILED("failed");

        @JsonValue
        private final String status;
    }
}
