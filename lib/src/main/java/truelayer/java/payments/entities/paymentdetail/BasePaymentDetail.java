package truelayer.java.payments.entities.paymentdetail;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import truelayer.java.TrueLayerException;
import truelayer.java.payments.entities.beneficiary.BaseBeneficiary;
import truelayer.java.payments.entities.paymentmethod.BasePaymentMethod;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "status",
        defaultImpl = AuthorizationRequiredPaymentDetail.class
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = AuthorizationRequiredPaymentDetail.class, name = "authorization_required"),
        @JsonSubTypes.Type(value = AuthorizingPaymentDetail.class, name = "authorizing"),
        @JsonSubTypes.Type(value = AuthorizedPaymentDetail.class, name = "authorized"),
        @JsonSubTypes.Type(value = FailedPaymentDetail.class, name = "failed"),
        @JsonSubTypes.Type(value = SettledPaymentDetail.class, name = "settled"),
        @JsonSubTypes.Type(value = SucceededPaymentDetail.class, name = "succeeded")
})
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public abstract class BasePaymentDetail {
    private String id;

    private int amountInMinor;

    private String currency;

    private BaseBeneficiary beneficiary;

    private User user;

    private BasePaymentMethod paymentMethod;

    private String createdAt;

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

    public SucceededPaymentDetail asSucceededPaymentDetail() {
        if (!(this instanceof SucceededPaymentDetail)) {
            throw new TrueLayerException(buildErrorMessage());
        }
        return (SucceededPaymentDetail) this;
    }

    public SettledPaymentDetail asSettledPaymentDetail() {
        if (!(this instanceof SettledPaymentDetail)) {
            throw new TrueLayerException(buildErrorMessage());
        }
        return (SettledPaymentDetail) this;
    }

    private String buildErrorMessage() {
        return String.format("payment is of type %1$s. Consider using as%1$s() instead.", this.getClass().getSimpleName());
    }
}
