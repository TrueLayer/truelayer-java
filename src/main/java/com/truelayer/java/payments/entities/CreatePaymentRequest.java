package com.truelayer.java.payments.entities;

import com.truelayer.java.Utils;
import com.truelayer.java.entities.CurrencyCode;
import com.truelayer.java.payments.entities.paymentmethod.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
@EqualsAndHashCode
public class CreatePaymentRequest {

    @Positive(message = "amount in minor must be >= 1")
    private int amountInMinor;

    @NotNull(message = "currency must be set")
    private CurrencyCode currency;

    @NotNull(message = "payment method must be set")
    private PaymentMethod paymentMethod;

    private User user;

    public static class CreatePaymentRequestBuilder {

        public CreatePaymentRequest build() {
            CreatePaymentRequest createPaymentRequest =
                    new CreatePaymentRequest(amountInMinor, currency, paymentMethod, user);
            Utils.validateObject(createPaymentRequest);
            return createPaymentRequest;
        }
    }
}
