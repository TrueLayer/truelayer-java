package com.truelayer.java.payments.entities.paymentdetail.forminput;

import com.fasterxml.jackson.annotation.*;
import com.truelayer.java.TrueLayerException;
import com.truelayer.java.payments.entities.paymentdetail.AuthorizedPaymentDetail;
import com.truelayer.java.payments.entities.paymentdetail.Consent;
import lombok.*;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Text.class, name = "text"),
        @JsonSubTypes.Type(value = TextWithImage.class, name = "text_with_image"),
        @JsonSubTypes.Type(value = Select.class, name = "select"),
})
@EqualsAndHashCode
@ToString
@Getter
public abstract class Input {
    protected Type type;

    String id;
    boolean mandatory;
    DisplayText displayText;
    String description;

    @JsonIgnore
    public boolean isText() {
        return this instanceof Text;
    }

    @JsonIgnore
    public boolean isTextWithImage() {
        return this instanceof TextWithImage;
    }

    @JsonIgnore
    public boolean isSelect() {
        return this instanceof Select;
    }

    @JsonIgnore
    public Text asText() {
        if (!isText()) {
            throw new TrueLayerException(buildErrorMessage());
        }
        return (Text) this;
    }

    @JsonIgnore
    public TextWithImage asTextWithImage() {
        if (!isTextWithImage()) {
            throw new TrueLayerException(buildErrorMessage());
        }
        return (TextWithImage) this;
    }

    @JsonIgnore
    public Select asSelect() {
        if (!isSelect()) {
            throw new TrueLayerException(buildErrorMessage());
        }
        return (Select) this;
    }

    private String buildErrorMessage() {
        return String.format(
                "Input is of type %s.", this.getClass().getSimpleName());
    }

    @RequiredArgsConstructor
    @Getter
    public enum Type {
        TEXT("text"),
        TEXT_WITH_IMAGE("text_with_image"),
        SELECT("select");

        @JsonValue
        private final String type;
    }

    @Value
    @EqualsAndHashCode
    public static class DisplayText {
        String key;

        @JsonAlias("default")
        String default_value;
    }
}
