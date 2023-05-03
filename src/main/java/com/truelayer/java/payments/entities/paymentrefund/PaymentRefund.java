package com.truelayer.java.payments.entities.paymentrefund;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.truelayer.java.TrueLayerException;
import com.truelayer.java.entities.CurrencyCode;
import java.time.ZonedDateTime;
import java.util.Map;
import lombok.Getter;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "status", defaultImpl = PendingPaymentRefund.class)
@JsonSubTypes({
    @JsonSubTypes.Type(value = PendingPaymentRefund.class, name = "pending"),
    @JsonSubTypes.Type(value = AuthorizedPaymentRefund.class, name = "authorized"),
    @JsonSubTypes.Type(value = ExecutedPaymentRefund.class, name = "executed"),
    @JsonSubTypes.Type(value = FailedPaymentRefund.class, name = "failed"),
})
@Getter
public abstract class PaymentRefund {
    private String id;
    private int amountInMinor;
    private CurrencyCode currency;
    private String reference;
    private Map<String, String> metadata;
    private ZonedDateTime createdAt;

    @JsonIgnore
    public abstract Status getStatus();

    @JsonIgnore
    public boolean isPending() {
        return this instanceof PendingPaymentRefund;
    }

    @JsonIgnore
    public boolean isAuthorized() {
        return this instanceof AuthorizedPaymentRefund;
    }

    @JsonIgnore
    public boolean isExecuted() {
        return this instanceof ExecutedPaymentRefund;
    }

    @JsonIgnore
    public boolean isFailed() {
        return this instanceof FailedPaymentRefund;
    }

    @JsonIgnore
    public PendingPaymentRefund asPendingPaymentRefund() {
        if (!isPending()) throw new TrueLayerException(buildErrorMessage());

        return (PendingPaymentRefund) this;
    }

    @JsonIgnore
    public AuthorizedPaymentRefund asAuthorizedPaymentRefund() {
        if (!isAuthorized()) throw new TrueLayerException(buildErrorMessage());

        return (AuthorizedPaymentRefund) this;
    }

    @JsonIgnore
    public ExecutedPaymentRefund asExecutedPaymentRefund() {
        if (!isExecuted()) throw new TrueLayerException(buildErrorMessage());

        return (ExecutedPaymentRefund) this;
    }

    @JsonIgnore
    public FailedPaymentRefund asFailedPaymentRefund() {
        if (!isFailed()) throw new TrueLayerException(buildErrorMessage());

        return (FailedPaymentRefund) this;
    }

    private String buildErrorMessage() {
        return String.format("Payment refund is of type %s.", this.getClass().getSimpleName());
    }
}
