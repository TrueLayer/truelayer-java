package com.truelayer.quarkusmvc.controllers;

import com.truelayer.quarkusmvc.models.SubscriptionRequest;
import com.truelayer.quarkusmvc.services.ISubscriptionService;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import org.jboss.resteasy.annotations.Form;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/subscriptions")
public class SubscriptionsController {

    @Inject
    Template subscriptions;

    @Inject
    Template callback;

    @Inject
    private ISubscriptionService subscriptionService;

    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance subscriptions(){
        return subscriptions.instance();
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response subscribe(@Form SubscriptionRequest subscriptionRequest){
        var hppUrl = subscriptionService.createSubscriptionLink(subscriptionRequest);
        return Response.seeOther(hppUrl).build();
    }

    @GET
    @Path("/callback")
    public TemplateInstance callback(@QueryParam("mandate_id") String mandateId){
        // subs otherwise
        var subscription = subscriptionService.getSubscriptionById(mandateId);
        return callback.data(subscription);
    }
}
