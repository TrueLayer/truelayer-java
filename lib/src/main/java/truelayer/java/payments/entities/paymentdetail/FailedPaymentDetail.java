package truelayer.java.payments.entities.paymentdetail;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Optional;

@NoArgsConstructor
@Getter
public class FailedPaymentDetail extends BasePaymentDetail {

    private final Status status = Status.FAILED;
    String failedAt;
    FailureStage failureStage;
    String failureReason;
    Optional<AuthorizationFlow> authorizationFlow;

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
