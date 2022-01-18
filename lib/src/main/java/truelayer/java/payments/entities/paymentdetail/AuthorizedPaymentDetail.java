package truelayer.java.payments.entities.paymentdetail;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Optional;

import static truelayer.java.payments.entities.paymentdetail.Status.AUTHORIZED;

@NoArgsConstructor
@Getter
public class AuthorizedPaymentDetail extends BasePaymentDetail {

    private final Status status = AUTHORIZED;

    Optional<AuthorizationFlow> authorizationFlow;
}
