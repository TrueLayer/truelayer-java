package truelayer.java.payments.entities.paymentdetail;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;
import truelayer.java.payments.entities.beneficiary.BaseBeneficiary;
import truelayer.java.payments.entities.paymentmethod.BasePaymentMethod;

import java.util.Optional;

import static truelayer.java.payments.entities.paymentdetail.BasePaymentDetail.Status.AUTHORIZED;
import static truelayer.java.payments.entities.paymentdetail.BasePaymentDetail.Status.AUTHORIZING;

@Getter
@Value
@EqualsAndHashCode(callSuper = true)
public class Authorized extends BasePaymentDetail {
    @JsonProperty("id")
    private String id;

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

    @JsonProperty("authorization_flow")
    private Optional<AuthorizationFlow> authorizationFlow;

    @JsonProperty("status")
    private final Status status = AUTHORIZED;
}
