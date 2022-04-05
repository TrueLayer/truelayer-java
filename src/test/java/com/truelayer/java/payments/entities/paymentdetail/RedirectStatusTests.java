package com.truelayer.java.payments.entities.paymentdetail;

import static org.junit.jupiter.api.Assertions.*;

import com.truelayer.java.TrueLayerException;
import java.net.URI;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RedirectStatusTests {

    @Test
    @DisplayName("It should yield true if instance is of type SupportedRedirectStatus")
    public void shouldYieldTrueIfSupportedRedirectStatus() {
        RedirectStatus sut = new SupportedRedirectStatus(URI.create("http://localhost"));

        assertTrue(sut.isSupported());
    }

    @Test
    @DisplayName("It should convert to an instance of class SupportedRedirectStatus")
    public void shouldConvertToSupportedRedirectStatus() {
        RedirectStatus sut = new SupportedRedirectStatus(URI.create("http://localhost"));

        assertDoesNotThrow(sut::asSupported);
    }

    @Test
    @DisplayName("It should throw an error when converting to MerchantAccount")
    public void shouldNotConvertToProviderSelection() {
        RedirectStatus sut = new NotSupportedRedirectStatus();

        Throwable thrown = assertThrows(TrueLayerException.class, sut::asSupported);

        assertEquals(String.format("Redirect is not supported.", sut.getClass().getSimpleName()), thrown.getMessage());
    }
}
