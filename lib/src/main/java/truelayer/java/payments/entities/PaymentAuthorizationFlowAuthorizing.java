package truelayer.java.payments.entities;

import static truelayer.java.payments.entities.SubmitProviderSelectionResponse.Status.AUTHORIZING;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = false)
public class PaymentAuthorizationFlowAuthorizing extends SubmitProviderSelectionResponse {
    Status status = AUTHORIZING;
}
