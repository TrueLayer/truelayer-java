package com.truelayer.java.http.entities;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ProblemDetailsTests {

    @Test
    @DisplayName("It should yield malformed")
    public void itShouldYieldMalformed() {
        ProblemDetails sut = ProblemDetails.builder().title("something").build();

        assertFalse(sut.isWellFormed());
    }

    @Test
    @DisplayName("It should yield well formed")
    public void itShouldYieldWellFormed() {
        ProblemDetails sut = ProblemDetails.builder()
                .title("something")
                .type("a-type")
                .status(400)
                .build();

        assertTrue(sut.isWellFormed());
    }
}
