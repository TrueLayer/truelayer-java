package truelayer.java.payments.entities;

import static truelayer.java.payments.entities.SubmitProviderSelectionResponse.Status.FAILED;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = false)
public class PaymentAuthorizationFlowAuthorizationFailed extends SubmitProviderSelectionResponse {
    Status status = FAILED;

    String failureStage;

    String failureReason;
}
