package truelayer.java.payments.entities.paymentmethod.provider;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserSelectionProvider extends BaseProvider {
    private final String type = "user_selection";

    private final ProviderFilter filter;
}
