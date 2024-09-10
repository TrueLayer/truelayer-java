package com.truelayer.java.payments.entities.verification;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonValue;
import com.truelayer.java.TrueLayerException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", defaultImpl = AutomatedVerification.class)
@JsonSubTypes({@JsonSubTypes.Type(value = AutomatedVerification.class, name = "automated")})
@ToString
@EqualsAndHashCode
@Getter
public abstract class Verification {
    @JsonIgnore
    public abstract Verification.Type getType();

    @JsonIgnore
    public boolean isAutomated() {
        return this instanceof AutomatedVerification;
    }

    @JsonIgnore
    public AutomatedVerification asAutomated() {
        if (!isAutomated()) {
            throw new TrueLayerException(
                    String.format("Verification is of type %s.", this.getClass().getSimpleName()));
        }
        return (AutomatedVerification) this;
    }

    @Getter
    @RequiredArgsConstructor
    public enum Type {
        AUTOMATED("automated");

        @JsonValue
        private final String type;
    }
}
