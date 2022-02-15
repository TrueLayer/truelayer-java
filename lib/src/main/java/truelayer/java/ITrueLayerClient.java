package truelayer.java;

import truelayer.java.auth.IAuthenticationHandler;
import truelayer.java.hpp.IHostedPaymentPageLinkBuilder;
import truelayer.java.merchantaccounts.IMerchantAccountsHandler;
import truelayer.java.payments.IPaymentHandler;

/**
 * TrueLayer client facade. Acts as entrypoint for the Java client library capabilities.
 */
public interface ITrueLayerClient {

    /**
     * Entrypoint for oauth endpoints.
     * @return a utility to create access tokens.
     */
    IAuthenticationHandler auth();

    /**
     * Entrypoint for payments endpoints.
     * @return a utility to create and authorize payments.
     */
    IPaymentHandler payments();

    /**
     * Entrypoint for merchant-account endpoints.
     * @return a utility to interact with merchant-accounts endpoints.
     */
    IMerchantAccountsHandler merchantAccounts();

    /**
     * Entrypoint for Hosted Payment Page related services.
     * @return a utility to build a Hosted Payment Page URL.
     */
    IHostedPaymentPageLinkBuilder hpp();
}
