package com.truelayer.quarkusmvc.services;

import com.truelayer.java.ITrueLayerClient;
import com.truelayer.quarkusmvc.models.SubscriptionRequest;
import com.truelayer.quarkusmvc.models.SubscriptionResult;
import lombok.RequiredArgsConstructor;
import org.jboss.resteasy.spi.NotImplementedYetException;

import javax.enterprise.context.ApplicationScoped;
import java.net.URI;

@ApplicationScoped
@RequiredArgsConstructor
public class SubscriptionService implements ISubscriptionService{

    private final ITrueLayerClient trueLayerClient;

    @Override
    public URI createSubscriptionLink(SubscriptionRequest req) {
        throw new NotImplementedYetException();
    }

    @Override
    public SubscriptionResult getSubscriptionById(String id) {
        throw new NotImplementedYetException();
    }
}
