package com.truelayer.quarkusmvc.controllers;

import com.truelayer.quarkusmvc.services.IDonationService;
import com.truelayer.quarkusmvc.services.ISubscriptionService;
import io.netty.util.internal.ObjectUtil;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

@Path("/callback")
public class CallbackController {
    @Inject
    Template callback;

    @Inject
    private IDonationService donationService;

    @Inject
    private ISubscriptionService subscriptionService;

    @GET
    public TemplateInstance callback(@QueryParam("payment_id") String paymentId, @QueryParam("mandate_id") String mandateId){
        if(paymentId != null){
            var donation = donationService.getDonationById(paymentId);
            return callback.data(donation);
        }

        // subs otherwise
        var subscription = subscriptionService.getSubscriptionById(mandateId);
        return callback.data(subscription);
    }
}
