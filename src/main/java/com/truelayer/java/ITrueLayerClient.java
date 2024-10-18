package com.truelayer.java;

import com.truelayer.java.auth.IAuthenticationHandler;
import com.truelayer.java.commonapi.entities.SubmitPaymentsProviderReturnRequest;
import com.truelayer.java.commonapi.entities.SubmitPaymentsProviderReturnResponse;
import com.truelayer.java.hpp.IHostedPaymentPageLinkBuilder;
import com.truelayer.java.http.entities.ApiResponse;
import com.truelayer.java.mandates.IMandatesHandler;
import com.truelayer.java.merchantaccounts.IMerchantAccountsHandler;
import com.truelayer.java.payments.IPaymentsHandler;
import com.truelayer.java.paymentsproviders.IPaymentsProvidersHandler;
import com.truelayer.java.payouts.IPayoutsHandler;
import com.truelayer.java.signupplus.ISignupPlusHandler;
import java.util.concurrent.CompletableFuture;

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
    IPaymentsHandler payments();

    /**
     * Entrypoint for payments providers endpoints.
     * @return a utility to interact with payments providers endpoints.
     */
    IPaymentsProvidersHandler paymentsProviders();

    /**
     * Entrypoint for merchant accounts endpoints.
     * @return a utility to interact with merchant account endpoints.
     */
    IMerchantAccountsHandler merchantAccounts();

    /**
     * Entrypoint for mandates endpoints
     * @return a utility to interact with mandates endpoints.
     */
    IMandatesHandler mandates();

    /**
     * Entrypoint for payouts endpoints.
     * @return a utility to interact with payouts endpoints.
     */
    IPayoutsHandler payouts();

    /**
     * Entrypoint for signup+ endpoints.
     * @return a utility to interact with signup+ endpoints.
     */
    ISignupPlusHandler signupPlus();

    /**
     * Entrypoint for Hosted Payment Page related services.
     * @return a utility to build a Hosted Payment Page URL.
     */
    IHostedPaymentPageLinkBuilder hpp();

    /**
     * Utility to submit payment returns parameters.
     * @param request a submit payment return parameters payload
     * @return the response of the <i>Submit payment returns parameters</i> operation
     * @see <a href="https://docs.truelayer.com/reference/submit-payments-provider-return-parameters"><i>Submit payments return parameters</i> API reference</a>
     */
    CompletableFuture<ApiResponse<SubmitPaymentsProviderReturnResponse>> submitPaymentReturnParameters(
            SubmitPaymentsProviderReturnRequest request);
}
