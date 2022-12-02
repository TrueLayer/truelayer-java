package com.truelayer.java.payments.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.truelayer.java.TrueLayerException;
import com.truelayer.java.payments.entities.paymentdetail.Status;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "status", defaultImpl = AuthorizationFlowAuthorizing.class)
@JsonSubTypes({
    @JsonSubTypes.Type(value = AuthorizationFlowAuthorizing.class, name = "authorizing"),
    @JsonSubTypes.Type(value = AuthorizationFlowAuthorizationFailed.class, name = "failed")
})
@ToString
@EqualsAndHashCode
public abstract class AuthorizationFlowResponse {

    @JsonIgnore
    public abstract Status getStatus();

    @Deprecated
    protected AuthorizationFlow authorizationFlow;

    @JsonIgnore
    public boolean isAuthorizing() {
        return this instanceof AuthorizationFlowAuthorizing;
    }

    @JsonIgnore
    public boolean isAuthorizationFailed() {
        return this instanceof AuthorizationFlowAuthorizationFailed;
    }

    @JsonIgnore
    public AuthorizationFlowAuthorizing asAuthorizing() {
        if (!isAuthorizing()) {
            throw new TrueLayerException(buildErrorMessage());
        }
        return (AuthorizationFlowAuthorizing) this;
    }

    @JsonIgnore
    public AuthorizationFlowAuthorizationFailed asAuthorizationFailed() {
        if (!isAuthorizationFailed()) {
            throw new TrueLayerException(buildErrorMessage());
        }
        return (AuthorizationFlowAuthorizationFailed) this;
    }

    /**
     * Deprecated: Only {@link AuthorizationFlowAuthorizing Authorizing} responses have {@link AuthorizationFlow}. Use {@link #asAuthorizing()}.{@link AuthorizationFlowAuthorizing#getAuthorizationFlow() getAuthorizationFlow()} instead.
     */
    @Deprecated
    public AuthorizationFlow getAuthorizationFlow() {
        return authorizationFlow;
    }

    private String buildErrorMessage() {
        return String.format("Response is of type %s.", this.getClass().getSimpleName());
    }
}
