package org.beko.exception;

public class PlaceNotFoundException extends RuntimeException {

    /**
     * Constructs a new WorkspaceNotFoundException with the specified detail message.
     *
     * @param message the detail message (which is saved for later retrieval by the getMessage() method).
     */
    public PlaceNotFoundException(String message) {
        super(message);
    }
}
