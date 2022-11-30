package com.truelayer.java.payments.entities.beneficiary;

import static org.junit.jupiter.api.Assertions.*;

import com.truelayer.java.TrueLayerException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BeneficiaryTests {

    @Test
    @DisplayName("It should yield true if instance is of type MerchantAccount")
    public void shouldYieldTrueIfMerchantAccount() {
        Beneficiary sut = MerchantAccount.merchantAccount().build();

        assertTrue(sut.isMerchantAccount());
    }

    @Test
    @DisplayName("It should convert to an instance of class MerchantAccount")
    public void shouldConvertToMerchantAccount() {
        Beneficiary sut = MerchantAccount.merchantAccount().build();

        assertDoesNotThrow(sut::asMerchantAccount);
    }

    @Test
    @DisplayName("It should throw an error when converting to MerchantAccount")
    public void shouldNotConvertToMerchantAccount() {
        Beneficiary sut = ExternalAccount.builder().build();

        Throwable thrown = assertThrows(TrueLayerException.class, sut::asMerchantAccount);

        assertEquals(String.format("Beneficiary is of type %s.", sut.getClass().getSimpleName()), thrown.getMessage());
    }

    @Test
    @DisplayName("It should yield true if instance is of type ExternalAccount")
    public void shouldYieldTrueIfExternalAccount() {
        Beneficiary sut = ExternalAccount.builder().build();

        assertTrue(sut.isExternalAccount());
    }

    @Test
    @DisplayName("It should convert to an instance of class ExternalAccount")
    public void shouldConvertToExternalAccount() {
        Beneficiary sut = ExternalAccount.builder().build();

        assertDoesNotThrow(sut::asExternalAccount);
    }

    @Test
    @DisplayName("It should throw an error when converting to ExternalAccount")
    public void shouldNotConvertToExternalAccount() {
        Beneficiary sut = MerchantAccount.builder().build();

        Throwable thrown = assertThrows(TrueLayerException.class, sut::asExternalAccount);

        assertEquals(String.format("Beneficiary is of type %s.", sut.getClass().getSimpleName()), thrown.getMessage());
    }
}
