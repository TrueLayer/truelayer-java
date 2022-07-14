package com.truelayer.java.payments.entities;

import com.truelayer.java.entities.User;
import lombok.Value;

@Value
public class CreatePaymentResponse {
    String id;

    User user;

    String resourceToken;
}
