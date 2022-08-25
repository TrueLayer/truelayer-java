package com.truelayer.java.mandates.entities;

import lombok.Value;

@Value
public class GetConfirmationOfFundsResponse {
    Boolean confirmed;

    String confirmed_at;
}
