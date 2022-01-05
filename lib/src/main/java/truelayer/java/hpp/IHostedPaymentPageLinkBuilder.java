package truelayer.java.hpp;

import java.net.URI;

public interface IHostedPaymentPageLinkBuilder {
    /**
     * Builds a link to TrueLayer HPP with the given details
     * @param paymentId the id of the payment created with a previous call to the Create Payment endpoint
     * @param resourceToken the resource_token of the payment created with a previous call to the Create Payment endpoint
     * @param returnUri the return URI where the client will be redirected once the payment is completed
     * @return the link to TrueLayer hosted payment page
     */
    URI getHostedPaymentPageLink(String paymentId, String resourceToken, URI returnUri);
}
