package com.truelayer.java.payments.entities.paymentdetail;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonValue;
import com.truelayer.java.TrueLayerException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

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
@ToString
@EqualsAndHashCode
@Getter
public abstract class AuthorizationFlowAction {

    protected Type type;

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
                "Authorization flow is of type %s.", this.getClass().getSimpleName());
    }

    @RequiredArgsConstructor
    @Getter
    public enum Type {
        PROVIDER_SELECTION("provider_selection"),
        REDIRECT("redirect"),
        WAIT("wait");

        @JsonValue
        private final String type;
    }
}
