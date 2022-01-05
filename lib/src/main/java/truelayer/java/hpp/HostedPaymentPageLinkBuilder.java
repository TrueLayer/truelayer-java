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
    public URI getHostedPaymentPageLink(String paymentId, String resourceToken, URI returnUrl) {
        if(isEmpty(paymentId)){
            throw new TrueLayerException("payment_id must be set");
        }

        if(isEmpty(resourceToken)){
            throw new TrueLayerException("resource_token must be set");
        }

        if(isEmpty(returnUrl) || isEmpty(returnUrl.toString())){
            throw new TrueLayerException("return_url must be set");
        }

        var link = String.format("%s/payments#payment_id=%s&resource_token=%s&return_url=%s",
                endpoint,
                paymentId,
                resourceToken,
                returnUrl);
        return URI.create(link);
    }
}
