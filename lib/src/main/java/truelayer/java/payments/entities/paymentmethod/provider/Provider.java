package truelayer.java.payments.entities.paymentmethod.provider;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@JsonTypeInfo(
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        use = JsonTypeInfo.Id.NAME,
        property = "type",
        defaultImpl = UserSelectionProvider.class)
@JsonSubTypes({
    @JsonSubTypes.Type(value = UserSelectionProvider.class, name = "user_selection"),
    @JsonSubTypes.Type(value = PreselectedProvider.class, name = "preselected")
})
@ToString
@EqualsAndHashCode
@Getter
public abstract class Provider {
    protected Type type;

    @RequiredArgsConstructor
    @Getter
    public enum Type {
        USER_SELECTION("user_selection"),
        PRESELECTED("preselected");

        @JsonValue
        private final String type;
    }
}
