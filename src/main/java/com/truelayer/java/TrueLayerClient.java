package com.truelayer.java;

import com.truelayer.java.auth.IAuthenticationHandler;
import com.truelayer.java.commonapi.ICommonApi;
import com.truelayer.java.commonapi.entities.SubmitPaymentReturnParametersRequest;
import com.truelayer.java.commonapi.entities.SubmitPaymentReturnParametersResponse;
import com.truelayer.java.hpp.IHostedPaymentPageLinkBuilder;
import com.truelayer.java.http.entities.ApiResponse;
import com.truelayer.java.merchantaccounts.IMerchantAccountsApi;
import com.truelayer.java.payments.IPaymentsApi;
import java.util.concurrent.CompletableFuture;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;

/**
 * Main class that holds TrueLayer API client. Should be built with the help of its builder
 * class. Acts as entrypoint for the Java client library capabilities.
 *
 * @see TrueLayerClientBuilder
 */
@AllArgsConstructor
public class TrueLayerClient implements ITrueLayerClient, ICommonApi {
    private IAuthenticationHandler authenticationHandler;
    private IPaymentsApi paymentsHandler;
    private IMerchantAccountsApi merchantAccountsHandler;
    private IHostedPaymentPageLinkBuilder hostedPaymentPageLinkBuilder;
    private ICommonApi commonApi;

    public TrueLayerClient(
            IAuthenticationHandler authenticationHandler,
            IHostedPaymentPageLinkBuilder hostedPaymentPageLinkBuilder,
            ICommonApi commonApi) {
        this.authenticationHandler = authenticationHandler;
        this.hostedPaymentPageLinkBuilder = hostedPaymentPageLinkBuilder;
        this.commonApi = commonApi;
    }

    /**
     * Static utility to return a builder instance.
     * @return an instance of the TrueLayer API client builder.
     */
    public static TrueLayerClientBuilder New() {
        return new TrueLayerClientBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IAuthenticationHandler auth() {
        return this.authenticationHandler;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IPaymentsApi payments() {
        if (ObjectUtils.isEmpty(paymentsHandler)) {
            throw buildInitializationException("payments");
        }
        return paymentsHandler;
    }

    @Override
    public IMerchantAccountsApi merchantAccounts() {
        if (ObjectUtils.isEmpty(merchantAccountsHandler)) {
            throw buildInitializationException("merchant accounts");
        }
        return merchantAccountsHandler;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IHostedPaymentPageLinkBuilder hpp() {
        return this.hostedPaymentPageLinkBuilder;
    }

    private TrueLayerException buildInitializationException(String handlerName) {
        return new TrueLayerException(String.format(
                "%s handler not initialized."
                        + " Make sure you specified the required signing options while initializing the library",
                handlerName));
    }

    @Override
    public CompletableFuture<ApiResponse<SubmitPaymentReturnParametersResponse>> submitPaymentReturnsParameters(
            SubmitPaymentReturnParametersRequest request) {
        return commonApi.submitPaymentReturnsParameters(request);
    }
}
