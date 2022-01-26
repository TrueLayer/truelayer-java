package truelayer.java.payments.entities.paymentmethod;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import truelayer.java.payments.entities.paymentmethod.provider.Provider;

@Builder
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BankTransfer extends PaymentMethod {
    private final String type = "bank_transfer";

    private Provider provider;
}
