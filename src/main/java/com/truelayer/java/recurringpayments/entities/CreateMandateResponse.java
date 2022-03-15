package com.truelayer.java.recurringpayments.entities;

import lombok.Value;

@Value
public class CreateMandateResponse {
    String id;

    String resourceToken;

    User user;

    @Value
    public static class User {
        String id;
    }
}
