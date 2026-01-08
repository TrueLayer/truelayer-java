package com.truelayer.java.payments.entities.userconsent;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = AuthorizationFlowCapturedConsent.class, name = "authorization_flow_captured"),
    @JsonSubTypes.Type(value = PrecapturedConsent.class, name = "precaptured")
})
@ToString
@EqualsAndHashCode
@Getter
public abstract class UserConsent {

    @Getter
    @RequiredArgsConstructor
    public enum Type {
        PRECAPTURED("precaptured"),
        AUTHORIZATION_FLOW_CAPTURED("authorization_flow_captured");

        @JsonValue
        private final String type;
    }
}
