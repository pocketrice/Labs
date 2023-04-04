//(c) A+ Computer Science
//www.apluscompsci.com
//Name - Lucas Xie

// ** Note: this is all three labs combined into one. I would like "looped box" to be graded. You can check this lab with the input "lb(int)". For formatting info input "help()".

// You can test several test cases at once like this: "ma(command + command + command...)"
// Example: "ma(lb(3) + lb(2) + lb(5))" will run all three test cases!


import static java.util.Collections.*;
import java.util.*;

public class NestedFloopOperation {
    static Scanner input = new Scanner(System.in);
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_CYAN = "\u001B[36m";

    private final List<String> floopCache;

    public NestedFloopOperation(List<String> fCache) { // Precache some data
        floopCache = fCache;
    }

    public NestedFloopOperation() { // No precaching
        floopCache = new ArrayList<>();
    }

    public static void main(String[] args)
    {
        boolean isFirstTime = true;
        NestedFloopOperation nFloopOp = new NestedFloopOperation(new ArrayList<>(List.of("lb(3)", "lb(4)", "lb(5)", "lb(2)", "lb(1)")));

        if (nFloopOp.floopCache.size() != 0) {
            System.out.println(ANSI_BLUE + "Successfully precached " + nFloopOp.floopCache.size() + " data. Executing..." + ANSI_RESET);
        }

        while (true) {
            if (nFloopOp.floopCache.size() == 0) {
                if (isFirstTime) {
                    System.out.println(ANSI_CYAN + "Input a nested floop operation command (options are ts, lb, wt). Wrap with ma() for multiactions. Type 'help()' for formatting help. Use 'end()' to terminate the program." + ANSI_RESET);
                    isFirstTime = false;
                } else
                    System.out.println(ANSI_CYAN + "\n\nInput a nested floop operation command. Use 'end()' to terminate program. Need help? -- use 'help()'" + ANSI_RESET);

                executeFlOperation(input.nextLine().trim(), nFloopOp.floopCache, true);
            } else {
                processFlCache(nFloopOp.floopCache);
                nFloopOp.floopCache.clear();
            }
        }
    }

