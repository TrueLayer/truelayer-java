package com.truelayer.quarkusmvc.controllers;

import com.truelayer.java.ITrueLayerClient;
import com.truelayer.quarkusmvc.models.DonationRequest;
import com.truelayer.quarkusmvc.services.IDonationService;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.jboss.resteasy.annotations.Form;

@Path("/donations")
@RequiredArgsConstructor
public class DonationsController {

    private final ITrueLayerClient trueLayerClient;

    @Inject
    Template donations;

    @Inject
    Template callback;

    @Inject
    private IDonationService donationService;

    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance home() {
        return donations.instance();
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response donate(@Form DonationRequest donationRequest) {
        var hppUrl = donationService.createDonationLink(donationRequest);
        return Response.seeOther(hppUrl).build();
    }

    @GET
    @Path("/callback")
    public TemplateInstance callback(@QueryParam("payment_id") String paymentId) {
        var donation = donationService.getDonationById(paymentId);
        return callback.data(donation);
    }
}
