package com.truelayer.java.payments.entities.submerchants;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonValue;
import com.truelayer.java.TrueLayerException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", defaultImpl = BusinessClient.class)
@JsonSubTypes({
    @JsonSubTypes.Type(value = BusinessClient.class, name = "business_client"),
    @JsonSubTypes.Type(value = BusinessDivision.class, name = "business_division")
})
public abstract class UltimateCounterparty {

    @JsonIgnore
    public abstract Type getType();

    @JsonIgnore
    public boolean isBusinessClient() {
        return this instanceof BusinessClient;
    }

    @JsonIgnore
    public boolean isBusinessDivision() {
        return this instanceof BusinessDivision;
    }

    @JsonIgnore
    public BusinessClient asBusinessClient() {
        if (!isBusinessClient()) throw new TrueLayerException(buildErrorMessage());
        return (BusinessClient) this;
    }

    @JsonIgnore
    public BusinessDivision asBusinessDivision() {
        if (!isBusinessDivision()) throw new TrueLayerException(buildErrorMessage());
        return (BusinessDivision) this;
    }

    public static BusinessClient.BusinessClientBuilder businessClient() {
        return new BusinessClient.BusinessClientBuilder();
    }

    public static BusinessDivision.BusinessDivisionBuilder businessDivision() {
        return new BusinessDivision.BusinessDivisionBuilder();
    }

    private String buildErrorMessage() {
        return String.format(
                "UltimateCounterparty is of type %s.", this.getClass().getSimpleName());
    }

    @Getter
    @RequiredArgsConstructor
    public enum Type {
        BUSINESS_CLIENT("business_client"),
        BUSINESS_DIVISION("business_division");

        @JsonValue
        private final String type;
    }
}
