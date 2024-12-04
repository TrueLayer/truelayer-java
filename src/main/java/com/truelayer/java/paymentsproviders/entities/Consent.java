package com.truelayer.java.paymentsproviders.entities;

import lombok.*;

@Builder
@Getter
@ToString
@EqualsAndHashCode
public class Consent {
    private Requirements requirements;
}