    public static boolean executeFlOperation (String action, List<String> flCache, boolean allowFeedback) {
        boolean wasErrorThrownOrCommandValid = false, isNoParentheses = false, wasSuccessfulExecution = true;

        String floopCommand = action.split("\\(")[0];
        List<String> floopArgs = new ArrayList<>();

        try {
            floopArgs = trimAll(action.substring(action.indexOf("(") + 1, action.indexOf(")")).split(","));
        } catch (Exception e) {
            if (allowFeedback) {
                System.out.println(ANSI_RED + "Error: commands must be followed by parentheses!");
                System.out.println("Acceptable formats are:"); // GET 'EM!
                System.out.println("\t* ts(int, char) = tripleSider");
                System.out.println("\t* lb(int) = loopBox");
                System.out.println("\t* wt(string) = wordTriangle");
                System.out.println("\t* ma(command + command...) = multiaction" + ANSI_RESET);

                wasSuccessfulExecution = false;
            }
            isNoParentheses = true;
        }

        if (!isNoParentheses) {
            switch (floopCommand) {
                case "ts" -> {
                    if (floopArgs.size() == 2 && intchecker(singletonList(floopArgs.get(0))) && isCharParsable(new String[]{floopArgs.get(1)}) && floopArgs.get(1).length() == 1) {
                        multiprint(tripleSider(Integer.parseInt(floopArgs.get(0)), floopArgs.get(1).charAt(0)));
                        wasErrorThrownOrCommandValid = true;
                    }
                }

                case "lb" -> {
                    if (floopArgs.size() == 1 && intchecker(floopArgs)) {
                        multiprint(loopBox(Integer.parseInt(floopArgs.get(0))));
                        wasErrorThrownOrCommandValid = true;
                    }
                }

                case "wt" -> {
                    multiprint(wordTriangle(floopArgs.get(0)));
                    wasErrorThrownOrCommandValid = true;
                }

                case "ma" -> { // ma(wt(arg, arg) + lb(arg))
                    if (flCache.size() != 0) {
                        return false; // No concurrent multiactions!
                    }
                    List<String> floopCommands = trimAll(action.substring(action.indexOf("(")+1, action.lastIndexOf(")")).trim().split("\\+"));

                    if (!(floopCommands.size() == 0)) {
                        System.out.println(ANSI_BLUE + "Multiaction valid and cached (" + floopCommands.size() + " actions). Executing..." + ANSI_RESET);

                        if (floopCommands.size() == 1)
                            System.out.println(ANSI_BLUE + "Redundancy warning: multiaction was called but only action was used." + ANSI_RESET);


                        flCache.addAll(floopCommands);

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
                    System.out.println("\tThis is a special suite of nested floop ops.\n");
                    System.out.println("\tPossible commands:");
                    System.out.println("\t\t* ts(int, char) = triple sider");
                    System.out.println("\t\t* lb(int) = looped box");
                    System.out.println("\t\t* wt(string) = word triangle\n");
                    System.out.println("\t\t* ma(command + command...) = multiaction");
                    System.out.println("\tNote: you normally can only call one command per action. Use multiaction instead for bulk ops.");
                    System.out.println("\n************************************************************************************************************\n" + ANSI_RESET);
                    wasErrorThrownOrCommandValid = true;
                }

                default -> {
                    if (allowFeedback) {
                        System.out.println(ANSI_RED + "Error: invalid command. Valid commands are ts, lb, wt, ma." + ANSI_RESET);
                    }
                    wasErrorThrownOrCommandValid = true;
                    wasSuccessfulExecution = false;
                }
            }

            if (!wasErrorThrownOrCommandValid) {
                if (allowFeedback) {
                    System.out.println(ANSI_RED + "Error: invalid args. Acceptable formats are:");
                    System.out.println("\t* ts(int, char) = tripleSider");
                    System.out.println("\t* lb(int) = loopBox");
                    System.out.println("\t* wt(string) = wordTriangle");
                    System.out.println("\t* ma(command + command...) = multiaction");
                }
                wasSuccessfulExecution = false;
            }
        }

        return wasSuccessfulExecution;
    }

    public static void processFlCache(List<String> flCache) {
        for (String cachedItem : flCache) {
            if (!executeFlOperation(cachedItem, flCache, false))
                System.out.println(ANSI_RED + "Error in cache execution: " + cachedItem + " (index " + flCache.indexOf(cachedItem) + ") could not execute." + ANSI_RESET);

            System.out.println();
        }
    }

    public static List<String> tripleSider(int lineCount, char letter) {
        List<String> compiledLines = new ArrayList<>();

        for (int i = 0; i < lineCount; i++) {
            StringBuilder compiledLine = new StringBuilder();
            for (int j = 0; j < lineCount - i; j++) // Optimization: compiledLine.append(" ".repeat(lineCount - i));
                compiledLine.append(" ");

            for (int k = compiledLine.length()-1; k < lineCount; k++)  // Optimization: compiledLine.append(letter.repeat(Math.max(0, lineCount - compiledLine.length()))); <-- need to Math.max to make sure no negatives
                compiledLine.append(letter);

            compiledLines.add(compiledLine.toString());
        }

        return compiledLines;
    }

    public static List<String> loopBox(int lineCount) {
        List<String> compiledLines = new ArrayList<>();

        for (int i = 0; i < lineCount; i++) {
            StringBuilder compiledLine = new StringBuilder();
            for (int j = 0; j < lineCount - i; j++) // Optimizable
                compiledLine.append("*");

            for (int k = compiledLine.length() - 1; k < lineCount; k++) // Optimizable
                compiledLine.append("#");

            compiledLines.add(compiledLine.toString());
        }

        return compiledLines;
    }

    public static List<String> wordTriangle(String word) {
        List<String> compiledLines = new ArrayList<>();

        for (int i = 0; i < word.length() - 1; i++) {
            //StringBuilder compiledLine = new StringBuilder(" ".repeat(1 + 2 * i));

            // ==================================================
            // REMOVE THIS SECTION IF USING OPTIMIZED VERSION ABOVE.
            StringBuilder compiledLine = new StringBuilder();
            for (int j = 0; j < 2 * i + 1; j++)
                compiledLine.append(" ");
            // ==================================================

            compiledLine.setCharAt(0, word.charAt(i));
            compiledLine.setCharAt(compiledLine.length() - 1, word.charAt(i));
            compiledLine.insert(0, " ".repeat(word.length()-i-1));

            compiledLines.add(compiledLine.toString());
        }

        compiledLines.add(new StringBuilder(word).reverse().deleteCharAt(word.length()-1) + word);

        return compiledLines;
    }

    public static boolean intchecker(List<String> strings) { // <+> All-purpose method
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

    public static <T> boolean isCharParsable(T[] objects) { // <+> APM
        for (T object : objects) {
            if (object instanceof Integer) {
                try {
                    char chObj = (char) object;
                } catch (Exception e) {
                    return false;
                }
            } else if (object instanceof String) {
                if (!(((String) object).length() == 1 && ((String) object).matches("[A-Za-z]")))
                    return false;
            }
        }
        return true;
    }

    public static <T extends Collection<E>, E> void multiprint(T collection) { // <+> APM
        for (int i = 0; i < collection.size(); i++)
            System.out.println(collection.toArray()[i]);
    }

    public static <T extends Collection<E>, E extends String> List<E> trimAll(T collection) { // <+> APM
        List<E> trimmedCollection = new ArrayList<>();

        for (E item : collection)
            trimmedCollection.add((E) item.trim());
        return trimmedCollection;
    }

    public static <T extends String> List<T> trimAll(T[] array) { // <+> APM (overloaded)
        List<T> trimmedArray = new ArrayList<>();

        for (T item : array)
            trimmedArray.add((T) item.trim());
        return trimmedArray; // Technical limitation: cannot feasibly return T[] -- have to create a list.
    }
}
