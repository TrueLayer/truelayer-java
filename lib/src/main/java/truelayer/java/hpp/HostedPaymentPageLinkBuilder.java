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
    public URI getHostedPaymentPageLink(String paymentId, String payment_token, URI returnUri) {
        if (isEmpty(paymentId)) {
            throw new TrueLayerException("payment_id must be set");
        }

        if (isEmpty(payment_token)) {
            throw new TrueLayerException("payment_token must be set");
        }

        if (isEmpty(returnUri) || isEmpty(returnUri.toString())) {
            throw new TrueLayerException("return_uri must be set");
        }

        String link = String.format(
                "%s/payments#payment_id=%s&payment_token=%s&return_uri=%s",
                endpoint, paymentId, payment_token, returnUri);
        return URI.create(link);
    }
}
