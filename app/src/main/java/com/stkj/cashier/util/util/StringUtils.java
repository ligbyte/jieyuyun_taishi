package com.stkj.cashier.util.util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import androidx.annotation.ArrayRes;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import java.util.IllegalFormatException;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2016/08/16
 *     desc  : utils about string
 * </pre>
 */
public final class StringUtils {

    private StringUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * Return whether the string is null or 0-length.
     *
     * @param s The string.
     * @return {@code true}: yes<br> {@code false}: no
     */
    public static boolean isEmpty(final CharSequence s) {
        return s == null || s.length() == 0;
    }

    /**
     * Return whether the string is null or whitespace.
     *
     * @param s The string.
     * @return {@code true}: yes<br> {@code false}: no
     */
    public static boolean isTrimEmpty(final String s) {
        return (s == null || s.trim().length() == 0);
    }

    /**
     * Return whether the string is null or white space.
     *
     * @param s The string.
     * @return {@code true}: yes<br> {@code false}: no
     */
    public static boolean isSpace(final String s) {
        if (s == null) return true;
        for (int i = 0, len = s.length(); i < len; ++i) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Return whether string1 is equals to string2.
     *
     * @param s1 The first string.
     * @param s2 The second string.
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean equals(final CharSequence s1, final CharSequence s2) {
        if (s1 == s2) return true;
        int length;
        if (s1 != null && s2 != null && (length = s1.length()) == s2.length()) {
            if (s1 instanceof String && s2 instanceof String) {
                return s1.equals(s2);
            } else {
                for (int i = 0; i < length; i++) {
                    if (s1.charAt(i) != s2.charAt(i)) return false;
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Return whether string1 is equals to string2, ignoring case considerations..
     *
     * @param s1 The first string.
     * @param s2 The second string.
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean equalsIgnoreCase(final String s1, final String s2) {
        return s1 == null ? s2 == null : s1.equalsIgnoreCase(s2);
    }

    /**
     * Return {@code ""} if string equals null.
     *
     * @param s The string.
     * @return {@code ""} if string equals null
     */
    public static String null2Length0(final String s) {
        return s == null ? "" : s;
    }

    /**
     * Return the length of string.
     *
     * @param s The string.
     * @return the length of string
     */
    public static int length(final CharSequence s) {
        return s == null ? 0 : s.length();
    }

    /**
     * Set the first letter of string upper.
     *
     * @param s The string.
     * @return the string with first letter upper.
     */
    public static String upperFirstLetter(final String s) {
        if (s == null || s.length() == 0) return "";
        if (!Character.isLowerCase(s.charAt(0))) return s;
        return (char) (s.charAt(0) - 32) + s.substring(1);
    }

    /**
     * Set the first letter of string lower.
     *
     * @param s The string.
     * @return the string with first letter lower.
     */
    public static String lowerFirstLetter(final String s) {
        if (s == null || s.length() == 0) return "";
        if (!Character.isUpperCase(s.charAt(0))) return s;
        return String.valueOf((char) (s.charAt(0) + 32)) + s.substring(1);
    }

    /**
     * Reverse the string.
     *
     * @param s The string.
     * @return the reverse string.
     */
    public static String reverse(final String s) {
        if (s == null) return "";
        int len = s.length();
        if (len <= 1) return s;
        int mid = len >> 1;
        char[] chars = s.toCharArray();
        char c;
        for (int i = 0; i < mid; ++i) {
            c = chars[i];
            chars[i] = chars[len - i - 1];
            chars[len - i - 1] = c;
        }
        return new String(chars);
    }

    /**
     * Convert string to DBC.
     *
     * @param s The string.
     * @return the DBC string
     */
    public static String toDBC(final String s) {
        if (s == null || s.length() == 0) return "";
        char[] chars = s.toCharArray();
        for (int i = 0, len = chars.length; i < len; i++) {
            if (chars[i] == 12288) {
                chars[i] = ' ';
            } else if (65281 <= chars[i] && chars[i] <= 65374) {
                chars[i] = (char) (chars[i] - 65248);
            } else {
                chars[i] = chars[i];
            }
        }
        return new String(chars);
    }

    /**
     * Convert string to SBC.
     *
     * @param s The string.
     * @return the SBC string
     */
    public static String toSBC(final String s) {
        if (s == null || s.length() == 0) return "";
        char[] chars = s.toCharArray();
        for (int i = 0, len = chars.length; i < len; i++) {
            if (chars[i] == ' ') {
                chars[i] = (char) 12288;
            } else if (33 <= chars[i] && chars[i] <= 126) {
                chars[i] = (char) (chars[i] + 65248);
            } else {
                chars[i] = chars[i];
            }
        }
        return new String(chars);
    }

    /**
     * Return the string value associated with a particular resource ID.
     *
     * @param id The desired resource identifier.
     * @return the string value associated with a particular resource ID.
     */
    public static String getString(@StringRes int id) {
        return getString(id, (Object[]) null);
    }

    /**
     * Return the string value associated with a particular resource ID.
     *
     * @param id         The desired resource identifier.
     * @param formatArgs The format arguments that will be used for substitution.
     * @return the string value associated with a particular resource ID.
     */
    public static String getString(@StringRes int id, Object... formatArgs) {
        try {
            return format(Utils.getApp().getString(id), formatArgs);
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
            return String.valueOf(id);
        }
    }

    /**
     * Return the string array associated with a particular resource ID.
     *
     * @param id The desired resource identifier.
     * @return The string array associated with the resource.
     */
    public static String[] getStringArray(@ArrayRes int id) {
        try {
            return Utils.getApp().getResources().getStringArray(id);
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
            return new String[]{String.valueOf(id)};
        }
    }

    /**
     * Format the string.
     *
     * @param str  The string.
     * @param args The args.
     * @return a formatted string.
     */
    public static String format(@Nullable String str, Object... args) {
        String text = str;
        if (text != null) {
            if (args != null && args.length > 0) {
                try {
                    text = String.format(str, args);
                } catch (IllegalFormatException e) {
                    e.printStackTrace();
                }
            }
        }
        return text;
    }
    public static Bitmap stringToBitmap(String string) {
        Bitmap bitmap = null;
        try {
            byte[] bitmapArray = Base64.decode(string.split(",")[1], Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

}
