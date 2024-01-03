package com.truelayer.java.commonapi.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonValue;
import com.truelayer.java.TrueLayerException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", defaultImpl = PaymentProviderReturnResource.class)
@JsonSubTypes({
    @JsonSubTypes.Type(value = PaymentProviderReturnResource.class, name = "payment"),
    @JsonSubTypes.Type(value = MandateProviderReturnResource.class, name = "mandate")
})
@Getter
public abstract class ProviderReturnResource {
    String type;

    String paymentId;

    @JsonIgnore
    public abstract Type getType();

    @JsonIgnore
    public boolean isPaymentProviderReturnResponse() {
        return this instanceof PaymentProviderReturnResource;
    }

    @JsonIgnore
    public boolean isMandateProviderReturnResponse() {
        return this instanceof MandateProviderReturnResource;
    }

    @JsonIgnore
    public PaymentProviderReturnResource asPaymentProviderReturnResponse() {
        if (!isPaymentProviderReturnResponse()) {
            throw new TrueLayerException(buildErrorMessage());
        }
        return (PaymentProviderReturnResource) this;
    }

    @JsonIgnore
    public MandateProviderReturnResource asMandateProviderReturnResponse() {
        if (!isMandateProviderReturnResponse()) {
            throw new TrueLayerException(buildErrorMessage());
        }
        return (MandateProviderReturnResource) this;
    }

    private String buildErrorMessage() {
        return String.format(
                "Provider return resource is of type %s.", this.getClass().getSimpleName());
    }

    @RequiredArgsConstructor
    @Getter
    public enum Type {
        PAYMENT("payment"),
        MANDATE("mandate");

        @JsonValue
        private final String type;
    }
}
