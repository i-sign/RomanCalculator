package com.signin.calculator.roman.romancalculator;

/**
 * Created by signin on 10/12/2017 AD.
 */

public class Converter {

    public final int[] ROMAN_VALUES = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
    public final String[] ROMAN_CHAR = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};

    public int toNumerical(String roman) {
        for (int i = 0; i < ROMAN_CHAR.length; i++) {

            if (roman.startsWith(ROMAN_CHAR[i])) {
                return ROMAN_VALUES[i] + toNumerical(roman.replaceFirst(ROMAN_CHAR[i], ""));
            }
        }
        return 0;
    }

    public String toRoman(int num) {
        String roman = "";
        for (int i = 0; i < ROMAN_VALUES.length; i++) {
            while (num >= ROMAN_VALUES[i]) {
                roman += ROMAN_CHAR[i];
                num -= ROMAN_VALUES[i];
            }
        }
        return roman;
    }


}
