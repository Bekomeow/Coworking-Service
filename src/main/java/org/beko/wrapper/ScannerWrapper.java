package org.beko.wrapper;

import lombok.RequiredArgsConstructor;

import java.util.Scanner;

/**
 * A wrapper class for Scanner to facilitate dependency injection and encapsulation.
 */
@RequiredArgsConstructor
public class ScannerWrapper {
    private final Scanner scanner;

    /**
     * Reads the next line of input from the scanner.
     *
     * @return the next line of input as a String
     */
    public String nextLine() {
        return scanner.nextLine();
    }

    /**
     * Closes the underlying Scanner instance.
     */
    public void close() {
        scanner.close();
    }
}
