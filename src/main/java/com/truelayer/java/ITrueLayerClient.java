package com.truelayer.java;

import com.truelayer.java.auth.IAuthenticationHandler;
import com.truelayer.java.commonapi.entities.SubmitPaymentReturnParametersRequest;
import com.truelayer.java.commonapi.entities.SubmitPaymentReturnParametersResponse;
import com.truelayer.java.hpp.IHostedPaymentPageLinkBuilder;
import com.truelayer.java.http.entities.ApiResponse;
import com.truelayer.java.mandates.IMandatesApi;
import com.truelayer.java.merchantaccounts.IMerchantAccountsApi;
import com.truelayer.java.payments.IPaymentsApi;
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
    IPaymentsApi payments();

    /**
     * Entrypoint for merchant accounts endpoints.
     * @return a utility to interact with merchant account endpoints.
     */
    IMerchantAccountsApi merchantAccounts();

    /**
     * Entrypoint for mandates endpoints
     * @return a utility to interact with mandates endpoints.
     */
    IMandatesApi mandates();

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
    CompletableFuture<ApiResponse<SubmitPaymentReturnParametersResponse>> submitPaymentReturnParameters(
            SubmitPaymentReturnParametersRequest request);
}
