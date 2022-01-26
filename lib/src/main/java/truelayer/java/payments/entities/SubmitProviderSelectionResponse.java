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
        property = "status",
        defaultImpl = PaymentAuthorizationFlowAuthorizing.class)
@JsonSubTypes({
    @JsonSubTypes.Type(value = PaymentAuthorizationFlowAuthorizing.class, name = "authorizing"),
    @JsonSubTypes.Type(value = PaymentAuthorizationFlowAuthorizationFailed.class, name = "failed")
})
@Getter
@ToString
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class SubmitProviderSelectionResponse {

    protected String status;

    AuthorizationFlow authorizationFlow;
}
