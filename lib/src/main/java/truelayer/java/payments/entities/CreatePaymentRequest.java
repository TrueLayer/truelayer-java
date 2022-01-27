package truelayer.java.payments.entities;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import truelayer.java.payments.entities.beneficiary.Beneficiary;
import truelayer.java.payments.entities.paymentmethod.PaymentMethod;

@Builder
@Getter
@ToString
@EqualsAndHashCode
public class CreatePaymentRequest {
    private int amountInMinor;

    private String currency;

    private PaymentMethod paymentMethod;

    private Beneficiary beneficiary;

    private User user;
}
