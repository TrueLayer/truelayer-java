package com.truelayer.java.payments.entities.paymentdetail;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@JsonDeserialize(as = ProviderMetadata.class)
@ToString
@EqualsAndHashCode
@Getter
public abstract class Metadata {
    protected Type type;

    public enum Type {
        PROVIDER("provider");

        Type(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }

        @JsonValue
        private final String type;
    }
}
