package truelayer.java.payments.entities.paymentdetail;

import static truelayer.java.payments.entities.paymentdetail.Status.AUTHORIZATION_REQUIRED;

import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;

@NoArgsConstructor
@Getter
public class AuthorizationRequiredPaymentDetail extends BasePaymentDetail {

    private final Status status = AUTHORIZATION_REQUIRED;
}
