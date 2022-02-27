package com.truelayer.java.hpp;

import static org.apache.commons.lang3.ObjectUtils.isEmpty;

import com.truelayer.java.TrueLayerException;
import java.net.URI;
import lombok.Builder;
import lombok.Getter;

/**
 * {{@inheritDoc}}
 */
@Builder(builderMethodName = "New")
@Getter
public class HostedPaymentPageLinkBuilder implements IHostedPaymentPageLinkBuilder {
    private URI uri;

    @Override
    public URI getHostedPaymentPageLink(String paymentId, String resource_token, URI returnUri) {
        if (isEmpty(paymentId)) {
            throw new TrueLayerException("payment_id must be set");
        }

        if (isEmpty(resource_token)) {
            throw new TrueLayerException("resource_token must be set");
        }

        if (isEmpty(returnUri) || isEmpty(returnUri.toString())) {
            throw new TrueLayerException("return_uri must be set");
        }

        String link = String.format(
                "%s/payments#payment_id=%s&resource_token=%s&return_uri=%s", uri, paymentId, resource_token, returnUri);
        return URI.create(link);
    }
}
