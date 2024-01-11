package com.aerotrack.utils;

import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.Color;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
        SimpleAttributeSet attributes = new SimpleAttributeSet();
        StyleConstants.setForeground(attributes, Color.RED);

        addStyledText(text + "\n", attributes, textPane);
        }

    public static String convertDate(String inputDate) throws  ParseException{
        SimpleDateFormat inputFormat = new SimpleDateFormat("dd MMMM yyyy");
        Date date = inputFormat.parse(inputDate);
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");

        return outputFormat.format(date);
    }
}
