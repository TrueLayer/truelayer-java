package com.truelayer.java.payments.entities.schemeselection.preselected;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonValue;
import com.truelayer.java.TrueLayerException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", defaultImpl = InstantOnlySchemeSelection.class)
@JsonSubTypes({
    @JsonSubTypes.Type(value = InstantOnlySchemeSelection.class, name = "instant_only"),
    @JsonSubTypes.Type(value = InstantPreferredSchemeSelection.class, name = "instant_preferred"),
    @JsonSubTypes.Type(value = PreselectedSchemeSelection.class, name = "preselected"),
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

    public static PreselectedSchemeSelection.PreselectedSchemeSelectionBuilder preselected() {
        return PreselectedSchemeSelection.builder();
    }

    @JsonIgnore
    public boolean isInstantOnly() {
        return this instanceof InstantOnlySchemeSelection;
    }

    @JsonIgnore
    public boolean isInstantPreferred() {
        return this instanceof InstantPreferredSchemeSelection;
    }

    @JsonIgnore
    public boolean isPreselected() {
        return this instanceof PreselectedSchemeSelection;
    }

    @JsonIgnore
    public InstantOnlySchemeSelection asInstantOnly() {
        if (!isInstantOnly()) {
            throw new TrueLayerException(buildErrorMessage());
        }
        return (InstantOnlySchemeSelection) this;
    }

    @JsonIgnore
    public InstantPreferredSchemeSelection asInstantPreferred() {
        if (!isInstantPreferred()) {
            throw new TrueLayerException(buildErrorMessage());
        }
        return (InstantPreferredSchemeSelection) this;
    }

    @JsonIgnore
    public PreselectedSchemeSelection asPreselected() {
        if (!isPreselected()) {
            throw new TrueLayerException(buildErrorMessage());
        }
        return (PreselectedSchemeSelection) this;
    }

    private String buildErrorMessage() {
        return String.format("Scheme selection is of type %s.", this.getClass().getSimpleName());
    }

    @RequiredArgsConstructor
    @Getter
    public enum Type {
        INSTANT_ONLY("instant_only"),
        INSTANT_PREFERRED("instant_preferred"),
        PRESELECTED("preselected");

        @JsonValue
        private final String type;
    }
}
