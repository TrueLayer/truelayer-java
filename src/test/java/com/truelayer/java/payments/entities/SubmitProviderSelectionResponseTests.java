package com.truelayer.java.payments.entities;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SubmitProviderSelectionResponseTests {

    @Test
    @DisplayName("It should yield true if instance is of type PaymentAuthorizationFlowAuthorizing")
    public void shouldYieldTrueIfAuthorizing() {
        SubmitProviderSelectionResponse authorizing = new PaymentAuthorizationFlowAuthorizing();

        Assertions.assertTrue(authorizing.isAuthorizing());
    }

    @Test
    @DisplayName("It should convert to an instance of class PaymentAuthorizationFlowAuthorizing")
    public void shouldConvertToAuthorizing() {
        SubmitProviderSelectionResponse authorizing = new PaymentAuthorizationFlowAuthorizing();

        Assertions.assertDoesNotThrow(authorizing::asAuthorizing);
    }
}
