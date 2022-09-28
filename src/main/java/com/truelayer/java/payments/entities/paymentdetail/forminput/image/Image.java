package com.truelayer.java.payments.entities.paymentdetail.forminput.image;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonValue;
import com.truelayer.java.TrueLayerException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = Base64.class, name = "base64"),
    @JsonSubTypes.Type(value = Uri.class, name = "uri"),
})
@EqualsAndHashCode
@ToString
@Getter
public abstract class Image {
    protected Type type;

    @JsonIgnore
    public boolean isBase64() {
        return this instanceof Base64;
    }

    @JsonIgnore
    public boolean isUri() {
        return this instanceof Uri;
    }

    @JsonIgnore
    public Base64 asBase64() {
        if (!isBase64()) {
            throw new TrueLayerException(buildErrorMessage());
        }
        return (Base64) this;
    }

    @JsonIgnore
    public Uri asUri() {
        if (!isUri()) {
            throw new TrueLayerException(buildErrorMessage());
        }
        return (Uri) this;
    }

    private String buildErrorMessage() {
        return String.format("Image is of type %s.", this.getClass().getSimpleName());
    }

    @RequiredArgsConstructor
    @Getter
    public enum Type {
        BASE_64("base64"),
        URI("uri");

        @JsonValue
        private final String type;
    }
}
