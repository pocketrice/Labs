// WP3 - Lucas Xie

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Palindrome {
    String splice;
    boolean isPalindromic;

    public Palindrome() {
        splice = "";
        isPalindromic = determinePalindromicity();
    }

    public Palindrome(String s) {
        splice = s;
        isPalindromic = determinePalindromicity();
    }

    public boolean determinePalindromicity() { // Yes, palindromicity is actually the word for "being palindromic". Which also is a weird word to say "being a palindrome". Hm.
        if (splice.length() <= 1)
            return false;

        for (int i = 0; i < depunctualize(splice).length(); i++) {
            if (depunctualize(splice).charAt(i) != (depunctualize(splice).charAt(depunctualize(splice).length() - i - 1)))
                return false;
        }

        return true;
    }

    public static String depunctualize(String splice) { // Rid of all invalid characters from string.
        StringBuilder parsedWord = new StringBuilder();

        for (int i = 0; i < splice.length(); i++) {
            String s = "" + splice.charAt(i);
            Matcher matcher = Pattern.compile("[a-z]").matcher(s);
            if (matcher.find())
                parsedWord.append(s);
        }

        return parsedWord.toString();
    }
}
