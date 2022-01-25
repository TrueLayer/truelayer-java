package truelayer.java.payments.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Value;
import truelayer.java.payments.entities.paymentdetail.BaseAuthorizationFlowAction;

@Value
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Actions {

    private BaseAuthorizationFlowAction next;
}