package truelayer.java.payments.entities;

import lombok.*;
import truelayer.java.payments.entities.paymentmethod.PaymentMethod;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreatePaymentRequest {
    private int amountInMinor;

    private CurrencyCode currency;

    private PaymentMethod paymentMethod;

    private User user;

    public static AmountInMinorStep builder() {
        return new CreatePaymentRequestBuilder();
    }

    public static class CreatePaymentRequestBuilder implements AmountInMinorStep, CurrencyStep, PaymentMethodStep, BuildStep {

        private CreatePaymentRequest createPaymentRequest;

        public CreatePaymentRequestBuilder() {
            createPaymentRequest = new CreatePaymentRequest();
        }

        @Override
        public CurrencyStep amountInMinor(int amountInMinor) {
            createPaymentRequest.amountInMinor = amountInMinor;
            return this;
        }

        @Override
        public PaymentMethodStep currency(CurrencyCode currency) {
            createPaymentRequest.currency = currency;
            return this;
        }

        @Override
        public BuildStep paymentMethod(PaymentMethod paymentMethod) {
            createPaymentRequest.paymentMethod = paymentMethod;
            return this;
        }

        @Override
        public BuildStep user(User user) {
            createPaymentRequest.user = user;
            return this;
        }

        @Override
        public CreatePaymentRequest build() {
            return createPaymentRequest;
        }
    }


    public interface AmountInMinorStep {
        CurrencyStep amountInMinor(int amountInMinor);
    }

    public interface CurrencyStep {
        PaymentMethodStep currency(CurrencyCode currency);
    }

    public interface PaymentMethodStep {
        BuildStep paymentMethod(PaymentMethod paymentMethod);
    }

    public interface BuildStep {
        BuildStep user(User user);
        CreatePaymentRequest build();
    }
}
