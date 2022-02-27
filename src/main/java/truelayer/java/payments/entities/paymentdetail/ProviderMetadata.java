package truelayer.java.payments.entities.paymentdetail;

import static truelayer.java.payments.entities.paymentdetail.Metadata.Type.PROVIDER;

import java.net.URI;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = false)
public class ProviderMetadata extends Metadata {
    Type type = PROVIDER;

    String providerId;

    String displayName;

    URI iconUri;

    URI logoUri;

    String bgColor;

    String countryCode;
}
