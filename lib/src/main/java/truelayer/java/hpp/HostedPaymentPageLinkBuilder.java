package truelayer.java.hpp;

import lombok.Builder;
import lombok.Getter;
import truelayer.java.TrueLayerException;

import java.net.URI;

import static org.apache.commons.lang3.ObjectUtils.isEmpty;

@Builder
@Getter
public class HostedPaymentPageLinkBuilder implements IHostedPaymentPageLinkBuilder {
    private String endpoint;

    @Override
    public URI getHostedPaymentPageLink(String paymentId, String resourceToken, URI returnUri) {
        if(isEmpty(paymentId)){
            throw new TrueLayerException("payment_id must be set");
        }

        if(isEmpty(resourceToken)){
            throw new TrueLayerException("resource_token must be set");
        }

        if(isEmpty(returnUri) || isEmpty(returnUri.toString())){
            throw new TrueLayerException("return_uri must be set");
        }

        var link = String.format("%s/payments#payment_id=%s&resource_token=%s&return_uri=%s",
                endpoint,
                paymentId,
                resourceToken,
                returnUri);
        return URI.create(link);
    }
}
