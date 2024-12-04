package com.truelayer.java.paymentsproviders.entities;

import java.util.List;
import lombok.*;

@Builder
@Getter
@ToString
@EqualsAndHashCode
public class AisConsent {
    private List<Scope> scopes;
}
