package truelayer.java.payments.exception;

import lombok.Getter;

@Getter
public class PaymentException extends RuntimeException {

    private final String errorCode;
    private final String errorMessage;

    public PaymentException(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public PaymentException(Exception e) {
        this.errorCode = "GENERIC_ERROR";
        this.errorMessage = e.getMessage();
    }
}
