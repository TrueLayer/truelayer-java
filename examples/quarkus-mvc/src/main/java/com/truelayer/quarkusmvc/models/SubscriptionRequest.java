package com.truelayer.quarkusmvc.models;

import lombok.Data;

import javax.ws.rs.FormParam;

@Data
public class SubscriptionRequest {
    @FormParam("amount")
    private int amount;
}
