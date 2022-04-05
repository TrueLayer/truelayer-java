package com.truelayer.java.merchantaccounts.entities.sweeping;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@JsonDeserialize(as = Iban.class)
@ToString
@EqualsAndHashCode
@Getter
public abstract class Destination {

    @JsonIgnore
    public Iban asIban() {
        return (Iban) this;
    }

    @RequiredArgsConstructor
    @Getter
    public enum Type {
        IBAN("iban");

        @JsonValue
        private final String type;
    }
}
