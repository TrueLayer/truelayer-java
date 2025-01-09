package com.truelayer.quarkusmvc.models;

import jakarta.ws.rs.FormParam;
import lombok.Data;

@Data
public class DonationRequest {
    @FormParam("name")
    private String name;
    @FormParam("amount")
    private int amount;
    @FormParam("email")
    private String email;
}
