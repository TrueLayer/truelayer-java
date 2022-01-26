package truelayer.java.payments.entities.paymentdetail;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Value;

@Value
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Configuration {
    ProviderSelection providerSelection;

    RedirectStatus redirect;

    @Value
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ProviderSelection {
        ConfigurationStatus status;
    }
}
