package truelayer.java.payments.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BankTransfer extends BasePaymentMethod {
    @JsonProperty("type")
    private final String type = "bank_transfer";

    @JsonProperty("statement_reference")
    private String statementReference;

    @JsonProperty("provider_filter")
    private ProviderFilter providerFilter;

    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
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
        @JsonInclude(JsonInclude.Include.NON_NULL)
        public static class Excludes {
            @JsonProperty("provider_ids")
            private List<String> providerIds;
        }
    }
}