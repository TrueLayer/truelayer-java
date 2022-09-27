package com.truelayer.java.payments.entities.paymentdetail.forminput.image;

import com.truelayer.java.TrueLayerException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ImageTests {

    @Test
    @DisplayName("It should yield true if instance is of type Base64")
    public void shouldYieldTrueIfBase64() {
        Image sut = new Base64(null, null);

        assertTrue(sut.isBase64());
    }

    @Test
    @DisplayName("It should convert to an instance of class Base64")
    public void shouldConvertToBase64() {
        Image sut = new Base64(null, null);

        assertDoesNotThrow(sut::asBase64);
    }

    @Test
    @DisplayName("It should throw an error when converting to Base64")
    public void shouldNotConvertToBase64() {
        Image sut = new Uri(null);

        Throwable thrown = assertThrows(TrueLayerException.class, sut::asBase64);

        assertEquals(String.format("Image is of type %s.", sut.getClass().getSimpleName()), thrown.getMessage());
    }

    @Test
    @DisplayName("It should yield true if instance is of type Uri")
    public void shouldYieldTrueIfUri() {
        Image sut = new Uri(null);

        assertTrue(sut.isUri());
    }

    @Test
    @DisplayName("It should convert to an instance of class Uri")
    public void shouldConvertToUri() {
        Image sut = new Uri(null);

        assertDoesNotThrow(sut::asUri);
    }

    @Test
    @DisplayName("It should throw an error when converting to Uri")
    public void shouldNotConvertToUri() {
        Image sut = new Base64(null, null);

        Throwable thrown = assertThrows(TrueLayerException.class, sut::asUri);

        assertEquals(String.format("Image is of type %s.", sut.getClass().getSimpleName()), thrown.getMessage());
    }
}
