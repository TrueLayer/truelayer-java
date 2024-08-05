package com.truelayer.java.payments.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.truelayer.java.payments.entities.schemeselection.SchemeSelection;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
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
