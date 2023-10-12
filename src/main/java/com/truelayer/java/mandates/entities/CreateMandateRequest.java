package com.truelayer.java.mandates.entities;

import com.truelayer.java.entities.CurrencyCode;
import com.truelayer.java.entities.RelatedProducts;
import com.truelayer.java.entities.User;
import com.truelayer.java.mandates.entities.mandate.Mandate;
import java.util.Map;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
@EqualsAndHashCode
public class CreateMandateRequest {

    private Mandate mandate;

    private CurrencyCode currency;

    private User user;

    private Constraints constraints;

    private Map<String, String> metadata;

    private RelatedProducts relatedProducts;
}
