package com.truelayer.java.payments.entities.createpaymentrequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.truelayer.java.TrueLayerException;
import com.truelayer.java.entities.CurrencyCode;
import com.truelayer.java.entities.beneficiary.Beneficiary;
import com.truelayer.java.entities.beneficiary.MerchantAccount;
import com.truelayer.java.payments.entities.CreatePaymentRequest;
import com.truelayer.java.payments.entities.SchemeId;
import com.truelayer.java.payments.entities.paymentmethod.BankTransfer;
import com.truelayer.java.payments.entities.paymentmethod.provider.PreselectedProviderSelection;
import com.truelayer.java.payments.entities.paymentmethod.provider.ProviderSelection;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class CreatePaymentRequestTests {

    public static final int AN_AMOUNT_IN_MINOR = 1;
    public static final CurrencyCode A_CURRENCY_CODE = CurrencyCode.EUR;
    public static final String A_PROVIDER_ID = "mock-bank";
    public static final String A_HOLDER_NAME = "Bob";

    @Test
    @DisplayName("It should build create payment request instance")
    public void itShouldBuildCreatePaymentRequestInstance() {
        CreatePaymentRequest createPaymentRequest = CreatePaymentRequest.builder()
                .amountInMinor(AN_AMOUNT_IN_MINOR)
                .currency(A_CURRENCY_CODE)
                .paymentMethod(BankTransfer.builder()
                        .beneficiary(beneficiaryStub())
                        .providerSelection(providerSelectionStub())
                        .build())
                .build();

        assertEquals(AN_AMOUNT_IN_MINOR, createPaymentRequest.getAmountInMinor());
        assertEquals(A_CURRENCY_CODE, createPaymentRequest.getCurrency());
        assertEquals(
                A_HOLDER_NAME,
                createPaymentRequest
                        .getPaymentMethod()
                        .getBeneficiary()
                        .asMerchantAccount()
                        .getAccountHolderName());
    }

    @ParameterizedTest
    @ValueSource(ints = {-100, 0})
    @DisplayName("It should throw an exception if amountInMinor is not positive")
    public void itShouldThrowExceptionIfAmountInMinorIsNotPositive(int amountInMinor) {
        Throwable thrown = assertThrows(TrueLayerException.class, () -> CreatePaymentRequest.builder()
                .amountInMinor(amountInMinor)
                .currency(A_CURRENCY_CODE)
                .paymentMethod(BankTransfer.builder()
                        .beneficiary(beneficiaryStub())
                        .providerSelection(providerSelectionStub())
                        .build())
                .build());

        assertEquals(thrown.getMessage(), "amount in minor must be >= 1");
    }

    @Test
    @DisplayName("It should throw an exception if currency is not set")
    public void itShouldThrowExceptionIfCurrencyIsNotSet() {
        Throwable thrown = assertThrows(TrueLayerException.class, () -> CreatePaymentRequest.builder()
                .amountInMinor(AN_AMOUNT_IN_MINOR)
                .paymentMethod(BankTransfer.builder()
                        .beneficiary(beneficiaryStub())
                        .providerSelection(providerSelectionStub())
                        .build())
                .build());

        assertEquals(thrown.getMessage(), "currency must be set");
    }

    @Test
    @DisplayName("It should throw an exception if payment method is not set")
    public void itShouldThrowExceptionIfPaymentMethodIsNotSet() {
        Throwable thrown = assertThrows(TrueLayerException.class, () -> CreatePaymentRequest.builder()
                .amountInMinor(1)
                .currency(CurrencyCode.GBP)
                .build());

        assertEquals(thrown.getMessage(), "payment method must be set");
    }

    private static Beneficiary beneficiaryStub() {
        return MerchantAccount.builder()
                .accountHolderName(A_HOLDER_NAME)
                .merchantAccountId(UUID.randomUUID().toString())
                .build();
    }

    private static ProviderSelection providerSelectionStub() {
        return PreselectedProviderSelection.builder()
                .providerId(A_PROVIDER_ID)
                .schemeId(SchemeId.FASTER_PAYMENTS_SERVICE)
                .build();
    }
}
