package com.truelayer.java.payments.entities.schemeselection.preselected;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.truelayer.java.TrueLayerException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class SchemeSelectionTests {

    @Test
    @DisplayName("It should yield true if instance is of type InstantOnlySchemeSelection")
    public void shouldYieldTrueIfInstantOnly() {
        SchemeSelection sut = new InstantOnlySchemeSelection(true);
        assertTrue(sut.isInstantOnly());
    }

    @Test
    @DisplayName("It should convert to an instance of class InstantOnlySchemeSelection")
    public void shouldConvertToInstantOnly() {
        SchemeSelection sut = new InstantOnlySchemeSelection(true);

        assertDoesNotThrow(sut::asInstantOnly);
    }

    @Test
    @DisplayName("It should throw an error when converting to InstantOnlySchemeSelection")
    public void shouldNotConvertToInstantOnly() {
        SchemeSelection sut = new InstantPreferredSchemeSelection(true);

        Throwable thrown = assertThrows(TrueLayerException.class, sut::asInstantOnly);

        assertEquals(
                String.format("Scheme selection is of type %s.", sut.getClass().getSimpleName()), thrown.getMessage());
    }

    @Test
    @DisplayName("It should yield true if instance is of type InstantPreferredSchemeSelection")
    public void shouldYieldTrueIfInstantPreferred() {
        SchemeSelection sut = new InstantPreferredSchemeSelection(true);
        assertTrue(sut.isInstantPreferred());
    }

    @Test
    @DisplayName("It should convert to an instance of class InstantPreferredSchemeSelection")
    public void shouldConvertToInstantPreferred() {
        SchemeSelection sut = new InstantPreferredSchemeSelection(true);

        assertDoesNotThrow(sut::asInstantPreferred);
    }

    @Test
    @DisplayName("It should throw an error when converting to InstantPreferredSchemeSelection")
    public void shouldNotConvertToInstantPreferred() {
        SchemeSelection sut = new InstantOnlySchemeSelection(true);

        Throwable thrown = assertThrows(TrueLayerException.class, sut::asInstantPreferred);

        assertEquals(
                String.format("Scheme selection is of type %s.", sut.getClass().getSimpleName()), thrown.getMessage());
    }

    @Test
    @DisplayName("It should yield true if instance is of type PreselectedSchemeSelection")
    public void shouldYieldTrueIfPreselected() {
        SchemeSelection sut = new PreselectedSchemeSelection(null);
        assertTrue(sut.isPreselected());
    }

    @Test
    @DisplayName("It should convert to an instance of class PreselectedSchemeSelection")
    public void shouldConvertToPreselected() {
        SchemeSelection sut = new PreselectedSchemeSelection(null);

        assertDoesNotThrow(sut::asPreselected);
    }

    @Test
    @DisplayName("It should throw an error when converting to PreselectedSchemeSelection")
    public void shouldNotConvertToPreselected() {
        SchemeSelection sut = new InstantOnlySchemeSelection(true);

        Throwable thrown = assertThrows(TrueLayerException.class, sut::asPreselected);

        assertEquals(
                String.format("Scheme selection is of type %s.", sut.getClass().getSimpleName()), thrown.getMessage());
    }
}
