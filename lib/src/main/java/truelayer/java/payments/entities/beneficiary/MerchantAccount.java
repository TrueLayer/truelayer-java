package truelayer.java.payments.entities.beneficiary;

import lombok.Builder;
import lombok.Getter;

import static truelayer.java.payments.entities.beneficiary.Beneficiary.Type.MERCHANT_ACCOUNT;

@Builder
@Getter
public class MerchantAccount extends Beneficiary {
    private final Type type = MERCHANT_ACCOUNT;

    private String id;

    private String name;
}
