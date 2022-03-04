package com.truelayer.java.hpp;

import java.net.URI;

/**
 * Exposes all the Hosted Payment Page related capabilities of the library.
 */
public interface IHostedPaymentPageLinkBuilder {
    /**
     * Builds a link to TrueLayer HPP with the given details
     * @param paymentId the id of the payment created with a previous call to the Create Payment endpoint
     * @param resource_token the token of the payment created with a previous call to the Create Payment endpoint
     * @param returnUri the return URI where the client will be redirected once the payment is completed
     * @return the link to TrueLayer hosted payment page
     * @see <a href="https://docs.truelayer.com/docs/hosted-payment-page">See <i>Hosted Payment Page</i> documentaion for further details</a>
     */
    URI getHostedPaymentPageLink(String paymentId, String resource_token, URI returnUri);
}
