package com.truelayer.java.mandates.entities;

import com.truelayer.java.entities.CurrencyCode;
import com.truelayer.java.entities.User;
import com.truelayer.java.mandates.entities.mandate.Mandate;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
@EqualsAndHashCode
public class CreateMandateRequest {

    Mandate mandate;

    CurrencyCode currency;

    User user;

    Constraints constraints;
}
