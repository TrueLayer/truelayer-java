package truelayer.java.payments.entities.paymentdetail;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = false)
public class NotSupportedRedirectStatus extends RedirectStatus {
    String type = "not_supported";
}
