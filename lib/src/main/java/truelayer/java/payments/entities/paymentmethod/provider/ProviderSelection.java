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
        defaultImpl = UserSelectedProviderSelection.class)
@JsonSubTypes({
    @JsonSubTypes.Type(value = UserSelectedProviderSelection.class, name = "user_selected"),
    @JsonSubTypes.Type(value = PreselectedProviderSelection.class, name = "preselected")
})
@ToString
@EqualsAndHashCode
@Getter
public abstract class ProviderSelection {
    protected Type type;

    public static UserSelectedProviderSelection.UserSelectedProviderSelectionBuilder newUserSelected() {
        return new UserSelectedProviderSelection.UserSelectedProviderSelectionBuilder();
    }

    public static PreselectedProviderSelection.PreselectedProviderSelectionBuilder newPreselected() {
        return new PreselectedProviderSelection.PreselectedProviderSelectionBuilder();
    }

    @RequiredArgsConstructor
    @Getter
    public enum Type {
        USER_SELECTED("user_selected"),
        PRESELECTED("preselected");

        @JsonValue
        private final String type;
    }
}
