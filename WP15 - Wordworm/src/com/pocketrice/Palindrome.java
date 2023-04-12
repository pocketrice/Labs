package com.pocketrice;// WP3 - Lucas Xie

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Palindrome {
    String str;
    boolean isPalindromic;

    public Palindrome() {
        str = "";
        isPalindromic = isPalindromic();
    }

    public Palindrome(String s) {
        str = s;
        isPalindromic = isPalindromic();
    }

    public void setString(String s) {
        str = s;
    }

    public boolean isPalindromic() {
        if (str.length() <= 1)
            return false;

        for (int i = 0; i < depunctualize(str).length(); i++) {
            if (depunctualize(str).charAt(i) != (depunctualize(str).charAt(depunctualize(str).length() - i - 1)))
                return false;
        }

        return true;
    }

    public static String depunctualize(String str) { // Rid of all invalid characters from string.
        StringBuilder parsedWord = new StringBuilder();

        for (int i = 0; i < str.length(); i++) {
            String s = "" + str.charAt(i);
            Matcher matcher = Pattern.compile("[a-z]").matcher(s);
            if (matcher.find())
                parsedWord.append(s);
        }

        return parsedWord.toString();
    }
}
