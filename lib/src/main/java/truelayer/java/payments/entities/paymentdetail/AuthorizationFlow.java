package truelayer.java.payments.entities.paymentdetail;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Value;
import truelayer.java.payments.entities.Actions;

@Getter
@Value
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthorizationFlow {

    Actions actions;

    Configuration configuration;
}
