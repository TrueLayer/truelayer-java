package com.truelayer.quarkusmvc.controllers;

import com.truelayer.quarkusmvc.services.IDonationService;
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
    private IDonationService paymentService;

    @GET
    public TemplateInstance callback(@QueryParam("payment_id") String paymentId){
        var paymentDetails = paymentService.getDonationById(paymentId);

        return callback.data(paymentDetails);
    }
}
