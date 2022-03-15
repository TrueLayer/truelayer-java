package com.truelayer.java.recurringpayments.entities.mandatedetail;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.truelayer.java.entities.User;
import com.truelayer.java.entities.beneficiary.Beneficiary;
import com.truelayer.java.payments.entities.paymentdetail.*;
import com.truelayer.java.recurringpayments.entities.mandate.Constraints;
import java.util.Date;
import lombok.Getter;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "status", defaultImpl = AuthorizationRequiredMandateDetail.class)
@JsonSubTypes({
    @JsonSubTypes.Type(value = AuthorizationRequiredMandateDetail.class, name = "authorization_required"),
    @JsonSubTypes.Type(value = AuthorizingMandateDetail.class, name = "authorizing"),
    @JsonSubTypes.Type(value = AuthorizedMandateDetail.class, name = "authorized"),
    @JsonSubTypes.Type(value = FailedMandateDetail.class, name = "failed"),
})
@Getter
public abstract class MandateDetail {
    private String id;

    private String currency;

    private Beneficiary beneficiary;

    private User user;

    private Date createdAt;

    private Constraints constraints;

    public abstract Status getStatus();

    /*public AuthorizationRequiredPaymentDetail asAuthorizationRequiredPaymentDetail() {
            if (!(this instanceof AuthorizationRequiredPaymentDetail)) {
                throw new TrueLayerException(buildErrorMessage());
            }
            return (AuthorizationRequiredPaymentDetail) this;
        }

        public AuthorizedPaymentDetail asAuthorizedPaymentDetail() {
            if (!(this instanceof AuthorizedPaymentDetail)) {
                throw new TrueLayerException(buildErrorMessage());
            }
            return (AuthorizedPaymentDetail) this;
        }

        public AuthorizingPaymentDetail asAuthorizingPaymentDetail() {
            if (!(this instanceof AuthorizingPaymentDetail)) {
                throw new TrueLayerException(buildErrorMessage());
            }
            return (AuthorizingPaymentDetail) this;
        }

        public FailedPaymentDetail asFailedPaymentDetail() {
            if (!(this instanceof FailedPaymentDetail)) {
                throw new TrueLayerException(buildErrorMessage());
            }
            return (FailedPaymentDetail) this;
        }

        public ExecutedPaymentDetail asSucceededPaymentDetail() {
            if (!(this instanceof ExecutedPaymentDetail)) {
                throw new TrueLayerException(buildErrorMessage());
            }
            return (ExecutedPaymentDetail) this;
        }

        public SettledPaymentDetail asSettledPaymentDetail() {
            if (!(this instanceof SettledPaymentDetail)) {
                throw new TrueLayerException(buildErrorMessage());
            }
            return (SettledPaymentDetail) this;
        }
    */
    private String buildErrorMessage() {
        return String.format(
                "payment is of type %1$s. Consider using as%1$s() instead.",
                this.getClass().getSimpleName());
    }
}
