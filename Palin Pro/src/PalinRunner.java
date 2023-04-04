// WP3 - Lucas Xie

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class PalinRunner {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";

    public static void main(String[] args) throws InterruptedException {
        while (true) {
            Palindrome pd = new Palindrome(prompt("Enter a palindrome.", "Invalid input.", new String[]{""}, true, false));
            if (pd.splice.equals("q")) {
                System.out.println(ANSI_RED + "Program terminated." + ANSI_RESET);
                System.exit(0);
            }

           fancyDelay(500);

            if (pd.isPalindromic) {
                System.out.println("That is indeed a palindrome.");
            }
            else {
                System.out.println("That is unfortunately not a palindrome.");
            }

            System.out.println("\n\n\n");
        }
    }

    public static String prompt(String message, String errorMessage, String[] bounds, boolean lineMode, boolean isCaseSensitive) // <+> APM
    {
        Scanner input = new Scanner(System.in);
        String nextInput;

        while (true)
        {
            if (!(message.matches("NO_MESSAGE")))
            {
                System.out.println(message);
            }

            if (lineMode) {
                nextInput = input.nextLine();
                input.nextLine();
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

    public static void fancyDelay(long delay) throws InterruptedException { // Yoinked from SchudawgCannoneer
        int recursionCount = 0;
        System.out.print(ANSI_CYAN + "Working... /");

        while (recursionCount < 2) {
            TimeUnit.MILLISECONDS.sleep(delay);
            System.out.print("\b\u2014");
            TimeUnit.MILLISECONDS.sleep(delay);
            System.out.print("\b\\");
            TimeUnit.MILLISECONDS.sleep(delay);
            System.out.print("\b|");
            TimeUnit.MILLISECONDS.sleep(delay);
            System.out.print("\b/");
            recursionCount++;
        }
        System.out.print("\b\nDone!\n" + ANSI_RESET);
    }
}
