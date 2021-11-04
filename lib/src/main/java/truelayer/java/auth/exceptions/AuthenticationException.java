package truelayer.java.auth.exceptions;

import lombok.RequiredArgsConstructor;
import lombok.Value;

@RequiredArgsConstructor
public class AuthenticationException extends Exception {

    private final String errorCode;

    private final String errorMessage;

    public AuthenticationException(Exception e){
        this.errorCode = "GENERIC_ERROR";
        this.errorMessage = e.getMessage();
    }
}
