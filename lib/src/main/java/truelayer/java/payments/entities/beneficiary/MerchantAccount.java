package truelayer.java.payments.entities.beneficiary;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MerchantAccount extends Beneficiary {
    private final String type = "merchant_account";

    private String id;

    private String name;
}
