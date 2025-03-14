package com.truelayer.java.payouts.entities.accountidentifier;

import static com.truelayer.java.Utils.getObjectMapper;
import static org.junit.jupiter.api.Assertions.*;

import com.truelayer.java.TrueLayerException;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AccountIdentifierTests {

    @Test
    @DisplayName("It should yield true if instance is of type SortCodeAccountNumberAccountIdentifier")
    public void shouldYieldTrueIfSortCodeAccNumber() {
        AccountIdentifier sut = new SortCodeAccountNumberAccountIdentifier("123456", "12345678");

        assertTrue(sut.isSortCodeAccountNumberIdentifier());
    }

    @SneakyThrows
    @Test
    @DisplayName("It should convert to an instance of class SortCodeAccountNumberAccountIdentifier")
    public void shouldConvertToSortCodeAccNumber() {
        AccountIdentifier sut = new SortCodeAccountNumberAccountIdentifier("123456", "12345678");

        assertDoesNotThrow(sut::asSortCodeAccountNumber);
        assertEquals(
                "{\"sort_code\":\"123456\",\"account_number\":\"12345678\",\"type\":\"sort_code_account_number\"}",
                getObjectMapper().writeValueAsString(sut));
    }

    @Test
    @DisplayName("It should throw an error when converting to SortCodeAccountNumberAccountIdentifier")
    public void shouldNotConvertToSortCodeAccNumber() {
        AccountIdentifier sut = new IbanAccountIdentifier("12345678");

        Throwable thrown = assertThrows(TrueLayerException.class, sut::asSortCodeAccountNumber);

        assertEquals(String.format("Identifier is of type %s.", sut.getClass().getSimpleName()), thrown.getMessage());
    }

    @Test
    @DisplayName("It should yield true if instance is of type IbanAccountIdentifier")
    public void shouldYieldTrueIfIban() {
        AccountIdentifier sut = new IbanAccountIdentifier("12345678");

        assertTrue(sut.isIbanIdentifier());
    }

    @SneakyThrows
    @Test
    @DisplayName("It should convert to an instance of class IbanAccountIdentifier")
    public void shouldConvertToIban() {
        AccountIdentifier sut = new IbanAccountIdentifier("12345678");

        assertDoesNotThrow(sut::asIban);
        assertEquals(
                "{\"iban\":\"12345678\",\"type\":\"iban\"}", getObjectMapper().writeValueAsString(sut));
    }

    @Test
    @DisplayName("It should throw an error when converting to IbanAccountIdentifier")
    public void shouldNotConvertToIbanAccountIdentifier() {
        AccountIdentifier sut = new SortCodeAccountNumberAccountIdentifier("123456", "12345678");

        Throwable thrown = assertThrows(TrueLayerException.class, sut::asIban);

        assertEquals(String.format("Identifier is of type %s.", sut.getClass().getSimpleName()), thrown.getMessage());
    }

    @Test
    @DisplayName("It should yield true if instance is of type NrbAccountIdentifier")
    public void shouldYieldTrueIfNrb() {
        AccountIdentifier sut = new NrbAccountIdentifier("12345678");

        assertTrue(sut.isNrbIdentifier());
    }

    @SneakyThrows
    @Test
    @DisplayName("It should convert to an instance of class NrbAccountIdentifier")
    public void shouldConvertToNrb() {
        AccountIdentifier sut = new NrbAccountIdentifier("12345678");

        assertDoesNotThrow(sut::asNrb);
        assertEquals(
                "{\"nrb\":\"12345678\",\"type\":\"nrb\"}", getObjectMapper().writeValueAsString(sut));
    }

    @Test
    @DisplayName("It should throw an error when converting to NrbAccountIdentifier")
    public void shouldNotConvertToNrbAccountIdentifier() {
        AccountIdentifier sut = new SortCodeAccountNumberAccountIdentifier("123456", "12345678");

        Throwable thrown = assertThrows(TrueLayerException.class, sut::asNrb);

        assertEquals(String.format("Identifier is of type %s.", sut.getClass().getSimpleName()), thrown.getMessage());
    }
}
