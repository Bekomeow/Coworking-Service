package org.beko.exception;

public class PlaceAlreadyBookedException extends RuntimeException {

    public PlaceAlreadyBookedException(String message) {
        super(message);
    }
}
