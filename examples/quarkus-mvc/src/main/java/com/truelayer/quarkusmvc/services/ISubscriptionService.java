package com.truelayer.quarkusmvc.services;

import com.truelayer.quarkusmvc.models.DonationRequest;
import com.truelayer.quarkusmvc.models.DonationResult;
import com.truelayer.quarkusmvc.models.SubscriptionRequest;
import com.truelayer.quarkusmvc.models.SubscriptionResult;

import java.net.URI;

public interface ISubscriptionService {
    URI createSubscriptionLink(SubscriptionRequest req);

    SubscriptionResult getSubscriptionById(String id);
}
