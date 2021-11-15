package truelayer.java.auth.exceptions;

//todo support for problem details: https://github.com/TrueLayer/api-standards/blob/initial-draft/README.md#804
public class AuthenticationException extends Exception {
    public AuthenticationException(String errorMessage){
        super(errorMessage);
    }
}
