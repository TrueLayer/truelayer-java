package truelayer.java.payments.entities.paymentmethod.provider;

import java.util.List;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import truelayer.java.payments.entities.CountryCode;
import truelayer.java.payments.entities.CustomerSegment;
import truelayer.java.payments.entities.ReleaseChannel;

@Builder
@Getter
@ToString
@EqualsAndHashCode
public class ProviderFilter {
    private List<CountryCode> countries;

    private ReleaseChannel releaseChannel;

    private List<CustomerSegment> customerSegments;

    private List<String> providerIds;

    private Excludes excludes;

    @Builder
    @Getter
    @ToString
    @EqualsAndHashCode
    public static class Excludes {

        private List<String> providerIds;
    }
}
