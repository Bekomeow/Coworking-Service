package org.beko.exception;

/**
 * Exception thrown when a place already exists.
 */
public class PlaceAlreadyExistException extends RuntimeException {
    /**
     * Constructs a new PlaceAlreadyExistException with the specified detail message.
     *
     * @param message the detail message
     */
    public PlaceAlreadyExistException(String message) {
        super(message);
    }
}
