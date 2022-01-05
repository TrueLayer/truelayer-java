package truelayer.java.payments.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MerchantAccount extends BaseBeneficiary {
    @JsonProperty("type")
    private final String type = "merchant_account";

    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;
}