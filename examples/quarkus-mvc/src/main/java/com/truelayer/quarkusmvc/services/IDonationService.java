package com.truelayer.quarkusmvc.services;

import com.truelayer.quarkusmvc.models.DonationResult;
import com.truelayer.quarkusmvc.models.DonationRequest;

import java.net.URI;

public interface IDonationService {
    URI createDonationLink(DonationRequest req);

    DonationResult getDonationById(String id);
}
