package com.aerotrack.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Utils {

    public static String convertDate(String inputDate) throws  ParseException{
        SimpleDateFormat inputFormat = new SimpleDateFormat("dd MMMM yyyy");
        Date date = inputFormat.parse(inputDate);
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");

        return outputFormat.format(date);
    }

    public static final Map<String, String> countryNameToCodeMap = new HashMap<>() {{
        put("France", "fr");
        put("United Kingdom", "gb");
        put("Italy", "it");
        put("Malta", "mt");
        put("Norway", "no");
        put("Netherlands", "nl");
        put("Ireland", "ie");
        put("Belgium", "be");
        put("Spain", "es");
        put("Denmark", "dk");
        put("Romania", "ro");
        put("Finland", "fi");
        put("Portugal", "pt");
        put("Poland", "pl");
        put("Germany", "de");
        put("Austria", "at");
        put("Hungary", "hu");
        put("Czech Republic", "ck");
        put("Greece", "gr");
        put("Sweden", "se");
    }};

}
