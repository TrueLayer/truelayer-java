package truelayer.java.payments.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@JsonTypeInfo(
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        use = JsonTypeInfo.Id.NAME,
        property = "type",
        defaultImpl = ProviderSelection.class
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ProviderSelection.class, name = "provider_selection"),
        @JsonSubTypes.Type(value = WaitForOutcome.class, name = "wait"),
        @JsonSubTypes.Type(value = Redirect.class, name = "redirect")
})
public abstract class BaseAuthorizationFlowAction {

}
