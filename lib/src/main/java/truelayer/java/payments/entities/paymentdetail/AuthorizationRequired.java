package truelayer.java.payments.entities.paymentdetail;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import truelayer.java.payments.entities.beneficiary.BaseBeneficiary;
import truelayer.java.payments.entities.paymentmethod.BasePaymentMethod;

import static truelayer.java.payments.entities.paymentdetail.BasePaymentDetail.Status.AUTHORIZATION_REQUIRED;

@Getter
@Builder
public class AuthorizationRequired extends BasePaymentDetail {
    @JsonProperty("id")
    protected String id;

    @JsonProperty("amount_in_minor")
    private int amountInMinor;

    @JsonProperty("currency")
    private String currency;

    @JsonProperty("beneficiary")
    private BaseBeneficiary beneficiary;

    @JsonProperty("user")
    private User user;

    @JsonProperty("payment_method")
    private BasePaymentMethod paymentMethod;

    @JsonProperty("created_at")
    private String createdAt;

    @JsonProperty("status")
    private final Status status = AUTHORIZATION_REQUIRED;
}
