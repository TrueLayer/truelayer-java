package com.truelayer.quarkusmvc.controllers;

import com.truelayer.quarkusmvc.models.DonationRequest;
import com.truelayer.quarkusmvc.services.IDonationService;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import org.jboss.resteasy.annotations.Form;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/donate")
public class DonateController {

    @Inject
    Template donate;

    @Inject
    private IDonationService donationService;

    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance home(){
        return donate.instance();
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response donate(@Form DonationRequest donationRequest){
        var hppUrl = donationService.createDonationLink(donationRequest);
        return Response.seeOther(hppUrl).build();
    }
}
