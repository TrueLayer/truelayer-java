package com.truelayer.quarkusmvc.models;

import javax.ws.rs.FormParam;

public class SubscriptionRequest {
    @FormParam("amount")
    private int amount;
}
