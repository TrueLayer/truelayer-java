package com.truelayer.java.payments.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.truelayer.java.TrueLayerException;
import com.truelayer.java.payments.entities.paymentdetail.Status;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@JsonTypeInfo(
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        use = JsonTypeInfo.Id.NAME,
        property = "status",
        defaultImpl = AuthorizationFlowAuthorizing.class)
@JsonSubTypes({
    @JsonSubTypes.Type(value = AuthorizationFlowAuthorizing.class, name = "authorizing"),
    @JsonSubTypes.Type(value = AuthorizationFlowAuthorizationFailed.class, name = "failed")
})
@Getter
@ToString
@EqualsAndHashCode
public abstract class AuthorizationFlowResponse {

    protected Status status;

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

    // @deprecated
    // Only `Authorizating` responses have `AuthorizationFlow`.
    // Use `asAuthorizing().getAuthorizationFlow()` instead.
    @Deprecated
    @JsonIgnore
    public AuthorizationFlow getAuthorizationFlow() {
        if (!isAuthorizing()) {
            throw new TrueLayerException(buildErrorMessage());
        }
        return asAuthorizing().getAuthorizationFlow();
    }

    private String buildErrorMessage() {
        return String.format("Response is of type %s.", this.getClass().getSimpleName());
    }
}
