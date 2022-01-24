package truelayer.java.payments.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import truelayer.java.payments.entities.beneficiary.BaseBeneficiary;
import truelayer.java.payments.entities.paymentmethod.BasePaymentMethod;

@Builder
@Getter
@ToString
@EqualsAndHashCode
@JsonInclude(Include.NON_NULL)
public class CreatePaymentRequest {
    private int amountInMinor;

    private String currency;

    private BasePaymentMethod paymentMethod;

    private BaseBeneficiary beneficiary;

    private User user;
}
