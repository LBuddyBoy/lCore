//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package me.lbuddyboy.libraries.util;

public class NumberUtils {
    public NumberUtils() {
    }

    public static boolean isInteger(String s) {
        int radix = 10;
        int result = 0;
        int i = 0;
        int len = s.length();
        int limit = -2147483647;
        if (len > 0) {
            char firstChar = s.charAt(0);
            if (firstChar < '0') {
                if (firstChar == '-') {
                    limit = -2147483648;
                } else if (firstChar != '+') {
                    return false;
                }

                if (len == 1) {
                    return false;
                }

                ++i;
            }

            int digit;
            for(int multmin = limit / radix; i < len; result -= digit) {
                if ((digit = Character.digit(s.charAt(i++), radix)) < 0) {
                    return false;
                }

                if (result < multmin) {
                    return false;
                }

                if ((result *= radix) < limit + digit) {
                    return false;
                }
            }

            return true;
        } else {
            return false;
        }
    }

    public static boolean isShort(String input) {
        if (!isInteger(input)) {
            return false;
        } else {
            int value = Integer.parseInt(input);
            return value > -32768 && value < 32767;
        }
    }
}
