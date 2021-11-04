package truelayer.java.auth.exceptions;

import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@RequiredArgsConstructor
public class AuthenticationException extends Exception {

    String errorCode;

    String errorMessage;

    public AuthenticationException(Exception e){
        this.errorCode = "GENERIC_ERROR";
        this.errorMessage = e.getMessage();
    }
}
