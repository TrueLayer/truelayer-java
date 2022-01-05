package truelayer.java.hpp;

import java.net.URI;

public interface IHostedPaymentPageLinkBuilder {
    URI getHostedPaymentPageLink(String paymentId, String resourceToken, URI returnUrl);
}
