package com.truelayer.java.payouts.entities.submerchants;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonValue;
import com.truelayer.java.TrueLayerException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", defaultImpl = BusinessClient.class)
@JsonSubTypes({@JsonSubTypes.Type(value = BusinessClient.class, name = "business_client")})
public abstract class UltimateCounterparty {

    @JsonIgnore
    public abstract Type getType();

    @JsonIgnore
    public boolean isBusinessClient() {
        return this instanceof BusinessClient;
    }

    @JsonIgnore
    public BusinessClient asBusinessClient() {
        if (!isBusinessClient()) throw new TrueLayerException(buildErrorMessage());
        return (BusinessClient) this;
    }

    public static BusinessClient.BusinessClientBuilder businessClient() {
        return new BusinessClient.BusinessClientBuilder();
    }

    private String buildErrorMessage() {
        return String.format(
                "UltimateCounterparty is of type %s.", this.getClass().getSimpleName());
    }

    @Getter
    @RequiredArgsConstructor
    public enum Type {
        BUSINESS_CLIENT("business_client");

        @JsonValue
        private final String type;
    }
}
