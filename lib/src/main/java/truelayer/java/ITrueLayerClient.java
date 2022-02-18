package truelayer.java;

import truelayer.java.auth.IAuthenticationHandler;
import truelayer.java.hpp.IHostedPaymentPageLinkBuilder;
import truelayer.java.merchantaccounts.IMerchantAccountsApi;
import truelayer.java.payments.IPaymentsApi;

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
     * @return a utility to interact with payments endpoints.
     */
    IPaymentsApi payments();

    /**
     * Entrypoint for merchant accounts endpoints.
     * @return a utility to interact with merchant account endpoints.
     */
    IMerchantAccountsApi merchantAccounts();

    /**
     * Entrypoint for Hosted Payment Page related services.
     * @return a utility to build a Hosted Payment Page URL.
     */
    IHostedPaymentPageLinkBuilder hpp();
}
