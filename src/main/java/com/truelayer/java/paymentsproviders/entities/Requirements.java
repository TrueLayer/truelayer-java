package com.truelayer.java.paymentsproviders.entities;

import lombok.*;

@Builder
@Getter
@ToString
@EqualsAndHashCode
public class Requirements {
    private PisConsent pis;

    private AisConsent ais;
}
