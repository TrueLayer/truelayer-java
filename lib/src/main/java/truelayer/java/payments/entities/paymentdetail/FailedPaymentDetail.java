package truelayer.java.payments.entities.paymentdetail;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import truelayer.java.payments.entities.beneficiary.BaseBeneficiary;
import truelayer.java.payments.entities.paymentmethod.BasePaymentMethod;

import java.util.Optional;

@NoArgsConstructor
@Getter
public class FailedPaymentDetail extends BasePaymentDetail {

    String failedAt;

    FailureStage failureStage;

    String failureReason;

    Optional<AuthorizationFlow> authorizationFlow;

    private final Status status = Status.FAILED;

    public FailedPaymentDetail(
            String id,
            int amountInMinor,
            String currency,
            BaseBeneficiary beneficiary,
            User user,
            BasePaymentMethod paymentMethod,
            String createdAt,
            String failedAt,
            FailureStage failureStage,
            String failureReason,
            AuthorizationFlow authorizationFlow
    ) {
        super(id, amountInMinor, currency, beneficiary, user, paymentMethod, createdAt);
        this.failedAt = failedAt;
        this.failureStage = failureStage;
        this.failureReason = failureReason;
        this.authorizationFlow = Optional.ofNullable(authorizationFlow);
    }


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
