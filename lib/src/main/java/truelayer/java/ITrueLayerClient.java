package truelayer.java;

import truelayer.java.auth.IAuthenticationHandler;
import truelayer.java.payments.IPaymentHandler;

public interface ITrueLayerClient {
    IAuthenticationHandler auth();

    IPaymentHandler payments();
}
