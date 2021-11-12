package truelayer.java;

import truelayer.java.auth.IAuthentication;
import truelayer.java.payments.IPayments;

public interface ITrueLayerClient {
    IAuthentication auth();

    IPayments payments();
}
