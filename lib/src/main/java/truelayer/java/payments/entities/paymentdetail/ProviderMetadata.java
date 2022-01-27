package truelayer.java.payments.entities.paymentdetail;

import static truelayer.java.payments.entities.paymentdetail.Metadata.Type.PROVIDER;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = false)
public class ProviderMetadata extends Metadata {
    Type type = PROVIDER;

    String providerId;

    String displayName;

    String iconUri;

    String logoUri;

    String bgColor;

    String countryCode;
}
