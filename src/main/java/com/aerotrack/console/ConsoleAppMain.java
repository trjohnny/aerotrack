// Main
package com.aerotrack.console;

import javax.swing.SwingUtilities;

public class ConsoleAppMain {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AerotrackApp aerotrackApp = new AerotrackApp();
        });
    }
}
