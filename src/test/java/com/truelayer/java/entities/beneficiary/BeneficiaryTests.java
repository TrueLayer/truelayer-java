package com.truelayer.java.entities.beneficiary;

import static org.junit.jupiter.api.Assertions.*;

import com.truelayer.java.TrueLayerException;
import com.truelayer.java.merchantaccounts.entities.transactions.accountidentifier.IbanAccountIdentifier;
import java.util.Arrays;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BeneficiaryTests {

    @Test
    @DisplayName("It should yield true if instance is of type ExternalAccount")
    public void shouldYieldTrueIfExternalAccount() {
        Beneficiary sut = new ExternalAccount(
                "a-reference",
                "a-name",
                Arrays.asList(IbanAccountIdentifier.builder().build()));

        assertTrue(sut.isExternalAccount());
    }

    @Test
    @DisplayName("It should convert to an instance of class ExternalAccount")
    public void shouldConvertToExternalAccount() {
        Beneficiary sut = new ExternalAccount(
                "a-reference",
                "a-name",
                Arrays.asList(IbanAccountIdentifier.builder().build()));

        assertDoesNotThrow(sut::asExternalAccount);
    }

    @Test
    @DisplayName("It should throw an error when converting to ExternalAccount")
    public void shouldNotConvertToExternalAccount() {
        Beneficiary sut = new PaymentSource(
                "an-id",
                "a-user-id",
                "a-reference",
                "an-account-holder-name",
                Arrays.asList(IbanAccountIdentifier.builder().build()));

        Throwable thrown = assertThrows(TrueLayerException.class, sut::asExternalAccount);

        assertEquals(String.format("Beneficiary is of type %s.", sut.getClass().getSimpleName()), thrown.getMessage());
    }

    @Test
    @DisplayName("It should yield true if instance is of type BusinessAccount")
    public void shouldYieldTrueIfBusinessAccount() {
        Beneficiary sut = new BusinessAccount(
                "a-reference",
                "a-name",
                Arrays.asList(IbanAccountIdentifier.builder().build()));

        assertTrue(sut.isBusinessAccount());
    }

    @Test
    @DisplayName("It should convert to an instance of class BusinessAccount")
    public void shouldConvertToBusinessAccount() {
        Beneficiary sut = new BusinessAccount(
                "a-reference",
                "a-name",
                Arrays.asList(IbanAccountIdentifier.builder().build()));

        assertDoesNotThrow(sut::asBusinessAccount);
    }

    @Test
    @DisplayName("It should throw an error when converting to BusinessAccount")
    public void shouldNotConvertToBusinessAccount() {
        Beneficiary sut = new PaymentSource(
                "an-id",
                "a-user-id",
                "a-reference",
                "an-account-holder-name",
                Arrays.asList(IbanAccountIdentifier.builder().build()));

        Throwable thrown = assertThrows(TrueLayerException.class, sut::asBusinessAccount);

        assertEquals(String.format("Beneficiary is of type %s.", sut.getClass().getSimpleName()), thrown.getMessage());
    }

    @Test
    @DisplayName("It should yield true if instance is of type PaymentSource")
    public void shouldYieldTrueIfPaymentSource() {
        Beneficiary sut = new PaymentSource(
                "an-id",
                "a-user-id",
                "a-reference",
                "an-account-holder-name",
                Arrays.asList(IbanAccountIdentifier.builder().build()));

        assertTrue(sut.isPaymentSource());
    }

    @Test
    @DisplayName("It should convert to an instance of class PaymentSource")
    public void shouldConvertToPaymentSource() {
        Beneficiary sut = new PaymentSource(
                "an-id",
                "a-user-id",
                "a-reference",
                "an-account-holder-name",
                Arrays.asList(IbanAccountIdentifier.builder().build()));

        assertDoesNotThrow(sut::asPaymentSource);
    }

    @Test
    @DisplayName("It should throw an error when converting to PaymentSource")
    public void shouldNotConvertToPaymentSource() {
        Beneficiary sut = new BusinessAccount(
                "a-reference",
                "a-name",
                Arrays.asList(IbanAccountIdentifier.builder().build()));

        Throwable thrown = assertThrows(TrueLayerException.class, sut::asPaymentSource);

        assertEquals(String.format("Beneficiary is of type %s.", sut.getClass().getSimpleName()), thrown.getMessage());
    }
}
