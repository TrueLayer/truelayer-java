package truelayer.java;

// todo support for problem details: https://github.com/TrueLayer/api-standards/blob/initial-draft/README.md#804
// i'd probably not use an exception to propagate errors to callers, instead a complex strcuture containing optionals
// field and an optional utility method like isSuccess() to discriminate
public class TrueLayerException extends RuntimeException {
    public TrueLayerException(String errorMessage){
        super(errorMessage);
    }
}
