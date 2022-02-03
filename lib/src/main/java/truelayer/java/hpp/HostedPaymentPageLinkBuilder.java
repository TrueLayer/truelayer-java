package truelayer.java.hpp;

import static org.apache.commons.lang3.ObjectUtils.isEmpty;

import java.net.URI;
import lombok.Builder;
import lombok.Getter;
import truelayer.java.TrueLayerException;

@Builder(builderMethodName = "New")
@Getter
public class HostedPaymentPageLinkBuilder implements IHostedPaymentPageLinkBuilder {
    private String endpoint;

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

        // todo: change to payment_token to resource_token when the HPP is ready
        String link = String.format(
                "%s/payments#payment_id=%s&payment_token=%s&return_uri=%s",
                endpoint, paymentId, resource_token, returnUri);
        return URI.create(link);
    }
}
