package truelayer.java.payments.entities.paymentmethod;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.List;
import java.util.Optional;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
public class BankTransfer extends BasePaymentMethod {
    private final String type = "bank_transfer";

    private Optional<ProviderFilter> providerFilter;

    @Builder
    @ToString
    @EqualsAndHashCode
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ProviderFilter {
        private List<String> countries;

        private List<CustomerSegment> customerSegments;

        private ReleaseChannel releaseChannel;

        private List<String> providerIds;

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
        @ToString
        @EqualsAndHashCode
        @JsonInclude(JsonInclude.Include.NON_NULL)
        public static class Excludes {
            private List<String> providerIds;
        }
    }
}
