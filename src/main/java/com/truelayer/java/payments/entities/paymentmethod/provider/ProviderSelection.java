package com.truelayer.java.payments.entities.paymentmethod.provider;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.*;

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

    public static UserSelectedProviderSelection.UserSelectedProviderSelectionBuilder userSelected() {
        return new UserSelectedProviderSelection.UserSelectedProviderSelectionBuilder();
    }

    public static PreselectedProviderSelection.PreselectedProviderSelectionBuilder preselected() {
        return new PreselectedProviderSelection.PreselectedProviderSelectionBuilder();
    }

    public UserSelectedProviderSelection asUserSelected() {
        return (UserSelectedProviderSelection) this;
    }

    public PreselectedProviderSelection asPreselected() {
        return (PreselectedProviderSelection) this;
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
