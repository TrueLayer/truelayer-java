package truelayer.java.payments.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentAuthorizationFlowAuthorizationFailed extends SubmitProviderSelectionResponse {
    String status = "failed";

    String failureStage;

    String failureReason;
}
