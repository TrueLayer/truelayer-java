package com.truelayer.quarkusmvc.services;

import com.truelayer.quarkusmvc.models.DonationRequest;
import com.truelayer.quarkusmvc.models.DonationResult;
import java.net.URI;

public interface IDonationService {
    URI createDonationLink(DonationRequest req);

    DonationResult getDonationById(String id);
}
