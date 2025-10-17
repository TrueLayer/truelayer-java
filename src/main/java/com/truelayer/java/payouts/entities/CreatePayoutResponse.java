package com.truelayer.java.payouts.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonValue;
import com.truelayer.java.TrueLayerException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "status", defaultImpl = CreatedPayout.class)
@JsonSubTypes({
    @JsonSubTypes.Type(value = CreatedPayout.class, name = "created"),
    @JsonSubTypes.Type(value = AuthorizationRequiredPayout.class, name = "authorization_required")
})
@ToString
@EqualsAndHashCode
@Getter
public abstract class CreatePayoutResponse {
    private String id;

    @JsonIgnore
    public abstract Status getStatus();

    @JsonIgnore
    public boolean isCreated() {
        return this instanceof CreatedPayout;
    }

    @JsonIgnore
    public boolean isAuthorizationRequired() {
        return this instanceof AuthorizationRequiredPayout;
    }

    @JsonIgnore
    public CreatedPayout asCreated() {
        if (!isCreated()) throw new TrueLayerException(buildErrorMessage());
        return (CreatedPayout) this;
    }

    @JsonIgnore
    public AuthorizationRequiredPayout asAuthorizationRequired() {
        if (!isAuthorizationRequired()) throw new TrueLayerException(buildErrorMessage());
        return (AuthorizationRequiredPayout) this;
    }

    private String buildErrorMessage() {
        return String.format(
                "Create payout response is of type %s.", this.getClass().getSimpleName());
    }

    @Getter
    @RequiredArgsConstructor
    public enum Status {
        CREATED("created"),
        AUTHORIZATION_REQUIRED("authorization_required");

        @JsonValue
        private final String status;
    }
}
