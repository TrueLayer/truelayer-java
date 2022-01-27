package truelayer.java.payments.entities.paymentdetail;

import static truelayer.java.payments.entities.paymentdetail.Status.AUTHORIZATION_REQUIRED;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Value
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthorizationRequiredPaymentDetail extends PaymentDetail {

    Status status = AUTHORIZATION_REQUIRED;
}
