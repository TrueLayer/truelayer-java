package truelayer.java.payments.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Redirect extends BaseAuthorizationFlowAction{
    @JsonProperty("type")
    private final String type = "redirect";

    @JsonProperty("uri")
    private final String uri;

    //todo metadata
}
