package truelayer.java.payments.entities.paymentdetail;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;
import truelayer.java.payments.entities.beneficiary.BaseBeneficiary;
import truelayer.java.payments.entities.paymentmethod.BasePaymentMethod;

import java.util.Optional;

import static truelayer.java.payments.entities.paymentdetail.BasePaymentDetail.Status.FAILED;
import static truelayer.java.payments.entities.paymentdetail.BasePaymentDetail.Status.SUCCEEDED;

@Getter
@Value
@EqualsAndHashCode(callSuper = true)
public class Failed extends BasePaymentDetail {
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

    @JsonProperty("failed_at")
    private String failedAt;

    @JsonProperty("failure_stage")
    private FailureStage failureStage;

    @JsonProperty("failure_reason")
    private String failureReason;

    @JsonProperty("authorization_flow")
    private Optional<AuthorizationFlow> authorizationFlow;

    @JsonProperty("status")
    private final Status status = FAILED;

    public enum FailureStage {
        AUTHORIZATION_REQUIRED("authorization_required"),
        AUTHORIZING("authorizing"),
        AUTHORIZED("authorized");

        private final String failureStage;

        FailureStage(String failureStage) {
            this.failureStage = failureStage;
        }

        @JsonValue
        public String getFailureStage() {
            return failureStage;
        }
    }
}
