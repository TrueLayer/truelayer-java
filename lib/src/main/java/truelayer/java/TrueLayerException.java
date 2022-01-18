package truelayer.java;

public class TrueLayerException extends RuntimeException {
    public TrueLayerException(String message) {
        super(message);
    }

    public TrueLayerException(String message, Throwable e) {
        super(message, e);
    }
}
