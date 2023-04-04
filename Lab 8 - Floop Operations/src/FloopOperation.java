//(c) A+ Computer Science
//www.apluscompsci.com
//Name - Lucas Xie

// ** Note: this is all four labs combined into one. I would like "multTable" to be graded. You can check this lab by typing "mt(int, int)".

// You can call any of the four labs by typing a 'command' -- type help() for the format. Thanks!


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class FloopOperation {
    static Scanner input = new Scanner(System.in);

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";

    public static void main(String[] args)
    {
        boolean isFirstTime = true;
        FloopOperation floopOp = new FloopOperation();

        while (true) {
            if (isFirstTime) {
                System.out.println(ANSI_CYAN + "Input a floop operation command (options are cp, mt, ls, fld). Type 'help()' for formatting help. Use 'end()' to terminate the program." + ANSI_RESET);
                isFirstTime = false;
            }
            else
                System.out.println(ANSI_CYAN + "\n\nInput a floop operation command. Use 'end()' to terminate program." + ANSI_RESET);

            boolean wasErrorThrownOrCommandValid = false, isNoParentheses = false;
            String action = input.nextLine().trim().toLowerCase();
            String floopCommand = action.split("\\(")[0];
            String[] floopArgs = new String[0];

            try {
                floopArgs = action.substring(action.indexOf("(") + 1, action.indexOf(")")).split(",");
            }
            catch (Exception e) {
                System.out.println(ANSI_RED + "Error: commands must be followed by parentheses!");
                System.out.println("Acceptable formats are:");
                System.out.println("\t* cp(string) = countPairs");
                System.out.println("\t* mt(int, int) = multTable");
                System.out.println("\t* ls(int, int) = loopStats");
                System.out.println("\t* fld(int, int, int) = forLoopDemo" + ANSI_RESET);
                isNoParentheses = true;
            }

            if (!isNoParentheses) {
                switch (floopCommand) {
                    case "cp" -> { // Tip: replacing : with -> prevents fall-through (so no need for break;!)
                        if (floopArgs.length == 1 && !intchecker(floopArgs)) // cp only has 1 string-type arg.
                        {
                            System.out.println(ANSI_GREEN + "\nThe string " + floopArgs[0] + " has " + floopOp.countPairs(floopArgs[0]) + " character pairs." + ANSI_RESET);
                            wasErrorThrownOrCommandValid = true;
                        }
                    }
                    case "mt" -> {
                        if (floopArgs.length == 2 && intchecker(floopArgs)) {
                            int[] floopData = floopOp.multTable(Integer.parseInt(floopArgs[0]), Integer.parseInt(floopArgs[1]));


                            System.out.println(ANSI_YELLOW + "\n\t\tMULTIPLES OF " + floopArgs[0]);
                            System.out.println("o--====--===========--====--o");
                            System.out.println("|\t\t\t\t\t\t\t|");

                            if (floopData.length == 0) {
                                System.out.println("|\t\t  No data!  \t\t|");
                            }
                            for (int i = 0; i < floopData.length; i++) {
                                System.out.println("|\t" + (i + 1) + " * " + floopArgs[0] + "\t\t\t  " + floopData[i] + "  \t|");
                            }

                            System.out.println("|\t\t\t\t\t\t\t|");
                            System.out.println("o--====--===========--====--o" + ANSI_RESET);
                            wasErrorThrownOrCommandValid = true;
                        }
                    }
                    case "ls" -> {
                        if (floopArgs.length == 2 && intchecker(floopArgs)) {
                            int[] floopData = floopOp.loopStats(Integer.parseInt(floopArgs[0]), Integer.parseInt(floopArgs[1]));

                            System.out.println(ANSI_BLUE + "\n\tFLOOP STATS FOR (" + floopArgs[0] + ", " + floopArgs[1] + ")");
                            System.out.println("o--====---============---====--o");
                            System.out.println("|\t\t\t\t\t\t\t   |");
                            System.out.println("|\tTotal sum: " + floopData[0] + "\t\t\t   |");
                            System.out.println("|\tEven i: " + floopData[1] + "\t\t\t\t   |");
                            System.out.println("|\tOdd i: " + floopData[2] + "\t\t\t\t   |");
                            System.out.println("|\t\t\t\t\t\t\t   |");
                            System.out.println("o--====---============---====--o" + ANSI_RESET);

                            wasErrorThrownOrCommandValid = true;
                        }
                    }


                    // Note to self: super hard-coded but still awful with numbers that go beyond 10!

                    case "fld" -> { //<!> Crappy code.
                        if (floopArgs.length == 3 && intchecker(floopArgs)) {
                            Object[] floopData = floopOp.floopDemo(Integer.parseInt(floopArgs[0]), Integer.parseInt(floopArgs[1]), Integer.parseInt(floopArgs[2]));

                            if (floopData.length == 0) {
                                floopData = new Object[]{"No data!"};
                            }

                            System.out.println(ANSI_PURPLE + "\n   FLOOP (start = " + floopArgs[0] + ", stop = " + floopArgs[1] + ", increment = " + floopArgs[2] + ")");
                            System.out.println(borderize(floopData));
                            System.out.println("|\t\t" + " ".repeat(borderize(floopData).length() - 14) + "\t\t|");
                            System.out.println("|\t\t" + Arrays.toString(floopData).replace("[", "").replace("]", "") + whitespacize(borderize(floopData).length(), floopData) + "|");
                            System.out.println("|\t\t" + " ".repeat(borderize(floopData).length() - 14) + "\t\t|");
                            System.out.println(borderize(floopData) + ANSI_RESET);
                            wasErrorThrownOrCommandValid = true;
                        }
                    }

                    case "end" -> {
                        System.out.println(ANSI_BLUE + "Floop operations complete -- program terminated." + ANSI_RESET);
                        System.exit(0);
                    }

                    case "help" -> {
                        System.out.println(ANSI_BLUE + "\n************************************************************************************************************\n");
                        System.out.println("\tFloop ops, or for-loop operations, are done through commands -- formatted much like calling methods.");
                        System.out.println("\tPossible commands:");
                        System.out.println("\t\t* cp(string) = count char pairs");
                        System.out.println("\t\t* mt(int, int) = multiplication table");
                        System.out.println("\t\t* ls(int, int) = loop statistics");
                        System.out.println("\t\t* fld(int, int, int) = floop demo\n");
                        System.out.println("\tNote: you can only call one command per action.");
                        System.out.println(ANSI_BLUE + "\n************************************************************************************************************\n");
                        wasErrorThrownOrCommandValid = true;
                    }

                    default -> {
                        System.out.println(ANSI_RED + "Error: invalid command. Valid commands are cp, mt, ls, fld." + ANSI_RESET);
                        wasErrorThrownOrCommandValid = true;
                    }
                }

                if (!wasErrorThrownOrCommandValid) {
                    System.out.println(ANSI_RED + "Error: invalid args. Acceptable formats are:");
                    System.out.println("\t* cp(string) = countPairs");
                    System.out.println("\t* mt(int, int) = multTable");
                    System.out.println("\t* ls(int, int) = loopStats");
                    System.out.println("\t* fld(int, int, int) = forLoopDemo" + ANSI_RESET);
                }
            }
        }
    }

    public int countPairs(String string)
    {
        int numOfPairs = 0;

        for (int i = 0; i < string.length()-1; i++)
        {
            if (string.charAt(i) == string.charAt(i+1))
            {
                numOfPairs++;
            }
        }

        return numOfPairs;
    }

    public int[] multTable(int number, int numOfMults)
    {
        int[] mTable = new int[numOfMults];

        for (int i = 1; i <= numOfMults; i++)
        {
            mTable[i-1] = number * i;
        }

        return mTable;
    }

    public int[] loopStats(int start, int stop)
    {
        int totalSum = 0, evenIterations = 0, oddIterations = 0;

        for (int i = start; i <= stop; i++)
        {
            totalSum += i;

            if (i % 2 == 1)
                oddIterations++;
            else
                evenIterations++;
        }

        return new int[]{totalSum, evenIterations, oddIterations};
    }

    public Object[] floopDemo(int start, int stop, int increment)
    {
        List<Integer> floopData = new ArrayList<>();

        for (int i = start; i < stop; i += increment)
        {
            floopData.add(i);
        }

        return floopData.toArray(); // todo: make this return int[] rather than obj[]??
    }

    public static boolean intchecker(String[] strings) { // <+> Useful all-purpose method!
        boolean areAllInts = true;

        for (String string : strings) {
            try {
                Integer.parseInt(string);
            }
            catch(Exception e) {
                areAllInts = false;
            }
        }

        return areAllInts;
    }

    public static String borderize(Object[] floopData) // <!> Crappy code
    {
        // o--====---============---====--o

        int extendedPrintLength = Arrays.toString(floopData).length() - 24; // The length of data (excluding [], and 'o--====---' parts.) <!> HARDCODED OFFSET

        if (extendedPrintLength > 0)
            return "o--====---=============================" + "=".repeat(extendedPrintLength) + "---====--o";
        else
            return "o--====---=============================---====--o";
    }

    public static String whitespacize(int borderLength, Object[] floopData) // <!> Crappy code
    {
        int columnMisalignment = borderLength - Arrays.toString(floopData).length() - 7; // Should be 2 for removal of brackets, but <!> HARDCODED OFFSET b/c too lazy!

        return " ".repeat(columnMisalignment);
    }
}
