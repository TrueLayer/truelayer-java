package com.truelayer.java.payments.entities.paymentmethod;

import static com.truelayer.java.payments.entities.paymentmethod.PaymentMethod.Type.BANK_TRANSFER;

import com.truelayer.java.Utils;
import com.truelayer.java.entities.beneficiary.Beneficiary;
import com.truelayer.java.payments.entities.paymentmethod.provider.ProviderSelection;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class BankTransfer extends PaymentMethod {
    private final Type type = BANK_TRANSFER;

    @NotNull(message = "provider selection must be set")
    private ProviderSelection providerSelection;

    @NotNull(message = "beneficiary must be set")
    private Beneficiary beneficiary;

    public static class BankTransferBuilder {

        public BankTransfer build() {
            BankTransfer bankTransfer = new BankTransfer(providerSelection, beneficiary);
            Utils.validateObject(bankTransfer);
            return bankTransfer;
        }
    }
}
