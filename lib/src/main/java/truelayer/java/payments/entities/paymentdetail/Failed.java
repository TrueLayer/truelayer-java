package truelayer.java.payments.entities.paymentdetail;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;
import truelayer.java.payments.entities.beneficiary.BaseBeneficiary;
import truelayer.java.payments.entities.paymentmethod.BasePaymentMethod;

import java.util.Optional;

import static truelayer.java.payments.entities.paymentdetail.BasePaymentDetail.Status.FAILED;

@Getter
@Value
@EqualsAndHashCode(callSuper = true)
public class Failed extends BasePaymentDetail {
    private String id;

    private int amountInMinor;

    private String currency;

    private BaseBeneficiary beneficiary;

    private User user;

    private BasePaymentMethod paymentMethod;

    private String createdAt;

    private String failedAt;

    private FailureStage failureStage;

    private String failureReason;

    private Optional<AuthorizationFlow> authorizationFlow;

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
