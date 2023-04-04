package com.pocketrice;
//(c) A+ Computer Science
//www.apluscompsci.com
//Name - lucas xie

import java.util.Scanner;
import java.util.regex.Pattern;

import static com.pocketrice.AnsiCode.*;

public class Program {
    public static void main(String[] args) throws Exception {
        WordSpy ws = new WordSpy(8, "APPLEXYPXLHJKEEDEGGLLXXCGFPDGOGNMYNTAHUUPUQDGBTSBTHIGHMSILKXLTHIS");
        String[] presets = "APPLE AXE APEX CAT HEX EGG HAT COMPUTER GUM THIS TUG THIGH".split(" ");
        System.out.println(ws);

        if (presets.length != 0) System.out.println("\n\n* Spying presets...");
        ws.spyWords(presets);


        while (true) {
            switch (prompt("\n\nDone. Next?\n" + ANSI_BLUE + "(Input ENTRY for additional entries, SEED to set new seed, or QUIT.)" + ANSI_RESET, "Error: invalid choice.\n\n", new String[]{"entry", "quit", "seed"}, false, false)) {
                case "entry" -> {
                    System.out.println("\n\n\n\n");
                    ws.spyWords(prompt("Enter the word to spy for.", "Error: must match regex [a-zA-Z].", Pattern.compile("[a-zA-Z]"), false));
                }

                case "quit" -> {
                    System.exit(0);
                }

                case "seed" -> {
                    ws.setSeed((long) prompt("Set the new seed (current: " + ((ws.getSeed() == 0) ? "N/A" : 0) + ").", "Error: invalid number. Due to limitations it must be an integer.\n\n", Integer.MIN_VALUE, Integer.MAX_VALUE, true));
                    System.out.println(ANSI_GREEN + "Done!\n\n\n\n\n\n\n" + ANSI_RESET);
                    System.out.println(ws);
                }
            }
        }


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


    public static String prompt(String message, String errorMessage, Pattern regex, boolean lineMode) // <+> APM
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

            if (regex.matcher(nextInput).find()) {
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
}
