package com.truelayer.java.payments.entities.verification;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class VerificationTests {
    @Test
    @DisplayName("It should yield true if instance is of type AutomatedVerification")
    public void shouldYieldTrueIfAutomatedVerification() {
        Verification sut =
                AutomatedVerification.builder().withRemitterDateOfBirth().build();

        assertTrue(sut.isAutomated());
    }

    @Test
    @DisplayName("It should convert to an instance of class AutomatedVerification")
    public void shouldConvertToAutomatedVerification() {
        Verification sut = AutomatedVerification.builder()
                .withRemitterName()
                .withRemitterDateOfBirth()
                .build();

        assertDoesNotThrow(sut::asAutomated);
    }
}
