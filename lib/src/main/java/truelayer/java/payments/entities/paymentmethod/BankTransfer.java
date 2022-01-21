package truelayer.java.payments.entities.paymentmethod;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import truelayer.java.payments.entities.paymentmethod.provider.BaseProvider;

@Builder
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BankTransfer extends BasePaymentMethod {
    private final String type = "bank_transfer";

    private BaseProvider provider;
}
