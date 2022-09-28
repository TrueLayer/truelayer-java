package com.truelayer.java.payments.entities.paymentdetail.forminput;

import static org.junit.jupiter.api.Assertions.*;

import com.truelayer.java.TrueLayerException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class InputTests {

    @Test
    @DisplayName("It should yield true if instance is of type Text")
    public void shouldYieldTrueIfText() {
        Input sut = new Text();

        assertTrue(sut.isText());
    }

    @Test
    @DisplayName("It should convert to an instance of class Text")
    public void shouldConvertToText() {
        Input sut = new Text();

        assertDoesNotThrow(sut::asText);
    }

    @Test
    @DisplayName("It should throw an error when converting to Text")
    public void shouldNotConvertToText() {
        Input sut = new Select(null);

        Throwable thrown = assertThrows(TrueLayerException.class, sut::asText);

        assertEquals(String.format("Input is of type %s.", sut.getClass().getSimpleName()), thrown.getMessage());
    }

    @Test
    @DisplayName("It should yield true if instance is of type TextWithImage")
    public void shouldYieldTrueIfTextWithImage() {
        Input sut = new TextWithImage(null);

        assertTrue(sut.isTextWithImage());
    }

    @Test
    @DisplayName("It should convert to an instance of class TextWithImage")
    public void shouldConvertToTextWithImage() {
        Input sut = new TextWithImage(null);

        assertDoesNotThrow(sut::asTextWithImage);
    }

    @Test
    @DisplayName("It should throw an error when converting to TextWithImage")
    public void shouldNotConvertToTextWithImage() {
        Input sut = new Select(null);

        Throwable thrown = assertThrows(TrueLayerException.class, sut::asTextWithImage);

        assertEquals(String.format("Input is of type %s.", sut.getClass().getSimpleName()), thrown.getMessage());
    }

    @Test
    @DisplayName("It should yield true if instance is of type Select")
    public void shouldYieldTrueIfSelect() {
        Input sut = new Select(null);

        assertTrue(sut.isSelect());
    }

    @Test
    @DisplayName("It should convert to an instance of class Select")
    public void shouldConvertToSelect() {
        Input sut = new Select(null);

        assertDoesNotThrow(sut::asSelect);
    }

    @Test
    @DisplayName("It should throw an error when converting to Select")
    public void shouldNotConvertToSelect() {
        Input sut = new Text();

        Throwable thrown = assertThrows(TrueLayerException.class, sut::asSelect);

        assertEquals(String.format("Input is of type %s.", sut.getClass().getSimpleName()), thrown.getMessage());
    }
}
