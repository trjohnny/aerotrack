// Main
package com.aerotrack.console.initial_console;

import lombok.extern.slf4j.Slf4j;

import javax.swing.SwingUtilities;

@Slf4j
public class ConsoleAppMain {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AerotrackApp aerotrackApp = new AerotrackApp();
        });
    }
}
