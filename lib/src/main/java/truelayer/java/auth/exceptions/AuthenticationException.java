package truelayer.java.auth.exceptions;

public class AuthenticationException extends Exception {

    private final String errorCode;
    private final String errorMessage;

    public AuthenticationException(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public AuthenticationException(Exception e){
        this.errorCode = "GENERIC_ERROR";
        this.errorMessage = e.getMessage();
    }
}
