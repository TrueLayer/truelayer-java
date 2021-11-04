package truelayer.java;

import truelayer.java.payments.entities.CreatePaymentRequest;

public class TestsUtil {

    public static CreatePaymentRequest getCreatePaymentRequest() {
        CreatePaymentRequest.PaymentMethod paymentMethod =
                new CreatePaymentRequest.PaymentMethod("bank_transfer");
        CreatePaymentRequest.PaymentBeneficiary paymentBeneficiary =
                new CreatePaymentRequest.PaymentBeneficiary("merchant_account", "c54104a5-fdd1-4277-8793-dbfa511c898b");
        CreatePaymentRequest.PaymentUser paymentUser =
                new CreatePaymentRequest.PaymentUser("new", "Giulio Leso");
        paymentUser.setEmail("g@gmail.com");

        CreatePaymentRequest createPaymentRequest = new CreatePaymentRequest();
        createPaymentRequest.setAmountInMinor(1);
        createPaymentRequest.setCurrency("GBP");
        createPaymentRequest.setPaymentMethod(paymentMethod);
        createPaymentRequest.setBeneficiary(paymentBeneficiary);
        createPaymentRequest.setUser(paymentUser);
        return createPaymentRequest;
    }
}
