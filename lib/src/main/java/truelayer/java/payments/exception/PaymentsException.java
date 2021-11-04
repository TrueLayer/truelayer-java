package truelayer.java.payments.exception;

import lombok.Getter;

@Getter
public class PaymentsException extends RuntimeException {

    private final String errorCode;
    private final String errorMessage;

    public PaymentsException(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public PaymentsException(Exception e) {
        this.errorCode = "GENERIC_ERROR";
        this.errorMessage = e.getMessage();
    }
}
