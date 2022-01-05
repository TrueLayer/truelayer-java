package truelayer.java.payments.entities;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.Optional;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type",
        defaultImpl = MerchantAccount.class
)
@JsonSubTypes({
        //this is instructing Jackson to deserialize into a MerchantAccount or ExternalAccount type
        //based on the value coming in the beneficiary JSON object with key "type"
        @JsonSubTypes.Type(value = MerchantAccount.class, name = "merchant_account"),
        @JsonSubTypes.Type(value = ExternalAccount.class, name = "external_account")
})
public abstract class BaseBeneficiary {
    public boolean isMerchantAccount(){
        return this instanceof MerchantAccount;
    }

    public boolean isExternalAccount(){
        return this instanceof ExternalAccount;
    }

    public Optional<MerchantAccount> merchantAccount(){
        if(isMerchantAccount()){
            return Optional.of((MerchantAccount) this);
        }
        return Optional.empty();
    }

    public Optional<ExternalAccount> externalAccount(){
        if(isExternalAccount()){
            return Optional.of((ExternalAccount) this);
        }
        return Optional.empty();
    }
}
