package com.truelayer.java.payouts.entities.beneficiary;

import static org.junit.jupiter.api.Assertions.*;

import com.truelayer.java.TrueLayerException;
import com.truelayer.java.entities.Address;
import com.truelayer.java.payouts.entities.accountidentifier.IbanAccountIdentifier;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BeneficiaryTests {

    @Test
    @DisplayName("It should yield true if instance is of type ExternalAccount")
    public void shouldYieldTrueIfExternalAccount() {
        Beneficiary sut = new ExternalAccount.ExternalAccountBuilder()
                .reference("a-reference")
                .accountHolderName("an-account-holder-name")
                .accountIdentifier(IbanAccountIdentifier.builder().build())
                .dateOfBirth(LocalDate.of(1980, 1, 1))
                .address(Address.builder().build())
                .build();

        assertTrue(sut.isExternalAccount());
    }

    @Test
    @DisplayName("It should convert to an instance of class ExternalAccount")
    public void shouldConvertToExternalAccount() {
        Beneficiary sut = new ExternalAccount.ExternalAccountBuilder()
                .reference("a-reference")
                .accountHolderName("an-account-holder-name")
                .accountIdentifier(IbanAccountIdentifier.builder().build())
                .dateOfBirth(LocalDate.of(1980, 1, 1))
                .address(Address.builder().build())
                .build();

        assertDoesNotThrow(sut::asExternalAccount);
    }

    @Test
    @DisplayName("It should throw an error when converting to ExternalAccount")
    public void shouldNotConvertToExternalAccount() {
        Beneficiary sut = new PaymentSource.PaymentSourceBuilder()
                .reference("a-reference")
                .paymentSourceId("a-source-id")
                .userId("a-user-id")
                .build();

        Throwable thrown = assertThrows(TrueLayerException.class, sut::asExternalAccount);

        assertEquals(String.format("Beneficiary is of type %s.", sut.getClass().getSimpleName()), thrown.getMessage());
    }

    @Test
    @DisplayName("It should yield true if instance is of type BusinessAccount")
    public void shouldYieldTrueIfBusinessAccount() {
        Beneficiary sut = new BusinessAccount.BusinessAccountBuilder()
                .reference("a-reference")
                .build();

        assertTrue(sut.isBusinessAccount());
    }

    @Test
    @DisplayName("It should convert to an instance of class BusinessAccount")
    public void shouldConvertToBusinessAccount() {
        Beneficiary sut = new BusinessAccount.BusinessAccountBuilder()
                .reference("a-reference")
                .build();

        assertDoesNotThrow(sut::asBusinessAccount);
    }

    @Test
    @DisplayName("It should throw an error when converting to BusinessAccount")
    public void shouldNotConvertToBusinessAccount() {
        Beneficiary sut = new PaymentSource.PaymentSourceBuilder()
                .reference("a-reference")
                .paymentSourceId("a-source-id")
                .userId("a-user-id")
                .build();

        Throwable thrown = assertThrows(TrueLayerException.class, sut::asBusinessAccount);

        assertEquals(String.format("Beneficiary is of type %s.", sut.getClass().getSimpleName()), thrown.getMessage());
    }

    @Test
    @DisplayName("It should yield true if instance is of type PaymentSource")
    public void shouldYieldTrueIfPaymentSource() {
        Beneficiary sut = new PaymentSource.PaymentSourceBuilder()
                .reference("a-reference")
                .paymentSourceId("a-source-id")
                .userId("a-user-id")
                .build();

        assertTrue(sut.isPaymentSource());
    }

    @Test
    @DisplayName("It should convert to an instance of class PaymentSource")
    public void shouldConvertToPaymentSource() {
        Beneficiary sut = new PaymentSource.PaymentSourceBuilder()
                .reference("a-reference")
                .paymentSourceId("a-source-id")
                .userId("a-user-id")
                .build();

        assertDoesNotThrow(sut::asPaymentSource);
    }

    @Test
    @DisplayName("It should throw an error when converting to PaymentSource")
    public void shouldNotConvertToPaymentSource() {
        Beneficiary sut = new BusinessAccount.BusinessAccountBuilder()
                .reference("a-reference")
                .build();

        Throwable thrown = assertThrows(TrueLayerException.class, sut::asPaymentSource);

        assertEquals(String.format("Beneficiary is of type %s.", sut.getClass().getSimpleName()), thrown.getMessage());
    }
}
