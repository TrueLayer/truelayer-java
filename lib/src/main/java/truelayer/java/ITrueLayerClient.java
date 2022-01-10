package truelayer.java;

import truelayer.java.auth.IAuthenticationHandler;
import truelayer.java.hpp.IHostedPaymentPageLinkBuilder;
import truelayer.java.payments.IPaymentHandler;

public interface ITrueLayerClient {
    IAuthenticationHandler auth();

    IPaymentHandler payments();

    IHostedPaymentPageLinkBuilder hpp();
}
