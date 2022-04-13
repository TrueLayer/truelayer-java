package com.truelayer.java.payments.entities.paymentdetail;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.truelayer.java.TrueLayerException;
import com.truelayer.java.entities.User;
import com.truelayer.java.payments.entities.paymentmethod.PaymentMethod;
import java.util.Date;
import lombok.*;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "status", defaultImpl = AuthorizationRequiredPaymentDetail.class)
@JsonSubTypes({
    @JsonSubTypes.Type(value = AuthorizationRequiredPaymentDetail.class, name = "authorization_required"),
    @JsonSubTypes.Type(value = AuthorizingPaymentDetail.class, name = "authorizing"),
    @JsonSubTypes.Type(value = AuthorizedPaymentDetail.class, name = "authorized"),
    @JsonSubTypes.Type(value = FailedPaymentDetail.class, name = "failed"),
    @JsonSubTypes.Type(value = SettledPaymentDetail.class, name = "settled"),
    @JsonSubTypes.Type(value = ExecutedPaymentDetail.class, name = "executed")
})
@Getter
public abstract class PaymentDetail {
    private String id;

    private int amountInMinor;

    private String currency;

    private User user;

    private PaymentMethod paymentMethod;

    private Date createdAt;

    public abstract Status getStatus();

    @JsonIgnore
    public boolean isAuthorizationRequired() {
        return this instanceof AuthorizationRequiredPaymentDetail;
    }

    @JsonIgnore
    public boolean isAuthorized() {
        return this instanceof AuthorizedPaymentDetail;
    }

    @JsonIgnore
    public boolean isAuthorizing() {
        return this instanceof AuthorizingPaymentDetail;
    }

    @JsonIgnore
    public boolean isFailed() {
        return this instanceof FailedPaymentDetail;
    }

    @JsonIgnore
    public boolean isExecuted() {
        return this instanceof ExecutedPaymentDetail;
    }

    @JsonIgnore
    public boolean isSettled() {
        return this instanceof SettledPaymentDetail;
    }

    @JsonIgnore
    public AuthorizationRequiredPaymentDetail asAuthorizationRequired() {
        if (!isAuthorizationRequired()) {
            throw new TrueLayerException(buildErrorMessage());
        }
        return (AuthorizationRequiredPaymentDetail) this;
    }

    @JsonIgnore
    public AuthorizedPaymentDetail asAuthorized() {
        if (!isAuthorized()) {
            throw new TrueLayerException(buildErrorMessage());
        }
        return (AuthorizedPaymentDetail) this;
    }

    @JsonIgnore
    public AuthorizingPaymentDetail asAuthorizing() {
        if (!isAuthorizing()) {
            throw new TrueLayerException(buildErrorMessage());
        }
        return (AuthorizingPaymentDetail) this;
    }

    @JsonIgnore
    public FailedPaymentDetail asFailed() {
        if (!isFailed()) {
            throw new TrueLayerException(buildErrorMessage());
        }
        return (FailedPaymentDetail) this;
    }

    @JsonIgnore
    public ExecutedPaymentDetail asExecuted() {
        if (!isExecuted()) {
            throw new TrueLayerException(buildErrorMessage());
        }
        return (ExecutedPaymentDetail) this;
    }

    @JsonIgnore
    public SettledPaymentDetail asSettled() {
        if (!isSettled()) {
            throw new TrueLayerException(buildErrorMessage());
        }
        return (SettledPaymentDetail) this;
    }

    private String buildErrorMessage() {
        return String.format("Payment is of type %s.", this.getClass().getSimpleName());
    }
}
