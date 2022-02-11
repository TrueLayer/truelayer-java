package com.truelayer.quarkusmvc.models;

import lombok.Data;

import javax.ws.rs.FormParam;

@Data
public class DonationRequest {
    @FormParam("name")
    private String name;
    @FormParam("amount")
    private int amount;
    @FormParam("email")
    private String email;
}
