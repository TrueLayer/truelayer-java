package com.truelayer.java.payments.entities.paymentdetail;

import static org.junit.jupiter.api.Assertions.*;

import com.truelayer.java.TrueLayerException;
import java.net.URI;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AuthorizationFlowActionTests {
    @Test
    @DisplayName("It should yield true if instance is of type ProviderSelection")
    public void shouldYieldTrueIfProviderSelection() {
        AuthorizationFlowAction sut = new ProviderSelection(null);

        assertTrue(sut.isProviderSelection());
    }

    @Test
    @DisplayName("It should convert to an instance of class ProviderSelection")
    public void shouldConvertToProviderSelection() {
        AuthorizationFlowAction sut = new ProviderSelection(null);

        assertDoesNotThrow(sut::asProviderSelection);
    }

    @Test
    @DisplayName("It should throw an error when converting to ProviderSelection")
    public void shouldNotConvertToProviderSelection() {
        AuthorizationFlowAction sut = new WaitForOutcome();

        Throwable thrown = assertThrows(TrueLayerException.class, sut::asProviderSelection);

        assertEquals(
                String.format(
                        "Authorization flow is of type %s.", sut.getClass().getSimpleName()),
                thrown.getMessage());
    }

    @Test
    @DisplayName("It should yield true if instance is of type Consent")
    public void shouldYieldTrueIfConsent() {
        AuthorizationFlowAction sut = new Consent(null);

        assertTrue(sut.isConsent());
    }

    @Test
    @DisplayName("It should convert to an instance of class Consent")
    public void shouldConvertToConsent() {
        AuthorizationFlowAction sut = new Consent(null);

        assertDoesNotThrow(sut::asConsent);
    }

    @Test
    @DisplayName("It should throw an error when converting to Consent")
    public void shouldNotConvertToConsent() {
        AuthorizationFlowAction sut = new WaitForOutcome();

        Throwable thrown = assertThrows(TrueLayerException.class, sut::asConsent);

        assertEquals(
                String.format(
                        "Authorization flow is of type %s.", sut.getClass().getSimpleName()),
                thrown.getMessage());
    }

    @Test
    @DisplayName("It should yield true if instance is of type Form")
    public void shouldYieldTrueIfForm() {
        AuthorizationFlowAction sut = new Form(null);

        assertTrue(sut.isForm());
    }

    @Test
    @DisplayName("It should convert to an instance of class Form")
    public void shouldConvertToForm() {
        AuthorizationFlowAction sut = new Form(null);

        assertDoesNotThrow(sut::asForm);
    }

    @Test
    @DisplayName("It should throw an error when converting to Form")
    public void shouldNotConvertToForm() {
        AuthorizationFlowAction sut = new WaitForOutcome();

        Throwable thrown = assertThrows(TrueLayerException.class, sut::asForm);

        assertEquals(
                String.format(
                        "Authorization flow is of type %s.", sut.getClass().getSimpleName()),
                thrown.getMessage());
    }

    @Test
    @DisplayName("It should yield true if instance is of type WaitForOutcome")
    public void shouldYieldTrueIfWaitForOutcome() {
        AuthorizationFlowAction sut = new WaitForOutcome();

        assertTrue(sut.isWaitForOutcome());
    }

    @Test
    @DisplayName("It should convert to an instance of class WaitForOutcome")
    public void shouldConvertToWaitForOutcome() {
        AuthorizationFlowAction sut = new WaitForOutcome();

        assertDoesNotThrow(sut::asWaitForOutcome);
    }

    @Test
    @DisplayName("It should throw an error when converting to WaitForOutcome")
    public void shouldNotConvertToWaitForOutcome() {
        AuthorizationFlowAction sut = new ProviderSelection(null);

        Throwable thrown = assertThrows(TrueLayerException.class, sut::asWaitForOutcome);

        assertEquals(
                String.format(
                        "Authorization flow is of type %s.", sut.getClass().getSimpleName()),
                thrown.getMessage());
    }

    @Test
    @DisplayName("It should yield true if instance is of type Redirect")
    public void shouldYieldTrueIfRedirect() {
        AuthorizationFlowAction sut = new Redirect(URI.create("http://localhost"));

        assertTrue(sut.isRedirect());
    }

    @Test
    @DisplayName("It should convert to an instance of class Redirect")
    public void shouldConvertToRedirect() {
        AuthorizationFlowAction sut = new Redirect(URI.create("http://localhost"));

        assertDoesNotThrow(sut::asRedirect);
    }

    @Test
    @DisplayName("It should throw an error when converting to Redirect")
    public void shouldNotConvertToRedirect() {
        AuthorizationFlowAction sut = new ProviderSelection(null);

        Throwable thrown = assertThrows(TrueLayerException.class, sut::asRedirect);

        assertEquals(
                String.format(
                        "Authorization flow is of type %s.", sut.getClass().getSimpleName()),
                thrown.getMessage());
    }
}
