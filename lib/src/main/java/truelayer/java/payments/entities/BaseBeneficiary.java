package truelayer.java.payments.entities;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type",
        defaultImpl = MerchantAccount.class
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = MerchantAccount.class, name = "merchant_account"),
        @JsonSubTypes.Type(value = ExternalAccount.class, name = "external_account")
})
interface BaseBeneficiary {
}
