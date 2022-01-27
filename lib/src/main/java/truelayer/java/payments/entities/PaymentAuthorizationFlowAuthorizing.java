package truelayer.java.payments.entities;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = false)
public class PaymentAuthorizationFlowAuthorizing extends SubmitProviderSelectionResponse {
    String status = "authorizing";
}
