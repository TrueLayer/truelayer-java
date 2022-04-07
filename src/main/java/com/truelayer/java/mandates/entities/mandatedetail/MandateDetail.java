package com.truelayer.java.mandates.entities.mandatedetail;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.truelayer.java.TrueLayerException;
import com.truelayer.java.entities.User;
import com.truelayer.java.entities.beneficiary.Beneficiary;
import java.util.Date;
import java.util.List;
import lombok.*;

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

    public AuthorizationRequiredMandateDetail asAuthorizationRequiredMandateDetail() {
        if (!(this instanceof AuthorizationRequiredMandateDetail)) {
            throw new TrueLayerException(buildErrorMessage());
        }
        return (AuthorizationRequiredMandateDetail) this;
    }

    public AuthorizedMandateDetail asAuthorizedMandateDetail() {
        if (!(this instanceof AuthorizedMandateDetail)) {
            throw new TrueLayerException(buildErrorMessage());
        }
        return (AuthorizedMandateDetail) this;
    }

    public AuthorizingMandateDetail asAuthorizingMandateDetail() {
        if (!(this instanceof AuthorizingMandateDetail)) {
            throw new TrueLayerException(buildErrorMessage());
        }
        return (AuthorizingMandateDetail) this;
    }

    public FailedMandateDetail asFailedMandateDetail() {
        if (!(this instanceof FailedMandateDetail)) {
            throw new TrueLayerException(buildErrorMessage());
        }
        return (FailedMandateDetail) this;
    }

    public RevokedMandateDetail asSucceededMandateDetail() {
        if (!(this instanceof RevokedMandateDetail)) {
            throw new TrueLayerException(buildErrorMessage());
        }
        return (RevokedMandateDetail) this;
    }

    private String buildErrorMessage() {
        return String.format(
                "payment is of type %1$s. Consider using as%1$s() instead.",
                this.getClass().getSimpleName());
    }

    // todo: trash this in the bin as soon as REC-285 is done
    @Value
    public static class Constraints {

        String validFrom;

        String validTo;

        Integer maximumIndividualAmount;

        List<PeriodicLimit> periodicLimits;

        @Value
        public static class PeriodicLimit {
            int maximumAmount;

            String periodAlignment;

            String periodType;
        }
    }
}
