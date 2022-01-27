package truelayer.java.payments.entities.paymentdetail;

import lombok.Value;

@Value
public class Configuration {
    ProviderSelection providerSelection;

    RedirectStatus redirect;

    @Value
    public static class ProviderSelection {
        ConfigurationStatus status;
    }
}
