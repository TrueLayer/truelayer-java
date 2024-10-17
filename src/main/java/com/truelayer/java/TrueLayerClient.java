package com.truelayer.java;

import com.truelayer.java.auth.IAuthenticationHandler;
import com.truelayer.java.commonapi.ICommonHandler;
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
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;

/**
 * Main class that holds TrueLayer API client. Should be built with the help of its builder
 * class. Acts as entrypoint for the Java client library capabilities.
 *
 * @see TrueLayerClientBuilder
 */
@AllArgsConstructor
public class TrueLayerClient implements ITrueLayerClient {

    private IAuthenticationHandler authenticationHandler;
    private IPaymentsHandler paymentsHandler;
    private IPaymentsProvidersHandler paymentsProvidersHandler;
    private IMerchantAccountsHandler merchantAccountsHandler;
    private IMandatesHandler mandatesHandler;
    private IPayoutsHandler payoutsHandler;
    private ISignupPlusHandler signupPlusHandler;
    private ICommonHandler commonHandler;

    private IHostedPaymentPageLinkBuilder hostedPaymentPageLinkBuilder;

    public TrueLayerClient(
            IAuthenticationHandler authenticationHandler,
            IHostedPaymentPageLinkBuilder hostedPaymentPageLinkBuilder,
            ICommonHandler commonHandler,
            ISignupPlusHandler signupPlusHandler) {
        this.authenticationHandler = authenticationHandler;
        this.hostedPaymentPageLinkBuilder = hostedPaymentPageLinkBuilder;
        this.commonHandler = commonHandler;
        this.signupPlusHandler = signupPlusHandler;
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
    public IPaymentsHandler payments() {
        if (ObjectUtils.isEmpty(paymentsHandler)) {
            throw buildInitializationException("payments");
        }
        return paymentsHandler;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IPaymentsProvidersHandler paymentsProviders() {
        if (ObjectUtils.isEmpty(paymentsProvidersHandler)) {
            throw buildInitializationException("payment providers");
        }
        return paymentsProvidersHandler;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IMerchantAccountsHandler merchantAccounts() {
        if (ObjectUtils.isEmpty(merchantAccountsHandler)) {
            throw buildInitializationException("merchant accounts");
        }
        return merchantAccountsHandler;
    }

    @Override
    public IMandatesHandler mandates() {
        if (ObjectUtils.isEmpty(mandatesHandler)) {
            throw buildInitializationException("mandates");
        }
        return mandatesHandler;
    }

    @Override
    public IPayoutsHandler payouts() {
        if (ObjectUtils.isEmpty(payoutsHandler)) {
            throw buildInitializationException("payouts");
        }
        return payoutsHandler;
    }

    @Override
    public ISignupPlusHandler signupPlus() {
        if (ObjectUtils.isEmpty(signupPlusHandler)) {
            throw buildInitializationException("signup plus");
        }
        return signupPlusHandler;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IHostedPaymentPageLinkBuilder hpp() {
        return this.hostedPaymentPageLinkBuilder;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CompletableFuture<ApiResponse<SubmitPaymentsProviderReturnResponse>> submitPaymentReturnParameters(
            SubmitPaymentsProviderReturnRequest request) {
        return commonHandler.submitPaymentReturnParameters(request);
    }

    private TrueLayerException buildInitializationException(String handlerName) {
        return new TrueLayerException(String.format(
                "%s handler not initialized."
                        + " Make sure you specified the required signing options while initializing the library",
                handlerName));
    }
}
