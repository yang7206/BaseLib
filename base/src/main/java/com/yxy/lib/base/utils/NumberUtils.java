package com.yxy.lib.base.utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NumberUtils {

    public static String formatKeepOneNumber(double number) {
        DecimalFormat formater = new DecimalFormat("##0.0", new DecimalFormatSymbols(Locale.US));
        return formater.format(number);
    }

    public static String formatToInt(double number) {
        DecimalFormat formater = new DecimalFormat("##0", new DecimalFormatSymbols(Locale.US));
        return formater.format(number);
    }

    public static String formatKeepTwoNumber(float number) {
        DecimalFormat formater = new DecimalFormat("##0.00", new DecimalFormatSymbols(Locale.US));
        return formater.format(number);
    }

    public static float formatKeepTwoNumberF(float number) {
        DecimalFormat formater = new DecimalFormat("##0.00", new DecimalFormatSymbols(Locale.US));
        return Float.parseFloat(formater.format(number));
    }

    public static int getIntFromString(String string) {
        Pattern p = Pattern.compile("[^0-9]");
        Matcher m = p.matcher(string);
        return Integer.parseInt(m.replaceAll("").trim());
    }

    public static String getDateString(int in) {
        return in < 10 ? "0" + in : in+"";
    }
}
