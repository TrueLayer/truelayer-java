package com.truelayer.java.signupplus.entities;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
@EqualsAndHashCode
public class GenerateAuthUriRequest {
    private String paymentId;

    private String state;
}
