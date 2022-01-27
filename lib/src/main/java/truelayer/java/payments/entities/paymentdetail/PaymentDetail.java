package truelayer.java.payments.entities.paymentdetail;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.util.Date;

import lombok.*;
import truelayer.java.TrueLayerException;
import truelayer.java.payments.entities.User;
import truelayer.java.payments.entities.beneficiary.Beneficiary;
import truelayer.java.payments.entities.paymentmethod.PaymentMethod;

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
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class PaymentDetail {
    private String id;

    private int amountInMinor;

    private String currency;

    private Beneficiary beneficiary;

    private User user;

    private PaymentMethod paymentMethod;

    private Date createdAt;

    private Status status;

    public AuthorizationRequiredPaymentDetail asAuthorizationRequiredPaymentDetail() {
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

    private String buildErrorMessage() {
        return String.format(
                "payment is of type %1$s. Consider using as%1$s() instead.",
                this.getClass().getSimpleName());
    }
}
