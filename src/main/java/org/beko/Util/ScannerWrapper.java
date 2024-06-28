package org.beko.Util;

import java.util.Scanner;

public class ScannerWrapper {
    private final Scanner scanner;

    public ScannerWrapper(Scanner scanner) {
        this.scanner = scanner;
    }

    public String nextLine() {
        return scanner.nextLine();
    }
}
