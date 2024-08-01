package com.truelayer.java.payments.entities.schemeselection;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.*;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", defaultImpl = InstantOnlySchemeSelection.class)
@JsonSubTypes({
    @JsonSubTypes.Type(value = InstantOnlySchemeSelection.class, name = "instant_only"),
    @JsonSubTypes.Type(value = InstantPreferredSchemeSelection.class, name = "instant_preferred")
})
@Getter
@ToString
@EqualsAndHashCode
public abstract class SchemeSelection {
    @JsonIgnore
    public abstract Type getType();

    public static InstantOnlySchemeSelection.InstantOnlySchemeSelectionBuilder instantOnly() {
        return InstantOnlySchemeSelection.builder();
    }

    public static InstantPreferredSchemeSelection.InstantPreferredSchemeSelectionBuilder instantPreferred() {
        return InstantPreferredSchemeSelection.builder();
    }

    @RequiredArgsConstructor
    @Getter
    public enum Type {
        INSTANT_ONLY("instant_only"),
        INSTANT_PREFERRED("instant_preferred");

        @JsonValue
        private final String type;
    }
}
