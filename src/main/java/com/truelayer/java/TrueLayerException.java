package com.truelayer.java;

/**
 * Class that models TrueLayer runtime exceptions.
 */
public class TrueLayerException extends RuntimeException {
    public TrueLayerException(String message) {
        super(message);
    }

    public TrueLayerException(String message, Throwable e) {
        super(message, e);
    }
}
