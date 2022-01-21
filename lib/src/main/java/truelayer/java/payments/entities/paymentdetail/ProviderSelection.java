package truelayer.java.payments.entities.paymentdetail;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProviderSelection extends BaseAuthorizationFlowAction {
    private String type = "provider_selection";

    private List<Provider> providers;
}
