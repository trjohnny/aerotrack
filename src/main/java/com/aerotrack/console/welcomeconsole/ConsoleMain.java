// Main
package com.aerotrack.console.welcomeconsole;

import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.IOException;
import java.util.Objects;

@Slf4j
public class ConsoleMain {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ScanInputView scanInputView = new ScanInputView();
            scanInputView.setIconImage(new ImageIcon("src/main/resources/logo.png").getImage());
        });
    }
}

