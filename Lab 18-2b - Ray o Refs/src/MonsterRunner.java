//(c) A+ Computer Science
//www.apluscompsci.com
//Name - Lucas Xie

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.System.*;

public class MonsterRunner {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";

    public static void main(String[] args) {
        int size = (int) prompt(ANSI_CYAN + "❖ How many monsters is this horde hoarding?" + ANSI_RESET, "Error: invalid amount.", 0, Integer.MAX_VALUE, true);
        Monsters herd = new Monsters(size);

        switch (prompt(ANSI_BLUE + "Individually define each monster?" + ANSI_RESET, "Error: invalid choice.", new String[]{"yes", "y", "no", "n"}, false, false)) {
            case "yes", "y" -> {
                for (int i = 0; i < size; i++) {
                    String monstName = prompt("➢ Name of monster " + (i+1) + "?", "Error: invalid name.", new String[]{""}, false, true);
                    int monstSize = (int) prompt("➢ Size of monster " + (i+1) + "?", "Error: invalid number.", 0, Integer.MAX_VALUE, true);
                    Monster newMonst = new Monster(monstName, monstSize);
                    herd.set(i, newMonst);
                    System.out.print("\n\n\n");
                }
            }

            case "no", "n" -> {
                for (int i = 0; i < size; i++) {
                    Monster newMonst = new Monster(generateRandomName(), (int) proximityRandom(15,14,20));
                    herd.set(i, newMonst);
                }
            }
        }
        System.out.println(ANSI_BLUE + "\n\n◇ The herd reveals itself!\n" + herd + "\n");
        herd.sortHorde();
        System.out.println(ANSI_PURPLE + "\n\n◇ The horde of monsters rearranged themselves...\n" + herd + "\n" + ANSI_RESET);

        Monster[] largestMonsts = herd.getLargest();
        Monster[] smallestMonsts = herd.getSmallest();

        System.out.println("It appears " + grammaticParse(largestMonsts, "and") + " (" + largestMonsts[0].getSize() + ((largestMonsts.length == 1) ? ") is the largest of the pack." : ") are the largest of the pack."));
        System.out.println("...and " + grammaticParse(smallestMonsts, "and") + " (" + smallestMonsts[0].getSize() + ((smallestMonsts.length == 1) ? ") is the smallest." : ") are the smallest."));
    }

    public static String prompt(String message, String errorMessage, String[] bounds, boolean lineMode, boolean isCaseSensitive) // <+> APM
    {
        Scanner input = new Scanner(System.in);
        String nextInput;

        while (true)
        {
            System.out.print(message);
            if (!message.equals(""))
                System.out.println();

            if (lineMode) {
                input.nextLine();
                nextInput = input.nextLine();
            }
            else {
                nextInput = input.next();
            }

            if (!isCaseSensitive)
            {
                nextInput = nextInput.toLowerCase();

                for (int i = 0; i < bounds.length; i++)
                    bounds[i] = bounds[i].toLowerCase();
            }

            if (nextInput.matches(String.join("|", bounds)) || bounds[0].equals("")) {
                return nextInput;
            } else {
                System.out.println(ANSI_RED + errorMessage + ANSI_RESET);
            }

        }
    }

    public static double prompt(String message, String errorMessage, double min, double max, boolean isIntegerMode)
    {
        Scanner input = new Scanner(System.in);
        String nextInput;
        double parsedInput = 0;
        boolean isValid;

        while (true) {
            System.out.print(message);
            if (!message.equals(""))
                System.out.println();

            nextInput = input.next();
            try {

                if (!isIntegerMode) {
                    parsedInput = Double.parseDouble(nextInput);
                } else {
                    parsedInput = Integer.parseInt(nextInput);
                }

                input.nextLine();
                isValid = true;
            } catch (Exception e) {
                isValid = false;
            }

            if (parsedInput >= min && parsedInput <= max && isValid) {
                return parsedInput;
            } else {
                System.out.println(ANSI_RED + errorMessage + ANSI_RESET);
            }
        }
    }

    public static <T> String grammaticParse(T[] array, String conjunction) { // <+> APM
        StringBuilder gParsedString = new StringBuilder(Arrays.toString(array));

        gParsedString.deleteCharAt(0)
                .deleteCharAt(gParsedString.length()-1);

        if (array.length > 1 && !conjunction.equals("")) gParsedString.insert(gParsedString.lastIndexOf(",") + 1, " " + conjunction);

        return gParsedString.toString();
    }

    public static String generateRandomName() { // <+> APM
        StringBuilder name = new StringBuilder();
        boolean hasDoubleVowel = ((int) (Math.random() * 2) == 1);
        int nameLength = (int) proximityRandom(4, 1, 3);

        for (int i = 0; i < nameLength; i++) {
            name.append((i == 1 || (i == 2 && hasDoubleVowel)) ? regexedChar(Pattern.compile("[aeiouy]")) : regexedChar(Pattern.compile("[bcdfghjklmnpqrstvwxz]")));
        }

        return toCamelCase(name.toString(), true);
    }

    public static double proximityRandom(double base, double lowerOffset, double upperOffset) { // <+> APM
        return Math.random()*(lowerOffset + upperOffset) + base - lowerOffset;
    }

    public static char regexedChar(Pattern pattern) { // <+> APM
        while (true) {
            int rnd = (int) (Math.random() * 52);
            char base = (rnd < 26) ? 'A' : 'a';
            char genChar = (char) (base + rnd % 26);

            if (pattern.matcher(Character.toString(genChar)).matches()) return genChar;
        }
    }

    @NotNull
    public static String toCamelCase(String string, boolean isUpperCamel) { // <+> APM
        // Camel case: remove all spaces (parse char after space as capitalized).
        StringBuilder cameledString = (isUpperCamel) ? new StringBuilder(String.valueOf(string.charAt(0)).toUpperCase() + string.substring(1).toLowerCase()) : new StringBuilder(string.toLowerCase());

        for (int i = 0; i < cameledString.length(); i++) {
            if (cameledString.charAt(i) == ' ') {
                cameledString.replace(i+1, i+2, String.valueOf(cameledString.charAt(i+1)).toUpperCase());
            }
        }

        return cameledString.toString().replaceAll(" ", "");
    }

}