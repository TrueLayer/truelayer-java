package com.truelayer.java.payouts.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonValue;
import com.truelayer.java.TrueLayerException;
import com.truelayer.java.entities.CurrencyCode;
import com.truelayer.java.entities.SchemeId;
import com.truelayer.java.entities.beneficiary.Beneficiary;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.ZonedDateTime;
import java.util.Map;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "status", defaultImpl = PendingPayout.class)
@JsonSubTypes({
        @JsonSubTypes.Type(value = PendingPayout.class, name = "pending"),
        @JsonSubTypes.Type(value = AuthorizedPayout.class, name = "authorized"),
        @JsonSubTypes.Type(value = ExecutedPayout.class, name = "executed"),
        @JsonSubTypes.Type(value = FailedPayout.class, name = "failed")
})
@Getter
public abstract class Payout {
    private String id;
    private String merchantAccountId;
    private int amountInMinor;
    private CurrencyCode currency;
    private Beneficiary beneficiary;
    private Map<String, String> metadata;
    private SchemeId schemeId;
    private ZonedDateTime createdAt;

    @JsonIgnore
    public abstract Status getStatus();

    @JsonIgnore
    public boolean isPending() { return this instanceof PendingPayout; }

    @JsonIgnore
    public boolean isAuthorized() { return this instanceof AuthorizedPayout; }

    @JsonIgnore
    public boolean isExecuted() { return this instanceof ExecutedPayout; }

    @JsonIgnore
    public boolean isFailed() { return this instanceof FailedPayout; }

    @JsonIgnore
    public PendingPayout asPendingPayout() {
        if(!isPending())
            throw new TrueLayerException(buildErrorMessage());
        return (PendingPayout) this;
    }

    @JsonIgnore
    public AuthorizedPayout asAuthorizedPayout() {
        if(!isAuthorized())
            throw new TrueLayerException(buildErrorMessage());
        return (AuthorizedPayout) this;
    }

    @JsonIgnore
    public ExecutedPayout asExecutedPayout() {
        if(!isExecuted())
            throw new TrueLayerException(buildErrorMessage());
        return (ExecutedPayout) this;
    }

    @JsonIgnore
    public FailedPayout asFailedPayout() {
        if(!isFailed())
            throw new TrueLayerException(buildErrorMessage());
        return (FailedPayout) this;
    }

    private String buildErrorMessage() {
        return String.format("Payout is of type %s.", this.getClass().getSimpleName());
    }

    @Getter
    @RequiredArgsConstructor
    public enum Status {
        PENDING("pending"),
        AUTHORIZED("authorized"),
        EXECUTED("executed"),
        FAILED("failed");

        @JsonValue
        private final String status;
    }
}
