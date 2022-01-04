package truelayer.java.payments.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
@JsonInclude(Include.NON_NULL)
public class CreatePaymentRequest {

    @JsonProperty("amount_in_minor")
    private int amountInMinor;

    @JsonProperty("currency")
    private String currency;

    @JsonProperty("payment_method")
    private PaymentMethod paymentMethod;

    @JsonProperty("beneficiary")
    private IBaseBeneficiary beneficiary;

    @JsonProperty("user")
    private User user;

    private interface IBaseBeneficiary {
    }

    @Builder
    @Getter
    @JsonInclude(Include.NON_NULL)
    public static class PaymentMethod {
        private final String type = "bank_transfer";

        @JsonProperty("statement_reference")
        private String statementReference;

        @JsonProperty("provider_filter")
        private ProviderFilter providerFilter;

        @Builder
        @JsonInclude(Include.NON_NULL)
        public static class ProviderFilter {
            @JsonProperty("countries")
            private List<String> countries;

            @JsonProperty("customer_segments")
            private List<CustomerSegment> customerSegments;

            @JsonProperty("release_channel")
            private ReleaseChannel releaseChannel;

            @JsonProperty("provider_ids")
            private List<String> providerIds;

            @JsonProperty("excludes")
            private Excludes excludes;

            public enum CustomerSegment {
                RETAIL("retail"),
                BUSINESS("business"),
                CORPORATE("corporate");

                private final String customerSegment;

                CustomerSegment(String type) {
                    this.customerSegment = type;
                }

                @JsonValue
                public String getCustomerSegment() {
                    return customerSegment;
                }
            }

            public enum ReleaseChannel {
                GENERAL_AVAILABILITY("general_availability"),
                PUBLIC_BETA("public_beta"),
                PRIVATE_BETA("private_beta");

                private final String releaseChannel;

                ReleaseChannel(String releaseChannel) {
                    this.releaseChannel = releaseChannel;
                }

                @JsonValue
                public String getReleaseChannel() {
                    return releaseChannel;
                }
            }

            @Builder
            @JsonInclude(Include.NON_NULL)
            public static class Excludes {
                @JsonProperty("provider_ids")
                private List<String> providerIds;
            }
        }
    }

    @Builder
    @Getter
    @JsonInclude(Include.NON_NULL)
    public static class MerchantAccount implements IBaseBeneficiary {
        @JsonProperty("type")
        private final String type = "merchant_account";

        @JsonProperty("id")
        private String id;

        @JsonProperty("name")
        private String name;
    }

    @Builder
    @Getter
    @JsonInclude(Include.NON_NULL)
    public static class ExternalAccount implements IBaseBeneficiary {
        @JsonProperty("type")
        private final String type = "external_account";

        @JsonProperty("name")
        private String name;

        @JsonProperty("reference")
        private String reference;

        @JsonProperty("scheme_identifier")
        private SchemeIdentifier schemeIdentifier;

        public static class SchemeIdentifier {
            @JsonProperty("type")
            private final String type = "sort_code_account_number";

            @JsonProperty("sort_code")
            private String sortCode;

            @JsonProperty("account_number")
            private String accountNumber;
        }
    }

    @Builder
    @Getter
    @JsonInclude(Include.NON_NULL)
    public static class User {
        @JsonProperty("type")
        private Type type;

        @JsonProperty("name")
        private String name;

        @JsonProperty("email")
        private String email;

        @JsonProperty("phone")
        private String phone;

        public enum Type {
            EXISTING("existing"),
            NEW("new");

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
