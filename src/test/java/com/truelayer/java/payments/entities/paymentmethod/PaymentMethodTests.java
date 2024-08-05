package com.truelayer.java.payments.entities.paymentmethod;

import static org.junit.jupiter.api.Assertions.*;

import com.truelayer.java.TrueLayerException;
import com.truelayer.java.entities.Remitter;
import com.truelayer.java.payments.entities.beneficiary.Beneficiary;
import com.truelayer.java.payments.entities.providerselection.ProviderSelection;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PaymentMethodTests {

    @Test
    @DisplayName("It should yield true if instance is of type BankTransfer")
    public void shouldYieldTrueIfBankTransfer() {
        PaymentMethod sut = new BankTransfer(null, null, null);

        assertTrue(sut.isBankTransfer());
    }

    @Test
    @DisplayName("It should convert to an instance of class BankTransfer")
    public void shouldConvertToBankTransfer() {
        PaymentMethod sut = new BankTransfer(null, null, null);

        assertDoesNotThrow(sut::asBankTransfer);
    }

    @Test
    @DisplayName("It should throw an error when converting to BankTransfer")
    public void shouldNotConvertToBankTransfer() {
        PaymentMethod sut =
                Mandate.builder().mandateId(UUID.randomUUID().toString()).build();

        Throwable thrown = assertThrows(TrueLayerException.class, sut::asBankTransfer);

        assertEquals(
                String.format("Payment method is of type %s.", sut.getClass().getSimpleName()), thrown.getMessage());
    }

    @Test
    @DisplayName("It should yield true if instance is of type Mandate")
    public void shouldYieldTrueIfMandate() {
        PaymentMethod sut =
                Mandate.builder().mandateId(UUID.randomUUID().toString()).build();

        assertTrue(sut.isMandate());
    }

    @Test
    @DisplayName("It should convert to an instance of class Mandate")
    public void shouldConvertToMandate() {
        PaymentMethod sut =
                Mandate.builder().mandateId(UUID.randomUUID().toString()).build();

        assertDoesNotThrow(sut::asMandate);
    }

    @Test
    @DisplayName("It should throw an error when converting to Mandate")
    public void shouldNotConvertToMandate() {
        PaymentMethod sut = new BankTransfer(null, null, null);

        Throwable thrown = assertThrows(TrueLayerException.class, sut::asMandate);

        assertEquals(
                String.format("Payment method is of type %s.", sut.getClass().getSimpleName()), thrown.getMessage());
    }

    @Test
    @DisplayName("BankTransfer class should implement equals method")
    public void bankTransferShouldImplementEqualsMethod() {
        String aBeneficiaryReference = "a_beneficiary_reference";
        String anotherBeneficiaryReference = "another_beneficiary_reference";
        String aRemitterName = "a_remitter_name";
        String anotherRemitterName = "another_remitter_name";

        BankTransfer bankTransfer = createBankTransfer(aBeneficiaryReference, aRemitterName);
        BankTransfer identicalBankTransfer = createBankTransfer(aBeneficiaryReference, aRemitterName);
        BankTransfer differentBankTransfer = createBankTransfer(anotherBeneficiaryReference, anotherRemitterName);

        assertEquals(bankTransfer, identicalBankTransfer);
        assertNotEquals(bankTransfer, differentBankTransfer);
    }

    @Test
    @DisplayName("Mandate class should implement equals method")
    public void mandateShouldImplementEqualsMethod() {
        String aMandateId = UUID.randomUUID().toString();
        String anotherMandateId = UUID.randomUUID().toString();

        Mandate mandate = createMandate(aMandateId);
        Mandate identicalMandate = createMandate(aMandateId);
        Mandate differentMandate = createMandate(anotherMandateId);

        assertEquals(mandate, identicalMandate);
        assertNotEquals(mandate, differentMandate);
    }

    private static BankTransfer createBankTransfer(String beneficiaryReference, String remitterName) {
        return BankTransfer.builder()
                .providerSelection(ProviderSelection.preselected()
                        .remitter(Remitter.builder()
                                .accountHolderName(remitterName)
                                .build())
                        .build())
                .beneficiary(Beneficiary.externalAccount()
                        .reference(beneficiaryReference)
                        .build())
                .build();
    }

    private static Mandate createMandate(String id) {
        return Mandate.builder().mandateId(id).build();
    }
}
