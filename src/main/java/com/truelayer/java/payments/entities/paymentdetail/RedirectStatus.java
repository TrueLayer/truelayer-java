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

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "status", defaultImpl = SupportedRedirectStatus.class)
@JsonSubTypes({
    @JsonSubTypes.Type(value = SupportedRedirectStatus.class, name = "supported"),
    @JsonSubTypes.Type(value = NotSupportedRedirectStatus.class, name = "not_supported"),
})
@EqualsAndHashCode
@ToString
@Getter
public abstract class RedirectStatus {

    protected Type type;

    @JsonIgnore
    public boolean isSupported() {
        return this instanceof SupportedRedirectStatus;
    }

    @JsonIgnore
    public SupportedRedirectStatus asSupported() {
        if (!isSupported()) {
            throw new TrueLayerException("Redirect is not supported.");
        }
        return (SupportedRedirectStatus) this;
    }

    @RequiredArgsConstructor
    @Getter
    public enum Type {
        NOT_SUPPORTED("not_supported"),
        SUPPORTED("supported");

        @JsonValue
        private final String type;
    }
}
