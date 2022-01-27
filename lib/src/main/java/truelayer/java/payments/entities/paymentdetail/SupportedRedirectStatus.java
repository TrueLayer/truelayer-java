package truelayer.java.payments.entities.paymentdetail;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = false)
public class SupportedRedirectStatus extends RedirectStatus {
    String type = "supported";

    String returnUri;
}
