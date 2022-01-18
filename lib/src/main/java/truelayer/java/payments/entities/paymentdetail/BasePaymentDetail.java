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

import static truelayer.java.payments.entities.paymentdetail.Status.*;

@JsonTypeInfo(
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        use = JsonTypeInfo.Id.NAME,
        property = "status",
        defaultImpl = AuthorizationRequiredPaymentDetail.class
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = AuthorizationRequiredPaymentDetail.class, name = "authorization_required")
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

    private Status status;

    public AuthorizedPaymentDetail asAuthorized() {
        if (status != AUTHORIZED) {
            throw new TrueLayerException(buildErrorMessage());
        }
        return (AuthorizedPaymentDetail) this;
    }

    public AuthorizingPaymentDetail asAuthorizing() {
        if (status != AUTHORIZING) {
            throw new TrueLayerException(buildErrorMessage());
        }
        return (AuthorizingPaymentDetail) this;
    }

    public FailedPaymentDetail asFailed() {
        if (status != FAILED) {
            throw new TrueLayerException(buildErrorMessage());
        }
        return (FailedPaymentDetail) this;
    }

    public SucceededPaymentDetail asSucceeded() {
        if (status != SUCCEEDED) {
            throw new TrueLayerException(buildErrorMessage());
        }
        return (SucceededPaymentDetail) this;
    }

    public SettledPaymentDetail asSettled() {
        if (status != SETTLED) {
            throw new TrueLayerException(buildErrorMessage());
        }
        return (SettledPaymentDetail) this;
    }

    private String buildErrorMessage() {
        return String.format("payment is of type %1$s. Consider using as%1$s() instead.", this.getClass().getSimpleName());
    }
}
