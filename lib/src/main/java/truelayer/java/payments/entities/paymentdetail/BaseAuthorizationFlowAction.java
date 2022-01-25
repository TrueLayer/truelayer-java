package truelayer.java.payments.entities.paymentdetail;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import truelayer.java.TrueLayerException;

@JsonTypeInfo(
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        use = JsonTypeInfo.Id.NAME,
        property = "type",
        defaultImpl = ProviderSelection.class)
@JsonSubTypes({
    @JsonSubTypes.Type(value = ProviderSelection.class, name = "provider_selection"),
    @JsonSubTypes.Type(value = WaitForOutcome.class, name = "wait"),
    @JsonSubTypes.Type(value = Redirect.class, name = "redirect")
})
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString
@Getter
@EqualsAndHashCode
public abstract class BaseAuthorizationFlowAction {

    protected String type;

    @JsonIgnore
    public boolean isProviderSelection() {
        return this instanceof ProviderSelection;
    }

    @JsonIgnore
    public boolean isWaitForOutcome() {
        return this instanceof WaitForOutcome;
    }

    @JsonIgnore
    public boolean isRedirect() {
        return this instanceof Redirect;
    }

    public ProviderSelection asProviderSelection() {
        if (!isProviderSelection()) {
            throw new TrueLayerException(buildErrorMessage());
        }
        return (ProviderSelection) this;
    }

    public WaitForOutcome asWaitForOutcome() {
        if (!isWaitForOutcome()) {
            throw new TrueLayerException(buildErrorMessage());
        }
        return (WaitForOutcome) this;
    }

    public Redirect asRedirect() {
        if (!isRedirect()) {
            throw new TrueLayerException(buildErrorMessage());
        }
        return (Redirect) this;
    }

    private String buildErrorMessage() {
        return String.format(
                "authorization flow is of type %1$s. Consider using as%1$s() instead.",
                this.getClass().getSimpleName());
    }
}
