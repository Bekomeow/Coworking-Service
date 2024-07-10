package org.beko.exception;

/**
 * Exception thrown when a place is already booked.
 */
public class PlaceAlreadyBookedException extends RuntimeException {
    /**
     * Constructs a new PlaceAlreadyBookedException with the specified detail message.
     *
     * @param message the detail message
     */
    public PlaceAlreadyBookedException(String message) {
        super(message);
    }
}
