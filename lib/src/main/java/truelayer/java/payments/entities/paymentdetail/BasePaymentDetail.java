package truelayer.java.payments.entities.paymentdetail;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;
import truelayer.java.TrueLayerException;
import truelayer.java.payments.entities.beneficiary.ExternalAccount;
import truelayer.java.payments.entities.beneficiary.MerchantAccount;
import truelayer.java.payments.entities.paymentmethod.BankTransfer;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@JsonTypeInfo(
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        use = JsonTypeInfo.Id.NAME,
        property = "status",
        defaultImpl = AuthorizationRequired.class
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = AuthorizationRequired.class, name = "authorization_required")
})
@Getter
@ToString
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class BasePaymentDetail {

    @JsonIgnore
    public boolean isAuthorized(){
        return this instanceof Authorized;
    }

    @JsonIgnore
    public boolean isAuthorizing(){
        return this instanceof Authorizing;
    }

    @JsonIgnore
    public boolean isFailed(){
        return this instanceof Failed;
    }

    @JsonIgnore
    public boolean isSucceeded(){
        return this instanceof Succeeded;
    }

    @JsonIgnore
    public boolean isSettled(){
        return this instanceof Settled;
    }

    public Authorized asAuthorized(){
        if(!isAuthorized()){
            throw new TrueLayerException(buildErrorMessage());
        }
        return (Authorized) this;
    }

    public Authorizing asAuthorizing(){
        if(!isAuthorizing()){
            throw new TrueLayerException(buildErrorMessage());
        }
        return (Authorizing) this;
    }

    public Failed asFailed(){
        if(!isFailed()){
            throw new TrueLayerException(buildErrorMessage());
        }
        return (Failed) this;
    }

    public Succeeded asSucceeded(){
        if(!isSucceeded()){
            throw new TrueLayerException(buildErrorMessage());
        }
        return (Succeeded) this;
    }

    public Settled asSettled(){
        if(!isSettled()){
            throw new TrueLayerException(buildErrorMessage());
        }
        return (Settled) this;
    }

    private String buildErrorMessage(){
        return String.format("payment is of type %1$s. Consider using as%1$s() instead.", this.getClass().getSimpleName());
    }

    public enum Status {
        AUTHORIZATION_REQUIRED("authorization_required"),
        AUTHORIZING("authorizing"),
        AUTHORIZED("authorized"),
        SUCCEEDED("succeeded"),
        FAILED("failed"),
        SETTLED("settled");

        private final String status;

        Status(String status) {
            this.status = status;
        }

        @JsonValue
        public String getStatus() {
            return status;
        }
    }

    @Value
    @Getter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class User {
        @JsonProperty("id")
        private String id;

        @JsonProperty("name")
        private String name;

        @JsonProperty("email")
        private Optional<String> email;

        @JsonProperty("phone")
        private Optional<String> phone;
    }

    @Getter
    @Value
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class AuthorizationFlow {

        @JsonProperty("configuration")
        private Configuration configuration;

        @Getter
        @Value
        @JsonInclude(JsonInclude.Include.NON_NULL)
        public static class Configuration {
            @JsonProperty("provider_selection")
            private ProviderSelection providerSelection;

            @JsonProperty("redirect")
            private Redirect redirect;

            @Value
            @JsonInclude(JsonInclude.Include.NON_NULL)
            public static class ProviderSelection {
                private Status status;
            }

            @Value
            @JsonInclude(JsonInclude.Include.NON_NULL)
            public static class Redirect {
                private Status status;
            }

            public enum Status {
                SUPPORTED("supported"),
                NOT_SUPPORTED("not_supported");

                private final String status;

                Status(String status) {
                    this.status = status;
                }

                @JsonValue
                public String getStatus() {
                    return status;
                }
            }
        }
    }

    @JsonTypeInfo(
            include = JsonTypeInfo.As.EXISTING_PROPERTY,
            use = JsonTypeInfo.Id.NAME,
            property = "type",
            defaultImpl = BaseAuthorizationFlowAction.ProviderSelection.class
    )
    @JsonSubTypes({
            @JsonSubTypes.Type(value = BaseAuthorizationFlowAction.ProviderSelection.class, name = "provider_selection"),
            @JsonSubTypes.Type(value = BaseAuthorizationFlowAction.WaitForOutcome.class, name = "wait"),
            @JsonSubTypes.Type(value = BaseAuthorizationFlowAction.Redirect.class, name = "redirect")
    })
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @ToString
    @EqualsAndHashCode
    public static abstract class BaseAuthorizationFlowAction {

        @JsonIgnore
        public boolean isProviderSelection(){
            return this instanceof ProviderSelection;
        }

        @JsonIgnore
        public boolean isWaitForOutcome(){
            return this instanceof WaitForOutcome;
        }

        @JsonIgnore
        public boolean isRedirect(){
            return this instanceof Redirect;
        }

        public ProviderSelection asProviderSelection(){
            if(!isProviderSelection()){
                throw new TrueLayerException(buildErrorMessage());
            }
            return (ProviderSelection) this;
        }

        public WaitForOutcome asWaitForOutcome(){
            if(!isWaitForOutcome()){
                throw new TrueLayerException(buildErrorMessage());
            }
            return (WaitForOutcome) this;
        }

        public Redirect asRedirect(){
            if(!isRedirect()){
                throw new TrueLayerException(buildErrorMessage());
            }
            return (Redirect) this;
        }

        private String buildErrorMessage(){
            return String.format("authorization flow is of type %1$s. Consider using as%1$s() instead.", this.getClass().getSimpleName());
        }

        @Value
        @EqualsAndHashCode(callSuper = true)
        @JsonInclude(JsonInclude.Include.NON_NULL)
        public static class ProviderSelection extends BaseAuthorizationFlowAction {
            @JsonProperty("type")
            private String type = "provider_selection";

            @JsonProperty("providers")
            private List<ProviderSelection.Provider> providers;

            @Value
            @JsonInclude(JsonInclude.Include.NON_NULL)
            public static class Provider {
                @JsonProperty("provider_id")
                private String providerId;

                @JsonProperty("display_name")
                private String displayName;

                @JsonProperty("icon_uri")
                private String iconUri;

                @JsonProperty("logo_uri")
                private String logoUri;

                @JsonProperty("bg_color")
                private String bgColor;

                @JsonProperty("country_code")
                private String countryCode;
            }
        }

        @Value
        @EqualsAndHashCode(callSuper = true)
        @JsonInclude(JsonInclude.Include.NON_NULL)
        public static class Redirect extends BaseAuthorizationFlowAction{
            @JsonProperty("type")
            private final String type = "redirect";

            @JsonProperty("uri")
            private final String uri;

            // todo: metadata
        }

        @Value
        @EqualsAndHashCode(callSuper = true)
        @JsonInclude(JsonInclude.Include.NON_NULL)
        public static class WaitForOutcome extends BaseAuthorizationFlowAction {
            @JsonProperty("type")
            private final String type = "wait";
        }
    }

    @JsonDeserialize(as = SourceOfFunds.ExternalAccount.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static abstract class SourceOfFunds {

        public ExternalAccount asExternalAccount(){
            return (ExternalAccount) this;
        }

        @Value
        @EqualsAndHashCode(callSuper = true)
        @JsonInclude(JsonInclude.Include.NON_NULL)
        public static class ExternalAccount extends SourceOfFunds{
            @JsonProperty("type")
            private String type = "external_account";

            @JsonProperty("scheme_identifiers")
            private List<SchemeIdentifier> schemeIdentifiers;

            @JsonProperty("external_account_id")
            private String externalAccountId;

            @JsonProperty("account_holder_name")
            private String accountHolderName;

            public static class SchemeIdentifier {
                @JsonProperty("type")
                private Type type;

                @JsonProperty("sort_code")
                private String sortCode;

                @JsonProperty("account_number")
                private String accountNumber;

                public enum Type {
                    NRB("nrb"),
                    BBAN("bban"),
                    IBAN("iban"),
                    SORT_CODE_ACCOUNT_NUMBER("sort_code_account_number");

                    private final String type;

                    Type(String type) {
                        this.type = type;
                    }

                    @JsonValue
                    public String getType() {
                        return type;
                    }
                }
            }
        }
    }
}
