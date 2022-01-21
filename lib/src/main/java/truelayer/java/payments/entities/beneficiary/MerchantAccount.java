package truelayer.java.payments.entities.beneficiary;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MerchantAccount extends BaseBeneficiary {
    private final String type = "merchant_account";

    private String id;

    private String name;
}
