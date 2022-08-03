package com.truelayer.java.payments.entities;

import com.truelayer.java.entities.CurrencyCode;
import com.truelayer.java.entities.User;
import com.truelayer.java.payments.entities.paymentmethod.PaymentMethod;
import java.util.Map;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
@EqualsAndHashCode
public class CreatePaymentRequest {
    private int amountInMinor;

    private CurrencyCode currency;

    private PaymentMethod paymentMethod;

    private User user;

    private Map<String, String> metadata;
}
