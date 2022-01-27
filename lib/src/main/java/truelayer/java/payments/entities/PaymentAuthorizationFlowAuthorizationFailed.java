package truelayer.java.payments.entities;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = false)
public class PaymentAuthorizationFlowAuthorizationFailed extends SubmitProviderSelectionResponse {
    String status = "failed";

    String failureStage;

    String failureReason;
}
