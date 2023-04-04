package com.pocketrice;

import java.util.*;
import java.util.regex.Pattern;

import static com.pocketrice.AnsiCode.*;
import static com.pocketrice.AnsiCode.ANSI_RESET;
import static com.pocketrice.Assortment.SortType;

public class Program {
    public static void main(String[] args) {
        Stack<Comparable[]> presets = new Stack<>();
        presets.addAll(List.of(new Integer[]{9, 5, 3, 2}, new Integer[]{19, 52, 3, 2, 7, 21}, new Integer[]{68, 66, 11, 2, 42, 31})); // <-- Change preset data here.

        SortType[] sortTypes = Arrays.stream(SortType.values()).filter(v -> v.index() >= 0).toList().toArray(new SortType[0]);
        Assortment sorter = new Assortment();

        if (presets.size() != 0) {
            System.out.println("\n\n* Sorting presets...\n");
            yieldWait(ANSI_BLUE, "Ready to sort set.");

            for (Comparable[] preset : presets) {
                sorter.setArr(preset);
                for (SortType st : sortTypes) {
                    sorter.setSort(st);
                    sorter.sort();
                    if (st == SortType.BUBBLE) {
                        yieldWait(ANSI_RED, "Finished with set.");
                        System.out.print("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
                    }
                    else {
                        yieldWait(ANSI_BLUE, "Ready for next sort.");
                        System.out.print("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
                    }
                }
            }
        }

        while (true) {
            switch (prompt("\n\n" + ((presets.size() != 0) ? "Presets done." : "Ready.") + " Next?\n" + ANSI_BLUE + "(Input RANDOM for random data, ENTRY to manually input data, or QUIT.)" + ANSI_RESET, "Error: invalid choice.\n\n", new String[]{"random", "entry", "quit"}, false, false)) {
                case "random" -> {
                    presets.clear();

                    System.out.println("\n\n\n\n");
                    sorter.setArr(new Assortment().getArr());
                    System.out.println("Generated " + sorter.getArr().length + " data (" + Arrays.toString(sorter.getArr()).replaceAll("[\\[\\]]", "") + ").");
                    yieldWait(ANSI_CYAN, "Ready to sort set.");

                    for (SortType st : sortTypes) {
                        sorter.setSort(st);
                        sorter.sort();
                        if (st == SortType.BUBBLE) {
                            yieldWait(ANSI_RED, "Finished with set.");
                            System.out.print("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
                        }
                        else {
                            yieldWait(ANSI_BLUE, "Ready for next sort.");
                            System.out.print("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
                        }
                    }
                }


                case "entry" -> {
                    presets.clear();

                    boolean isArrSet = false;
                    System.out.println("\n\n\n\n");

                    while (!isArrSet) {
                        String arrStr = prompt("Enter the array to sort (use [a,b,c] notation; spaces are OK).", "Error: must match regex \\[\\s*([0-9]+,\\s*)*\\s*[0-9]+].", Pattern.compile("\\[\\s*([0-9]+,\\s*)*\\s*[0-9]+]"), true);
                        Comparable[] arr = Arrays.stream(arrStr.substring(1, arrStr.length() - 1).split(",")).map(s -> Integer.parseInt(s.trim())).toList().toArray(new Comparable[0]);
                        sorter.setArr(arr);

                        if (!Assortment.validateArr(arr)) {
                            isArrSet = (prompt(ANSI_YELLOW + "\n\nYour inputted array " + arrStr + " is not recommended for sorting (either already sorted, non-distinct items, or size < 2)." + ANSI_RESET + "\nConfirm whether you want to continue with this array.", "Error: choose YES or NO.", new String[]{"yes", "no"}, false, false).equals("yes"));
                            System.out.print("\n\n\n\n");
                        }
                        else {
                            isArrSet = true;
                        }
                    }

                    for (SortType st : sortTypes) {
                        sorter.setSort(st);
                        sorter.sort();
                        if (st == SortType.BUBBLE) {
                            yieldWait(ANSI_RED, "Finished with set.");
                            System.out.print("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
                        }
                        else {
                            yieldWait(ANSI_BLUE, "Ready for next sort.");
                            System.out.print("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
                        }
                    }
                }

                case "quit" -> {
                    System.exit(0);
                }
            }
        }
    }

    public static String prompt(String message, boolean lineMode, boolean isCaseSensitive) // <+> APM
    {
        Scanner input = new Scanner(System.in);
        String nextInput;

        System.out.print(message);
        if (!message.equals(""))
            System.out.println();

        if (lineMode) {
            nextInput = input.nextLine();
        } else {
            nextInput = input.next();
        }

        if (!isCaseSensitive) {
            nextInput = nextInput.toLowerCase();
        }

        return nextInput;
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

    public static void yieldWait(AnsiCode color, String message) {
        prompt(color + message + " Type anything to continue..." + ANSI_RESET, false, false);
    }
}