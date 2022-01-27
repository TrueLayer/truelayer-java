package truelayer.java.payments.entities.paymentdetail;

import static truelayer.java.payments.entities.paymentdetail.RedirectStatus.Type.SUPPORTED;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = false)
public class SupportedRedirectStatus extends RedirectStatus {
    Type type = SUPPORTED;

    String returnUri;
}
