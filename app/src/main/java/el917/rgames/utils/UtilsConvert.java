package el917.rgames.utils;

import java.text.DecimalFormat;

public class UtilsConvert {

   public static String mmToInch(String mm) {
        DecimalFormat decimalFormat = new DecimalFormat("###.####");
        String value = mm.replace(",", ".");
        try {
            value = decimalFormat.format(Double.parseDouble(value) / 25.4).replace(",", ".");
        } catch (NumberFormatException e) {
            value = "-";
        }


        return value;
    }
    public static String inchTomm(String inch) {
        DecimalFormat decimalFormat = new DecimalFormat("###.####");
        String value = inch.replace(",", ".");
        try {
            value = decimalFormat.format(Double.parseDouble(value) * 25.4).replace(",", ".");
        } catch (NumberFormatException e) {
            value = "-";
        }
        return value;
    }

    public static String doubleToString(double value) {
        DecimalFormat decimalFormat = new DecimalFormat("###.####");
        String s;
        try {
            s = decimalFormat.format(value).replace(",", ".");
        } catch (NumberFormatException e) {
            s = "-";
        }


        return s;
    }





}
