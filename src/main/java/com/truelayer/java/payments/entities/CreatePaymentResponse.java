package com.truelayer.java.payments.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.truelayer.java.TrueLayerException;
import com.truelayer.java.entities.User;
import com.truelayer.java.payments.entities.paymentdetail.Status;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "status")
@JsonSubTypes({
    @JsonSubTypes.Type(value = CreatePaymentResponseAuthorizationRequired.class, name = "authorization_required"),
    @JsonSubTypes.Type(value = CreatePaymentResponseAuthorized.class, name = "authorized"),
    @JsonSubTypes.Type(value = CreatePaymentResponseFailed.class, name = "failed")
})
@ToString
@EqualsAndHashCode
@Getter
public abstract class CreatePaymentResponse {
    @JsonIgnore
    public abstract Status getStatus();

    String id;

    User user;

    String resourceToken;

    @JsonIgnore
    public boolean isAuthorizationRequired() {
        return this instanceof CreatePaymentResponseAuthorizationRequired;
    }

    @JsonIgnore
    public boolean isAuthorized() {
        return this instanceof CreatePaymentResponseAuthorized;
    }

    @JsonIgnore
    public boolean isFailed() {
        return this instanceof CreatePaymentResponseFailed;
    }

    @JsonIgnore
    public CreatePaymentResponseAuthorizationRequired asAuthorizationRequired() {
        if (!isAuthorizationRequired()) {
            throw new TrueLayerException(buildErrorMessage());
        }
        return (CreatePaymentResponseAuthorizationRequired) this;
    }

    @JsonIgnore
    public CreatePaymentResponseAuthorized asAuthorized() {
        if (!isAuthorized()) {
            throw new TrueLayerException(buildErrorMessage());
        }
        return (CreatePaymentResponseAuthorized) this;
    }

    @JsonIgnore
    public CreatePaymentResponseFailed asFailed() {
        if (!isFailed()) {
            throw new TrueLayerException(buildErrorMessage());
        }
        return (CreatePaymentResponseFailed) this;
    }

    private String buildErrorMessage() {
        return String.format("Response is of type %s.", this.getClass().getSimpleName());
    }
}
