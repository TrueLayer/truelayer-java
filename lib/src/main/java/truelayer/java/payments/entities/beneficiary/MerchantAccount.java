package truelayer.java.payments.entities.beneficiary;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MerchantAccount extends BaseBeneficiary {
    @JsonProperty("type")
    private final String type = "merchant_account";

    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;
}