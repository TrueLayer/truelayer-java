package truelayer.java.payments.entities.paymentdetail;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Redirect extends BaseAuthorizationFlowAction {
    private final String type = "redirect";

    private final String uri;

    // todo: metadata
}