package truelayer.java.payments.entities.paymentdetail;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonValue;
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

    @RequiredArgsConstructor
    @Getter
    public enum Type {
        NOT_SUPPORTED("not_supported"),
        SUPPORTED("supported");

        @JsonValue
        private final String type;
    }
}
