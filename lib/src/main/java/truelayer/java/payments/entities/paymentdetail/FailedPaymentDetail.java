package truelayer.java.payments.entities.paymentdetail;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Date;
import java.util.Optional;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FailedPaymentDetail extends PaymentDetail {

    private final Status status = Status.FAILED;

    private Date failedAt;

    private FailureStage failureStage;

    private String failureReason;

    private Optional<AuthorizationFlowWithConfiguration> authorizationFlow;

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
