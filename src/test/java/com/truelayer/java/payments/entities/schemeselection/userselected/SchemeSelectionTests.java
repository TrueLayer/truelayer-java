package com.truelayer.java.payments.entities.schemeselection.userselected;

import static org.junit.jupiter.api.Assertions.*;

import com.truelayer.java.TrueLayerException;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class SchemeSelectionTests {

    @DisplayName("Scheme selection base class should yield the expected type and allow_remitter_fee")
    @ParameterizedTest(name = "object={0}, expected type={1}, expected allow_remitter_fee={2}")
    @MethodSource("provideSchemeSelectionTestParameters")
    public void baseClassShouldYieldExpectedDetails(
            SchemeSelection schemeSelection, SchemeSelection.Type expectedType, boolean expectAllowRemitterFee) {
        assertEquals(schemeSelection.getType(), expectedType);
        assertEquals(schemeSelection.allowRemitterFee(), expectAllowRemitterFee);
    }

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

    private static Stream<Arguments> provideSchemeSelectionTestParameters() {
        return Stream.of(
                Arguments.of(
                        SchemeSelection.instantOnly().allowRemitterFee(true).build(),
                        SchemeSelection.Type.INSTANT_ONLY,
                        true),
                Arguments.of(
                        SchemeSelection.instantOnly().allowRemitterFee(false).build(),
                        SchemeSelection.Type.INSTANT_ONLY,
                        false),
                Arguments.of(
                        SchemeSelection.instantPreferred()
                                .allowRemitterFee(true)
                                .build(),
                        SchemeSelection.Type.INSTANT_PREFERRED,
                        true),
                Arguments.of(
                        SchemeSelection.instantPreferred()
                                .allowRemitterFee(false)
                                .build(),
                        SchemeSelection.Type.INSTANT_PREFERRED,
                        false));
    }
}
