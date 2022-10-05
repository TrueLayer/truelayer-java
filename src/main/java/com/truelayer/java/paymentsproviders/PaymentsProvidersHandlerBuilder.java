package com.truelayer.java.paymentsproviders;

import com.truelayer.java.ClientCredentials;
import com.truelayer.java.TrueLayerException;
import org.apache.commons.lang3.ObjectUtils;
import retrofit2.Retrofit;

public class PaymentsProvidersHandlerBuilder {

    private Retrofit httpClient;

    private ClientCredentials clientCredentials;

    PaymentsProvidersHandlerBuilder() {}

    public PaymentsProvidersHandlerBuilder clientCredentials(ClientCredentials clientCredentials) {
        this.clientCredentials = clientCredentials;
        return this;
    }

    public PaymentsProvidersHandlerBuilder httpClient(Retrofit httpClient) {
        this.httpClient = httpClient;
        return this;
    }

    public PaymentsProvidersHandler build() {
        if (ObjectUtils.isEmpty(clientCredentials)) {
            throw new TrueLayerException("client credentials must be set");
        }
        return new PaymentsProvidersHandler(
                clientCredentials.clientId(), httpClient.create(IPaymentsProvidersApi.class));
    }
}
