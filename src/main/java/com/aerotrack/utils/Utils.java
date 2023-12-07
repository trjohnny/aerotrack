package com.aerotrack.utils;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;

public class Utils {

    public static void addStyledText(String text, AttributeSet attributes, JTextPane textPane) {
        StyledDocument doc = textPane.getStyledDocument();
        try {
            doc.insertString(doc.getLength(), text, attributes);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }
        public static void appendErrorText(String text, JTextPane textPane) {
            // Imposta il colore del testo a rosso
            SimpleAttributeSet attributes = new SimpleAttributeSet();
            StyleConstants.setForeground(attributes, Color.RED);

            // Aggiunge il testo al JTextPane con gli attributi specificati
            addStyledText(text + "\n", attributes, textPane);
        }
}
