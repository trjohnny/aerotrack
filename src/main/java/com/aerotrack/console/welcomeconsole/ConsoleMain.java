// Main
package com.aerotrack.console.welcomeconsole;

import lombok.extern.slf4j.Slf4j;

import javax.swing.SwingUtilities;

@Slf4j
public class ConsoleMain {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ScanInputView scanInputView = new ScanInputView();
        });
    }
}
