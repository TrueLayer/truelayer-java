package com.truelayer.java.entities.providerselection;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonValue;
import com.truelayer.java.TrueLayerException;
import com.truelayer.java.payments.entities.providerselection.PreselectedProviderSelection;
import com.truelayer.java.payments.entities.providerselection.PreselectedProviderSelection.PreselectedProviderSelectionBuilder;
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
        return UserSelectedProviderSelection.builder();
    }

    public static PreselectedProviderSelectionBuilder preselected() {
        return PreselectedProviderSelection.builder();
    }

    @JsonIgnore
    public boolean isUserSelected() {
        return this instanceof UserSelectedProviderSelection;
    }

    @JsonIgnore
    public boolean isPreselected() {
        return this instanceof PreselectedProviderSelection;
    }

    @JsonIgnore
    public UserSelectedProviderSelection asUserSelected() {
        if (!isUserSelected()) {
            throw new TrueLayerException(buildErrorMessage());
        }
        return (UserSelectedProviderSelection) this;
    }

    @JsonIgnore
    public PreselectedProviderSelection asPreselected() {
        if (!isPreselected()) {
            throw new TrueLayerException(buildErrorMessage());
        }
        return (PreselectedProviderSelection) this;
    }

    private String buildErrorMessage() {
        return String.format(
                "Provider selection is of type %s.", this.getClass().getSimpleName());
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
