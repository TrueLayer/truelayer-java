package com.truelayer.quarkusmvc.models;

import jakarta.ws.rs.FormParam;
import lombok.Data;

@Data
public class SubscriptionRequest {
    @FormParam("amount")
    private int amount;
}
