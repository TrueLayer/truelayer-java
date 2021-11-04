package truelayer.java.payments;

import lombok.Builder;
import truelayer.java.SigningOptions;
import truelayer.java.payments.entities.CreatePaymentRequest;
import truelayer.java.payments.entities.Payment;

@Builder
public class Payments implements IPayments{

    private String clientId;

    private String clientSecret;

    private SigningOptions signingOptions;

    @Override
    public Payment createPayment(CreatePaymentRequest request) {
        //TODO implement
        return null;
    }
}
