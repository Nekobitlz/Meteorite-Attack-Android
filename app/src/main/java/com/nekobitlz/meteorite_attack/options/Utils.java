package com.nekobitlz.meteorite_attack.options;

public class Utils {

    private Utils() { }

    /*
        Adding spaces between thousands
    */
    public static String formatMoney(String money) {
        StringBuilder result = new StringBuilder();
        int j = 0;

        for (int i = money.length(); i > 0; i--) {
            if (i % 3 == 0) result.append(" ");

            result.append(money.charAt(j));
            j++;
        }

        return result.toString().trim();
    }
}
